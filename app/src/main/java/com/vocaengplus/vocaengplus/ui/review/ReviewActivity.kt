package com.vocaengplus.vocaengplus.ui.review

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.vocaengplus.vocaengplus.adapter.ReviewAdapter
import com.vocaengplus.vocaengplus.databinding.ActivityReviewBinding
import com.vocaengplus.vocaengplus.databinding.ReviewhelpBinding
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.model.data.Voca

class ReviewActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ReviewAdapter
    lateinit var data:ArrayList<Voca>
    lateinit var binding: ActivityReviewBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    lateinit var databaseReference: DatabaseReference
    lateinit var uid:String
    val initialization= Initialization

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init() {
        binding.engWordTextView.text="단어를 선택해주세요"
        binding.meaningTextView.visibility= View.INVISIBLE
        binding.blackBox.visibility= View.INVISIBLE

        //단어 가져오기
        initRecyclerView()

        binding.helpbtn.setOnClickListener {
            val dlgBinding= ReviewhelpBinding.inflate(layoutInflater)
            val dlgbuilder= AlertDialog.Builder(this)
            dlgbuilder.setView(dlgBinding.root)
                    .setNeutralButton("확인",null)
            val dlg=dlgbuilder.create()
            dlg.show()
        }

    }

    private fun initRecyclerView() {
        firebaseAuth= initialization.getFBauth()
        firebaseUser=initialization.getFBuser()
        databaseReference= initialization.getDBref()
        uid=initialization.getuid()

        recyclerView=binding.reviewRecyclerView
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val query=databaseReference.child("UserData").child(uid.toString()).child("downloadData")
                .child("오답노트").child("words").orderByChild("word")

        val option = FirebaseRecyclerOptions.Builder<Voca>()
                .setQuery(query, Voca::class.java)
                .build()

        adapter = ReviewAdapter(option)
        adapter.itemClickListener=object: ReviewAdapter.OnItemClickListener {

            override fun OnItemClick(holder: ReviewAdapter.ViewHolder, view: View, position: Int) {
                binding.engWordTextView.text=holder.word.text.toString().toUpperCase()
                binding.meaningTextView.text=holder.meaning.text.toString()

                binding.meaningTextView.visibility= View.INVISIBLE
                binding.blackBox.visibility= View.VISIBLE

                binding.blackBox.setOnClickListener {
                    binding.blackBox.visibility= View.INVISIBLE
                    binding.meaningTextView.visibility= View.VISIBLE
                }
            }
        }
        recyclerView.adapter=adapter

        adapter.startListening()

    }

}