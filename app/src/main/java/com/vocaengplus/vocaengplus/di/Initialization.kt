package com.vocaengplus.vocaengplus.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.vocaengplus.vocaengplus.model.data.Category
import com.vocaengplus.vocaengplus.model.data.CommunityWriter
import com.vocaengplus.vocaengplus.model.data.UserAccount
import com.vocaengplus.vocaengplus.model.data.Voca
import java.text.SimpleDateFormat
import java.util.*

object Initialization { //singleton
    lateinit var  databaseref:DatabaseReference
    lateinit var  firebaseAuth:FirebaseAuth
    lateinit var firebaseUser:FirebaseUser
    lateinit var uid:String
    lateinit var firebaseStorage: StorageReference


    fun setDatabase(){
        databaseref =FirebaseDatabase.getInstance().getReference("VocaEngPlus")
        firebaseAuth =FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance().reference
    }

    fun setCurrentUser(){
        firebaseUser = firebaseAuth.currentUser!!
        uid = firebaseUser.uid.toString()
    }

    fun getDBref():DatabaseReference{
        return databaseref
    }

    fun getFBauth():FirebaseAuth{
        return firebaseAuth
    }

    fun getFBuser():FirebaseUser{
        return firebaseUser
    }

    fun getuid():String{
        return uid
    }

    fun getStorageref():StorageReference{
        return firebaseStorage
    }


    fun getdate() : String{
        val now : Long = System.currentTimeMillis()
        val mdate : Date = Date(now)
        val simpelDate : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val getTime : String = simpelDate.format(mdate)
        return getTime
    }

