package com.vocaengplus.vocaengplus.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.vocaengplus.vocaengplus.databinding.ActivityProfileBinding
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.ui.util.Validation

@Suppress("DEPRECATION")
class ProfileActivity : AppCompatActivity() {
    private val PICK_FROM_ALBUM=1
    private var pathUri:Uri?=null
    lateinit var binding: ActivityProfileBinding
    val initialization= Initialization
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    lateinit var databaseref:DatabaseReference
    lateinit var storageref:StorageReference
    lateinit var validation: Validation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        databaseref=initialization.getDBref()
        firebaseAuth=initialization.getFBauth()
        firebaseUser=initialization.getFBuser()
        storageref=initialization.getStorageref()
        validation= Validation

        binding.apply{
            storageref.child(firebaseUser.photoUrl.toString()).downloadUrl.addOnSuccessListener {
                Glide.with(this@ProfileActivity)
                        .load(it)
                        .circleCrop()
                        .into(profileProfileImg)
            }

            profileProfileUpload.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK) //앨범으로 이동
                intent.type=MediaStore.Images.Media.CONTENT_TYPE
                startActivityForResult(intent, PICK_FROM_ALBUM)
            }


            myidtext.setText(firebaseUser.email.toString())
            mynicknametext.setText(firebaseUser.displayName.toString())

            savebtn.setOnClickListener {
                val newnickname=mynicknametext.text.toString()
                if(newnickname.length==0){
                    Toast.makeText(this@ProfileActivity, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else if(!validation.isValidateNickname(newnickname)){
                    Toast.makeText(this@ProfileActivity, "닉네임은 2~8글자 사이여야 합니다.(특수문자 불가).", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else {
                    lateinit var profileUpdates:UserProfileChangeRequest
                    if(pathUri==null){
                        profileUpdates= userProfileChangeRequest{
                            displayName=newnickname
                        }
                        firebaseUser.updateProfile(profileUpdates)
                            .addOnCompleteListener {
                                if(it.isSuccessful){
                                    //데이터베이스에 저장
                                    databaseref.child("UserData").child(firebaseUser.uid.toString())
                                        .child("nickname").setValue(newnickname)

                                    databaseref.child("UserData").child(firebaseUser.uid).child("downloadData").get().addOnSuccessListener { utask->
                                        Log.i("userdata",utask.children.toString())
                                        for(c in utask.children){
                                            Log.i("cccC",c.child("categorywritertoken").value.toString())
                                            if(c.child("categorywritertoken").value.toString()==firebaseUser.uid){
                                                Log.i("categorywritertoken",c.child("categorywritertoken").value.toString())
                                                databaseref.child("UserData").child(firebaseUser.uid).child("downloadData").child(c.key.toString()).child("categorywriter").setValue(newnickname)
                                            }
                                        }
                                    }

                                    databaseref.child("Community").child(firebaseUser.uid).get().addOnSuccessListener {
                                        if(it.hasChild("uploads")){
                                            val uploads=ArrayList<String>()
                                            for(c in it.child("uploads").children){
                                                uploads.add(c.key.toString())
                                            }
                                            for(up in uploads){
                                                databaseref.child("Community").child("categories").child(up).child("categorywriter").setValue(newnickname)
                                            }
                                        }
                                        Toast.makeText(this@ProfileActivity, "계정 수정 완료", Toast.LENGTH_SHORT).show()
                                        setResult(RESULT_OK)
                                        finish()
                                    }
                                }
                            }
                    }
                    else{
                        val filename=initialization.getuid()+"_profileimg.jpg"
                        //사진 저장
                        val fileref=initialization.getStorageref().child("profile_images/"+filename)
                        val uploadTask=fileref.putFile(pathUri!!)

                        uploadTask.addOnSuccessListener {
                            profileUpdates= userProfileChangeRequest{
                                displayName=newnickname
                                photoUri=Uri.parse(fileref.path)
                            }
                            firebaseUser.updateProfile(profileUpdates)
                                .addOnCompleteListener {
                                    if(it.isSuccessful){
                                        //데이터베이스에 저장
                                        databaseref.child("UserData").child(firebaseUser.uid.toString())
                                            .child("nickname").setValue(newnickname)

                                        databaseref.child("UserData").child(firebaseUser.uid).child("downloadData").get().addOnSuccessListener { utask->
                                            Log.i("userdata",utask.children.toString())
                                            for(c in utask.children){
                                                Log.i("cccC",c.child("categorywritertoken").value.toString())
                                                if(c.child("categorywritertoken").value.toString()==firebaseUser.uid){
                                                    Log.i("categorywritertoken",c.child("categorywritertoken").value.toString())
                                                    databaseref.child("UserData").child(firebaseUser.uid).child("downloadData").child(c.key.toString()).child("categorywriter").setValue(newnickname)
                                                }
                                            }
                                        }

                                        databaseref.child("Community").child(firebaseUser.uid).get().addOnSuccessListener { ctask->
                                            if(ctask.hasChild("uploads")){
                                                val uploads=ArrayList<String>()
                                                for(c in ctask.child("uploads").children){
                                                    uploads.add(c.key.toString())
                                                }
                                                for(up in uploads){
                                                    databaseref.child("Community").child("categories").child(up).child("profile").setValue(fileref.path)
                                                    databaseref.child("Community").child("categories").child(up).child("categorywriter").setValue(newnickname)
                                                }

                                            }
                                            Toast.makeText(this@ProfileActivity, "계정 수정 완료", Toast.LENGTH_SHORT).show()
                                            setResult(RESULT_OK)
                                            finish()
                                        }

                                    }
                                }
                        }
                    }
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== PICK_FROM_ALBUM){
            if(resultCode== RESULT_OK){
                pathUri = data!!.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,pathUri)
                Glide.with(this@ProfileActivity)
                    .load(pathUri)
                    .circleCrop()
                    .into(binding.profileProfileImg)
            }
            else{
                Toast.makeText(this, "프로필 사진 변경을 취소하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}