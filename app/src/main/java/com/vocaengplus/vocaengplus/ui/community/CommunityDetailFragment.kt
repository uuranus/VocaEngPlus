package com.vocaengplus.vocaengplus.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.vocaengplus.vocaengplus.adapter.CommunityDetailAdapter
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.databinding.FragmentCommunityDetailBinding
import com.vocaengplus.vocaengplus.model.data.Category
import com.vocaengplus.vocaengplus.model.data.CommunityCategory
import com.vocaengplus.vocaengplus.model.data.CommunityForLikeDownload
import com.vocaengplus.vocaengplus.model.data.CommunityWriter

class CommunityDetailFragment(val data: CommunityCategory) : Fragment() {
    var binding: FragmentCommunityDetailBinding?=null
    lateinit var recyclerView:RecyclerView
    lateinit var adapter: CommunityDetailAdapter
    val initialization= Initialization
    var islike:Boolean=false
    var isdonwload:Boolean=false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentCommunityDetailBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.apply{
            if(data.categorywritertoken== Initialization.uid){ //내가 올린 단어장이라면
                deletecategory.visibility=View.VISIBLE
                deletecategory.setOnClickListener {
                    val builder= AlertDialog.Builder(requireContext())
                    builder.setMessage("해당 단어장을 삭제하시겠습니까?")
                        .setPositiveButton("네"){ _, _ ->
                            Initialization.getDBref().child("Community").child("categories").child(data.categoryname).removeValue()
                            Initialization.getDBref().child("Community").child(Initialization.uid).child("uploads").child(data.categoryname).removeValue()
                            (activity as CommunityActivity).replaceFragment(CommunityMainFramgent(),"communitymain") //메인화면으로 돌아가기
                        }
                        .setNegativeButton("아니오"){ _, _ ->
                        }
                    val dlg=builder.create()
                    dlg.show()
                   
                }
            }

            communitycategoryname.text=data.categoryname.toString()
            communitywriter.text="${data.categorywriter}"
            communityupload.text="업로드  ${data.uploadDate}"
            communitydescription.text=data.description
            communitylike.text="${data.like}"
            communitydownload.text="${data.download}"

            if(data.isLiked){
                communitylikebtn.isSelected=true
                islike=true
            }
            else{
                communitylikebtn.isSelected=false
                islike=false
            }

            if(data.isDownloaded){
                communitydownloadbtn.isSelected=true
                isdonwload=true
            }
            else{
                communitydownloadbtn.isSelected=false
                isdonwload=false
            }

            binding!!.communitylikebtn.setOnClickListener {
                if(Initialization.getFBuser().isAnonymous){
                    val builder= AlertDialog.Builder(requireContext())
                    builder.setMessage("게스트 계정은 좋아요를 할 수 없습니다")
                            .setPositiveButton("확인"){ _, _ ->

                            }
                    val dlg=builder.create()
                    dlg.show()
                }
                else {
                    if (islike) {
                        it.isSelected = false
                        islike = false
                    } else {
                        it.isSelected = true
                        islike = true
                    }
                    updateHeart(Initialization.getDBref().child("Community").child("categories").child(data.categoryname.toString()))
                }
            }

            binding!!.communitydownloadbtn.setOnClickListener {
                if(Initialization.getFBuser().isAnonymous){
                    val builder= AlertDialog.Builder(requireContext())
                    builder.setMessage("게스트 계정은 다운로드를 받을 수 없습니다")
                            .setPositiveButton("확인"){ _, _ ->

                            }
                    val dlg=builder.create()
                    dlg.show()
                }
                else{
                    if(isdonwload){
                        val builder= AlertDialog.Builder(requireContext())
                        builder.setMessage("이미 다운로드 한 단어장입니다.\n다운받으시겠습니까?")
                                .setPositiveButton("네"){ _, _ ->
                                    //userdata에 추가하기 반복 다운이어도 overwrite 될 것이기 때문에 괜찮
                                    //하지만, 해당 단어장의 다운로드 수 내가 다운로드 한 수에는 변화 없음
                                    downloadCategory()
                                }
                                .setNegativeButton("아니오"){ _, _ ->
                                }
                        val dlg=builder.create()
                        dlg.show()
                    }
                    else{
                        val builder= AlertDialog.Builder(requireContext())
                        builder.setMessage("해당 단어장을 다운받으시겠습니까?")
                                .setPositiveButton("네"){ _, _ ->
                                    binding!!.communitydownloadbtn.setImageResource(R.drawable.ic_check)
                                    updateDownload(Initialization.getDBref().child("Community").child("categories").child(data.categoryname.toString()))
                                    isdonwload=true
                                    it.isSelected=true
                                }
                                .setNegativeButton("아니오"){ _, _ ->
                                }
                        val dlg=builder.create()
                        dlg.show()
                    }
                }

            }

        }

        Initialization.firebaseStorage.child(data.profile).downloadUrl.addOnSuccessListener {
            Glide.with(this)
                    .load(it)
                    .circleCrop()
                    .into(binding!!.writerprofileimg)
        }

