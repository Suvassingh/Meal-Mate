package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.databinding.ActivityLoginBinding
import com.example.app.databinding.ActivityStartBinding
import com.example.app.model.UserModel

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import java.util.zip.Inflater

class LoginActivity : AppCompatActivity() {
    private var userName:String?=null
    private lateinit var email:String
    private lateinit var password :String
    private lateinit var auth:FirebaseAuth
    private lateinit var database: DatabaseReference
    private  val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.donthaveaaccount.setOnClickListener {
            val intent=Intent(this,SignUp::class.java)
            startActivity(intent)
        }
        // initialization of Firebase auth
        auth=Firebase.auth
        // initilization of Firebase database
        database=Firebase.database.reference
        //Login with email and password
        binding.login.setOnClickListener{
            //get data from text field

            email=binding.emailAddress.text.toString().trim()
            password=binding.password.text.toString().trim()

            if (email.isBlank()||password.isBlank()){
                Toast.makeText(this, "Please Enter All The Detail...", Toast.LENGTH_SHORT).show()
            }else{
                createUser()
                Toast.makeText(this, "Login Successful...", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun createUser() {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){
                val user=auth.currentUser
                updateUi(user)
            }else{
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
                    if (task.isSuccessful){
                        saveUserData()
                        val user=auth.currentUser
                        updateUi(user)
                    }else{
                        Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }

    private fun saveUserData() {
        // getting data from text field
        email=binding.emailAddress.text.toString().trim()
        password=binding.password.text.toString().trim()
        val user=UserModel(userName,email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        // save data into database
        database.child("user").child(userId).setValue(user)

    }

    private fun updateUi(user: FirebaseUser?) {
        val intent = Intent ( this, MainActivity::class.java)
        startActivity(intent)
    }


}