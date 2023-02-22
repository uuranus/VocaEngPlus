package com.vocaengplus.vocaengplus.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.adapter.CommunityMyAdapter
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.databinding.FragmentCommunityMyBinding
import com.vocaengplus.vocaengplus.model.data.CommunityForLikeDownload
import com.vocaengplus.vocaengplus.model.data.CommunityForUpload

class CommunityMyFragment : Fragment() {
    var binding: FragmentCommunityMyBinding?=null
    val initialization= Initialization
    lateinit var recyclerview: RecyclerView
    lateinit var adapter: CommunityMyAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentCommunityMyBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTable()

        val data=ArrayList<CommunityForUpload>()
        Initialization.getDBref().child("UserData").child(Initialization.uid).child("downloadData").get().addOnSuccessListener {
            for(cld in it.children){
                if(cld.child("categorywritertoken").value== Initialization.uid &&cld.hasChild("words")){
                    val words=ArrayList<Voca>()
                    for(c in cld.child("words").children){
                        words.add(Voca(c.child("category").value.toString(),c.child("word").value.toString(),c.child("meaning").value.toString(),0))
                    }
                    val isliked = HashMap<String,Boolean>()
                    isliked.put("Default",true)
                    val isDownloaded = HashMap<String,Boolean>()
                    isDownloaded.put("Default",true)

                    val category= CommunityForUpload(cld.child("categoryname").value.toString(),
                        Initialization.getFBuser().photoUrl.toString(),cld.child("categorywriter").value.toString(),
                            cld.child("categorywritertoken").value.toString(),
                        Initialization.getdate(),cld.child("description").value.toString()
                            ,0,isliked,0,isDownloaded, words)

                    data.add(category)
                }
            }
            val uploads=ArrayList<String>()
            Initialization.getDBref().child("Community").child(Initialization.uid).get().addOnSuccessListener {
                if(it.hasChild("uploads")){
                    for(c in it.child("uploads").children){
                        uploads.add(c.key.toString())
                    }
                }
                initRecyclerView(data,uploads)
            }
        }
    }
    private fun initTable(){
        if(!Initialization.getFBuser().isAnonymous){
            Initialization.getDBref().child("Community").child(Initialization.uid).get().addOnSuccessListener {
                binding!!.communitymylike.text="${it.child("totalLike").value}회"
                binding!!.communitymyliked.text="${it.child("totalLiked").value}회"
                binding!!.communitymyupload.text="${it.child("totalUpload").value}개"
                binding!!.communitymydownloaded.text="${it.child("totalDownloaded").value}회"
            }
        }

    }

    private fun initRecyclerView(data:ArrayList<CommunityForUpload>, uploads:ArrayList<String>){
        recyclerview=binding!!.communitymycategoryrecyclerview
        recyclerview.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

        adapter = CommunityMyAdapter(data,uploads)

        adapter.itemClickListener=object: CommunityMyAdapter.OnItemClickListener {
            override fun OnItemClick(holder: CommunityMyAdapter.ViewHolder, view: View, position: Int) {
                if(Initialization.getFBuser().isAnonymous){
                    val builder= AlertDialog.Builder(requireContext())
                    builder.setMessage("게스트 계정은 업로드를 할 수 없습니다.")
                            .setPositiveButton("확인"){ _, _ ->

                            }
                    val dlg=builder.create()
                    dlg.show()
                    return
                }
                if(view.isSelected){
                    val builder= AlertDialog.Builder(requireContext())
                    builder.setMessage("이미 업로드한 단어장입니다.")
                            .setPositiveButton("확인"){ _, _ ->

                            }
                    val dlg=builder.create()
                    dlg.show()
                }
                else{
                    val builder= AlertDialog.Builder(requireContext())
                    builder.setMessage("해당 단어장을 올리시겠습니까?")
                            .setPositiveButton("네"){ _, _ ->
                                Initialization.getDBref().child("Community").child("categories").get().addOnSuccessListener {
                                    if(it.hasChild(data[position].categoryname)){ //동일 이름이 존재하면
                                        val builder2= AlertDialog.Builder(requireContext())
                                        builder2.setMessage("동일 이름의 단어장이 존재합니다.")
                                                .setPositiveButton("확인"){ _, _ ->

                                                }
                                        val dlg=builder2.create()
                                        dlg.show()
                                        return@addOnSuccessListener
                                    }
                                    val words=HashMap<String, Voca>()
                                    for(i in 0..data[position].words.size-1){
                                        words.put(data[position].words[i].word,
                                            Voca(data[position].words[i].category,data[position].words[i].word,data[position].words[i].meaning,0)
                                        )
                                    }
                                    Initialization.getDBref().child("Community").child("categories").child(data[position].categoryname)
                                            .setValue(CommunityForLikeDownload(data[position].categoryname,
                                                Initialization.getFBuser().photoUrl.toString(),data[position].categorywriter,
                                                Initialization.uid,
                                                Initialization.getdate(),data[position].description,0,data[position].likes,0,data[position].downloads,words))
                                    Initialization.getDBref()
                                        .child("Community").child(Initialization.uid).child("totalUpload").get().addOnSuccessListener {
                                        var tu=it.value.toString().toInt()
                                        Initialization.getDBref()
                                            .child("Community").child(Initialization.uid).child("totalUpload").setValue(++tu)
                                        Initialization.getDBref()
                                            .child("Community").child(Initialization.uid).child("uploads").child(data[position].categoryname).setValue(true)
                                        initTable()
                                    }
                                    view.isSelected=true
                                }

                            }
                            .setNegativeButton("아니오"){ _, _ ->
                            }
                    val dlg=builder.create()
                    dlg.show()
                }
            }
        }
        recyclerview.adapter=adapter
        if(data.size==0){
            binding!!.emptyrecyclervie.visibility= View.VISIBLE
        }
        else{
            binding!!.emptyrecyclervie.visibility= View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}