        initRecyclerView()

    }

    private fun initRecyclerView(){
        recyclerView=binding!!.commmunitydetailrecyclerview
        recyclerView.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        adapter = CommunityDetailAdapter(data)

        recyclerView.adapter=adapter

    }

    private fun updateHeart(categoryRef:DatabaseReference){
        categoryRef.runTransaction(object:Transaction.Handler{
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val p=currentData.getValue(CommunityForLikeDownload::class.java)
                        ?:return Transaction.success(currentData)
                if(p.likes!!.containsKey(Initialization.uid)){ //좋아요를 눌렀다면
                    p.like=p.like-1
                    p.likes!!.remove(Initialization.uid)
                    Initialization.getDBref().child("Community").child(Initialization.uid).child("totalLike").get().addOnSuccessListener {
                        var likecount=it.value.toString().toInt()
                        if(likecount==1){
                            Initialization.getDBref().child("Community").child(Initialization.uid).child("totalLike").setValue(0)
                        }
                        else{
                            Initialization.getDBref().child("Community").child(Initialization.uid).child("totalLike").setValue(--likecount)
                        }
                    }
                    if(data.categorywritertoken!="Default"){
                        updateWriter(Initialization.getDBref().child("Community").child(data.categorywritertoken),true,false)
                    }
                }
                else{
                    p.like=p.like+1
                    p.likes!!.put(Initialization.uid,true)
                    Initialization.getDBref().child("Community").child(Initialization.uid).child("totalLike").get().addOnSuccessListener {
                        var likecount=it.value.toString().toInt()
                        Initialization.getDBref().child("Community").child(Initialization.uid).child("totalLike").setValue(++likecount)
                    }
                    if(data.categorywritertoken!="Default"){
                        updateWriter(Initialization.getDBref().child("Community").child(data.categorywritertoken),true,true)
                    }
                }

                currentData.value=p
                initLikeDownload()
                return Transaction.success(currentData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                //TODO("Not yet implemented")
            }

        })
    }

    private fun updateDownload(categoryRef:DatabaseReference){ //다운로드가 안 되어있는 상태에만 호출
        categoryRef.runTransaction(object:Transaction.Handler{
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val p=currentData.getValue(CommunityForLikeDownload::class.java)
                        ?:return Transaction.success(currentData)
                if(!(p.downloads!!.containsKey(Initialization.uid))) { //다운로드를 눌렀다면
                    p.download = p.download + 1
                    p.downloads!!.put(Initialization.uid, true)
                    if(data.categorywritertoken!="Default"){
                        updateWriter(Initialization.getDBref().child("Community").child(data.categorywritertoken),false,true)
                    }
                }

                currentData.value=p
                downloadCategory()
                initLikeDownload()
                return Transaction.success(currentData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                //TODO("Not yet implemented")
            }

        })
    }

    private fun updateWriter(writerRef:DatabaseReference,islike:Boolean,isUp:Boolean){
        writerRef.runTransaction(object:Transaction.Handler{
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val p=currentData.getValue(CommunityWriter::class.java)
                        ?:return Transaction.success(currentData)
                if(islike){ //좋아요 받은 것 업데이트
                    if(isUp) {
                        p.totalLiked = p.totalLiked + 1
                    }
                    else {
                        p.totalLiked = p.totalLiked - 1
                    }

                    currentData.value=p
                    return Transaction.success(currentData)
                }
                else{ //다운로드 받은 것 업데이트
                    if(isUp) {
                        p.totalDownloaded = p.totalDownloaded + 1
                    }
                    else {
                        p.totalDownloaded = p.totalDownloaded - 1
                    }

                    currentData.value=p
                    return Transaction.success(currentData)
                }

            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                //TODO("Not yet implemented")
            }

        })
    }

    private fun initLikeDownload(){
        Initialization.getDBref().child("Community").child("categories").child(data.categoryname).get().addOnSuccessListener {
            
            binding!!.communitylike.text="${it.child("like").value}"
            binding!!.communitydownload.text="${it.child("download").value}"
        }
    }

    private fun downloadCategory(){
        Initialization.getDBref().child("UserData").child(Initialization.uid).child("downloadData").child(data.categoryname).setValue(
            Category(data.categoryname,data.categorywriter,data.categorywritertoken,
                Initialization.getdate(),data.description,ArrayList<Voca>())
        )
        Initialization.getDBref().child("UserData").child(Initialization.uid).child("downloadNames").child(data.categoryname).setValue(data.categoryname)
        for(i in 0..data.words.size-1){
            Initialization.getDBref().child("UserData").child(Initialization.uid).child("downloadData").child(data.categoryname).child("words").child(data.words[i].word).setValue(
                Voca(data.words[i].category,data.words[i].word,data.words[i].meaning,0)
            )
        }

        //log값에 download한 것 추가
        Initialization.getDBref()
            .child("UserLog").child(Initialization.uid).get().addOnSuccessListener { snapshot ->
            if (!snapshot.hasChild(Initialization.getdate().substring(0, 7))) {
                Initialization.initData("addCategory")
            } else {
                var ac = snapshot.child(Initialization.getdate().substring(0, 7)).child("AddDelete").child("addCategory").value.toString().toInt()
                Initialization.getDBref().child("UserLog").child(Initialization.uid).child(
                    Initialization.getdate().substring(0, 7)).child("AddDelete").child("addCategory").setValue(++ac)
            }
        }
    }
}