package com.vocaengplus.vocaengplus.ui.search

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.di.Initialization

class NaverAddDiaglog(v: View) :DialogFragment() {
    private val v=v

    lateinit var databaseref:DatabaseReference
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var firebaseUser:FirebaseUser
    lateinit var uid:String
    val initialization= Initialization

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        databaseref= initialization.getDBref()
        firebaseAuth= initialization.getFBauth()
        firebaseUser=initialization.getFBuser()
        uid=initialization.getuid()

        val spinner=v.findViewById<Spinner>(R.id.naveraddcategory)

        var categories=ArrayList<String>()

        databaseref.child("UserData").child(uid.toString()).child("downloadNames").get().addOnSuccessListener {
            for(child in it.children){
                if(child.value.toString()=="오답노트"){
                    continue
                }
                categories.add(child.value.toString())
            }

            spinner.adapter= ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line,categories)
            spinner.setSelection(0)
        }

        val dlgBuilder:androidx.appcompat.app.AlertDialog.Builder=androidx.appcompat.app.AlertDialog.Builder(
                requireContext(),android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)

        dlgBuilder.setView(v)
                .setPositiveButton("추가"){
            _,_ ->
                    val naveraddword=v.findViewById<TextView>(R.id.naveraddword)
                    val naveraddmeaning=v.findViewById<TextView>(R.id.naveraddmeaning)
                    val naveraddcategory=spinner.selectedItem.toString()

                    val addword=naveraddword.text.toString()
                    val addmeaning=naveraddmeaning.text.toString()

                    databaseref.child("UserData").child(uid.toString()).child("downloadData").child(naveraddcategory)
                            .child("words").child(addword).setValue(Voca(naveraddcategory,addword,addmeaning,0))


                    val date=initialization.getdate().substring(0,7)
                    //log추가
                    databaseref.child("UserLog").child(uid.toString()).get().addOnSuccessListener {
                        if(it.hasChild(date)){
                            databaseref.child("UserLog").child(uid.toString()).child(date).child("AddDelete").child("addWord").get().addOnSuccessListener {
                                var add=it.value.toString().toInt()
                                databaseref.child("UserLog").child(uid.toString()).child(date).child("AddDelete").child("addWord").setValue(++add)
                            }
                        }
                        else{
                            initialization.initData("addWord")
                        }
                    }
                    Toast.makeText(requireContext(),"단어 추가 완료",Toast.LENGTH_SHORT).show()

            }
                .setNegativeButton("취소"){
                    _,_ ->

                }

        val dlg=dlgBuilder.create()
        return dlg


    }

}