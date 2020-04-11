package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var txtUserEmail: EditText

    //Cosas de firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        txtUserEmail = findViewById(R.id.TB_UserEmail)

        auth = FirebaseAuth.getInstance()
    }

    fun resetPassword(view: View){
        val email:String = txtUserEmail.text.toString()

        if(!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this){
                    task ->
                    if(task.isSuccessful){
                        startActivity(Intent(this, LoginActivity::class.java))
                    }else{
                        Toast.makeText(this, "Error al enviar el correo de recuperación",
                            Toast.LENGTH_LONG).show()
                    }
                }
        }else{
            Toast.makeText(this, "Ingrese un correo",
                Toast.LENGTH_LONG).show()
        }
    }
}
