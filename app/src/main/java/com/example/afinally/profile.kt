package com.example.afinally

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.afinally.DataClasses.User
import com.example.afinally.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        dbRef=FirebaseDatabase.getInstance().getReference("users")

       dbRef.addValueEventListener(object:ValueEventListener{
           override fun onDataChange(dataSnapshot: DataSnapshot){
               val value=dataSnapshot.getValue(User::class.java)

               var email=binding.email.text.toString()
               var name=binding.name.text.toString()

           }


           override fun onCancelled(error: DatabaseError) {
               TODO("Not yet implemented")
           }
       })

        binding.delete.setOnClickListener {
            val id=intent.getStringExtra("id").toString()

            var currentUser=auth.currentUser

            currentUser?.delete()?.addOnCompleteListener {
                Toast.makeText(this,"Google auth delete fail",Toast.LENGTH_SHORT).show()
            }?.addOnFailureListener {
                Toast.makeText(this,"Google auth delete fail",Toast.LENGTH_SHORT).show()
            }

            dbRef.child(id).removeValue().addOnCompleteListener {
                Toast.makeText(this,"Data delete success",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Data delete Unsuccess",Toast.LENGTH_SHORT).show()
            }
        }

        binding.update.setOnClickListener {

            val Uemail=binding.email.text.toString()
            val Uname=binding.name.text.toString()

            val CurreUser=auth.currentUser

            CurreUser?.updateEmail(Uemail)?.addOnCompleteListener {
                Toast.makeText(this, "update email Success", Toast.LENGTH_SHORT).show()
            }?.addOnFailureListener {
                Toast.makeText(this, "Failed to update email", Toast.LENGTH_SHORT).show()
            }
                  updateData(Uname,Uemail)
        }

        binding.signOutBtn.setOnClickListener{
            //logout
                Firebase.auth.signOut()

                //redirect user to login page
                intent = Intent(applicationContext, welcompage::class.java)
                startActivity(intent)

                //toast message
            Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()

        }

    }
    fun updateData(name:String,email:String){
        val updatedata= mapOf<String,String>(
            "name" to name,
            "email" to email
        )

        var id = intent.getStringExtra("id").toString()

        dbRef.child(id).updateChildren(updatedata)
            .addOnSuccessListener {
                Toast.makeText(this,"update details Success",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Details Update Unsuccess",Toast.LENGTH_LONG).show()
            }
    }

}