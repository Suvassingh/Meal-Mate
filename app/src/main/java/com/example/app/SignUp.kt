package com.example.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app.databinding.ActivityMainBinding
import com.example.app.databinding.ActivitySignUpBinding
import com.example.app.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

@Suppress("DEPRECATION")
class SignUp : AppCompatActivity() {
    private lateinit var userName:String
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference
    private val binding:ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        //initialize firebase auth
        auth = Firebase.auth
        //initialize firebase database
        database= Firebase.database.reference
        //initialize firebase database
        //googleSignInClient
        binding.createAccountButton.setOnClickListener {
            userName=binding.userName.text.toString()
            email=binding.emailAddress.text.toString().trim()
            password=binding.password.text.toString().trim()
            if (userName.isBlank()||email.isBlank()||password.isBlank()){
                Toast.makeText(this, "Please Fill All The Detail", Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }

        }
        binding.haveanaccount.setOnClickListener{
            val intent = Intent ( this, LoginActivity::class.java)
            startActivity(intent)
        }
    }




    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task->
            if (task.isSuccessful){
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Account Creation Failed ", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure",task.exception)
            }

        }


    }

    private fun saveUserData() {
        //retrieve data from input field
        userName=binding.userName.text.toString()
        email=binding.emailAddress.text.toString().trim()
        password=binding.password.text.toString().trim()
        val user = UserModel(userName,email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        //save data to Firebase database
        database.child("user").child(userId).setValue(user)

    }
}