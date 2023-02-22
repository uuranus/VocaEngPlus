package com.vocaengplus.vocaengplus.ui.community

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.adapter.CommunityMainAdapter
import com.vocaengplus.vocaengplus.databinding.FragmentCommunityMainBinding
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.model.data.CommunityCategory
import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.ui.util.Validation

class CommunityMainFramgent : Fragment() {
    var binding: FragmentCommunityMainBinding?=null
    lateinit var validation: Validation
    lateinit var recyclerview:RecyclerView
    lateinit var adapter: CommunityMainAdapter
    val initialization= Initialization
    var isliked=false
    var isDownloaded=false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentCommunityMainBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validation= Validation()
        binding!!.apply{
            searchbtn.setOnClickListener {
                if(!validation.checkInput(arrayOf(searchCategory.text.toString()))) {
                    Toast.makeText(requireContext(),"검색할 내용을 입력해주세요",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(!validation.isValidateCategoryName(searchCategory.text.toString())){
                    Toast.makeText(requireContext(),"단어장 이름이 올바르지 않습니다.",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                Initialization.getDBref().child("Community").child("categories").get().addOnSuccessListener {
                    val cateogries=ArrayList<CommunityCategory>()
                    for(chld in it.children){
                        if(chld.key.toString().contains(searchCategory.text.toString())){
                            val words=ArrayList<Voca>()
                            for(c in chld.child("words").children){
                                words.add(Voca(c.child("category").value.toString(),c.child("word").value.toString(),c.child("meaning").value.toString(),0))
                            }
                            isliked = chld.child("likes").hasChild(Initialization.getuid())
                            isDownloaded = chld.child("downloads").hasChild(Initialization.getuid())

                            val category= CommunityCategory(chld.child("categoryname").value.toString(),chld.child("profile").value.toString(),chld.child("categorywriter").value.toString(),
                                chld.child("categorywritertoken").value.toString(),chld.child("uploadDate").value.toString(),chld.child("description").value.toString()
                                ,chld.child("like").value.toString().toInt(),isliked,chld.child("download").value.toString().toInt(),isDownloaded, words)

                            cateogries.add(category)
                        }
                    }
                    if(cateogries.size!=0){
                        initRecyclerView(cateogries)
                    }

                }
            }

            searchCategory.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if(searchCategory.text.length==0){
                        totaldata("download")
                    }
                }

            })

            sortdownload.setOnClickListener {
                totaldata("download")
            }
            sortlike.setOnClickListener {
                totaldata("like")
            }
            sortrecent.setOnClickListener {
                totaldata("uploadDate")
            }


        }

        totaldata("download")

    }

    private fun totaldata(sorttype:String){ //download => 다운로드 순 like=> 좋아요 순 uploadDate -> 최신순
        Initialization.getDBref().child("Community").child("categories").orderByChild(sorttype).limitToLast(100).get().addOnSuccessListener {
            val data=ArrayList<CommunityCategory>()
            for(ctgy in it.children){
                val words=ArrayList<Voca>()
                for(c in ctgy.child("words").children){
                    words.add(Voca(c.child("category").value.toString(),c.child("word").value.toString(),c.child("meaning").value.toString(),0))
                }
                isliked = ctgy.child("likes").hasChild(Initialization.getuid())
                isDownloaded = ctgy.child("downloads").hasChild(Initialization.getuid())
                val category= CommunityCategory(ctgy.child("categoryname").value.toString(),ctgy.child("profile").value.toString(),ctgy.child("categorywriter").value.toString(),
                        ctgy.child("categorywritertoken").value.toString(), ctgy.child("uploadDate").value.toString(),ctgy.child("description").value.toString()
                        ,ctgy.child("like").value.toString().toInt(),isliked,ctgy.child("download").value.toString().toInt(), isDownloaded,words)

                data.add(category)
            }
            //오름차순으로 반환해오기 때문에 반대로 저장해야함
            when(sorttype){
                "download"->{
                    data.sortByDescending { it.download }
                }
                "like" ->{
                    data.sortByDescending { it.like}
                }
                "uploadDate" ->{
                    data.sortByDescending { it.uploadDate}
                }
            }
            initRecyclerView(data)
        }
    }

    private fun initRecyclerView(data:ArrayList<CommunityCategory>){
        recyclerview=binding!!.communityrecyclerview
        recyclerview.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

        adapter = CommunityMainAdapter(data)

        adapter.itemClickListener=object: CommunityMainAdapter.OnItemClickListener {
            override fun OnItemClick(holder: CommunityMainAdapter.ViewHolder, view: View, position: Int) {
                (activity as CommunityActivity).replaceFragment(CommunityDetailFragment(data[position]),"communitydetail")
            }
        }

        recyclerview.adapter=adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}