    fun initData(type:String){
        //해당 월의 내용 추가한 내용 반영해서 초기화
        var date= getdate()
        when(type){
            "addWord"->{
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("testcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("totalcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("corcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("wrongcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("addCategory").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("deleteCategory").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("addWord").setValue(1)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("deleteWord").setValue(0)
            }

            "deleteWord"->{
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("testcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("totalcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("corcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("wrongcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("addCategory").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("deleteCategory").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("addWord").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("deleteWord").setValue(1)

            }

            "deleteCategory"->{
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("testcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("totalcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("corcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("wrongcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("addCategory").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("deleteCategory").setValue(1)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("addWord").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("deleteWord").setValue(0)

            }

            "addCategory"->{
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("testcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("totalcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("corcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("Test").child("wrongcount").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("addCategory").setValue(1)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("deleteCategory").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("addWord").setValue(0)
                databaseref.child("UserLog").child(uid.toString()).child(date.substring(0,7)).child("AddDelete").child("deleteWord").setValue(0)

            }
        }



    }

    fun initUserData(nickname:String){
        val getTime : String = getdate()

        // email, pw, registerdate, nickname
        var categorynames=ArrayList<String>()
        var categorydata=ArrayList<Category>()

        val useraccount = UserAccount(
            firebaseUser?.email.toString(),nickname,
            uid.toString(),categorynames,categorydata)
        //UserData
        databaseref.child("UserData").child(uid.toString()).setValue(useraccount)

        val user= databaseref.child("UserData").child(uid.toString())

        user.child("downloadNames").child("토플 영단어").setValue("토플 영단어")


        var toefl=ArrayList<Voca>()
        user.child("downloadData").child("토플 영단어").setValue(Category("토플 영단어","Default","Default",getTime,"토플 필수 영단어",toefl))

        user.child("downloadData").child("토플 영단어").child("words").child("abrupt").setValue(Voca("토플 영단어","abrupt","갑작스러운",0))
        user.child("downloadData").child("토플 영단어").child("words").child("constitute").setValue(Voca("토플 영단어","constitute","구성하다",0))
        user.child("downloadData").child("토플 영단어").child("words").child("scold").setValue(Voca("토플 영단어","scold","꾸짖다",0))
        user.child("downloadData").child("토플 영단어").child("words").child("principle").setValue(Voca("토플 영단어","principle","원칙",0))
        user.child("downloadData").child("토플 영단어").child("words").child("link").setValue(Voca("토플 영단어","link","결합",0))
        user.child("downloadData").child("토플 영단어").child("words").child("flee").setValue(Voca("토플 영단어","flee","피하다",0))
        user.child("downloadData").child("토플 영단어").child("words").child("bound").setValue(Voca("토플 영단어","bound","경계",0))
        user.child("downloadData").child("토플 영단어").child("words").child("suspend").setValue(Voca("토플 영단어","suspend","연기하다",0))
        user.child("downloadData").child("토플 영단어").child("words").child("withhold").setValue(Voca("토플 영단어","withhold","보류하다",0))
        user.child("downloadData").child("토플 영단어").child("words").child("hazardous").setValue(Voca("토플 영단어","hazardous","위험한",0))


        user.child("downloadNames").child("토익 영단어").setValue("토익 영단어")

        var toeic=ArrayList<Voca>()
        user.child("downloadData").child("토익 영단어").setValue(Category("토익 영단어","Default","Default",getTime,"토익 필수 영단어",toeic))

        user.child("downloadData").child("토익 영단어").child("words").child("remove").setValue(Voca("토익 영단어","remove","벗다",0))
        user.child("downloadData").child("토익 영단어").child("words").child("clear off").setValue(Voca("토익 영단어","clear off","치우다",0))
        user.child("downloadData").child("토익 영단어").child("words").child("carve").setValue(Voca("토익 영단어","carve","조각하다",0))
        user.child("downloadData").child("토익 영단어").child("words").child("lift").setValue(Voca("토익 영단어","lift","들다",0))
        user.child("downloadData").child("토익 영단어").child("words").child("gather").setValue(Voca("토익 영단어","gather","모이다",0))
        user.child("downloadData").child("토익 영단어").child("words").child("agreement").setValue(Voca("토익 영단어","agreement","계약서",0))
        user.child("downloadData").child("토익 영단어").child("words").child("neighborhood").setValue(
            Voca("토익 영단어","neighborhood","동네",0)
        )
        user.child("downloadData").child("토익 영단어").child("words").child("or so").setValue(Voca("토익 영단어","or so","정도,쯤",0))
        user.child("downloadData").child("토익 영단어").child("words").child("shine").setValue(Voca("토익 영단어","shine","비추다",0))
        user.child("downloadData").child("토익 영단어").child("words").child("beverage").setValue(Voca("토익 영단어","beverage","음료",0))

        user.child("downloadNames").child("수능 영단어").setValue("수능 영단어")

        var sooneung=ArrayList<Voca>()
        user.child("downloadData").child("수능 영단어").setValue(Category("수능 영단어","Default","Default",getTime,"수능 필수 영단어",sooneung))

        user.child("downloadData").child("수능 영단어").child("words").child("accounting").setValue(Voca("수능 영단어","accounting","회계",0))
        user.child("downloadData").child("수능 영단어").child("words").child("candidate").setValue(Voca("수능 영단어","candidate","후보자",0))
        user.child("downloadData").child("수능 영단어").child("words").child("deserve").setValue(Voca("수능 영단어","deserve","받을 자격(가치)가 있다",0))
        user.child("downloadData").child("수능 영단어").child("words").child("delay").setValue(Voca("수능 영단어","delay","지연시키다",0))
        user.child("downloadData").child("수능 영단어").child("words").child("surface").setValue(Voca("수능 영단어","surface","표면",0))
        user.child("downloadData").child("수능 영단어").child("words").child("absence").setValue(Voca("수능 영단어","absence","결석,부재",0))
        user.child("downloadData").child("수능 영단어").child("words").child("application").setValue(Voca("수능 영단어","application","응용",0))
        user.child("downloadData").child("수능 영단어").child("words").child("flexible").setValue(Voca("수능 영단어","flexible","유연한,신축성 있는",0))
        user.child("downloadData").child("수능 영단어").child("words").child("gratitude").setValue(Voca("수능 영단어","gratitude","감사,고마움",0))
        user.child("downloadData").child("수능 영단어").child("words").child("notepad").setValue(Voca("수능 영단어","notepad","메모지",0))

        user.child("downloadNames").child("오답노트").setValue("오답노트")

    }

    fun initCommunity(){
        databaseref.child("Community").child(uid.toString()).setValue(CommunityWriter(0,0,0,0,HashMap<String,Boolean>()))
    }

    fun initUserLog(){
        val getTime : String = getdate()

        databaseref.child("UserLog").child(uid.toString()).child(getTime.substring(0,7)).child("Test").child("testcount").setValue(0)
        databaseref.child("UserLog").child(uid.toString()).child(getTime.substring(0,7)).child("Test").child("totalcount").setValue(0)
        databaseref.child("UserLog").child(uid.toString()).child(getTime.substring(0,7)).child("Test").child("corcount").setValue(0)
        databaseref.child("UserLog").child(uid.toString()).child(getTime.substring(0,7)).child("Test").child("wrongcount").setValue(0)
        databaseref.child("UserLog").child(uid.toString()).child(getTime.substring(0,7)).child("AddDelete").child("addCategory").setValue(0)
        databaseref.child("UserLog").child(uid.toString()).child(getTime.substring(0,7)).child("AddDelete").child("deleteCategory").setValue(0)
        databaseref.child("UserLog").child(uid.toString()).child(getTime.substring(0,7)).child("AddDelete").child("addWord").setValue(0)
        databaseref.child("UserLog").child(uid.toString()).child(getTime.substring(0,7)).child("AddDelete").child("deleteWord").setValue(0)
    }

}