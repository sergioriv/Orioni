package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var txtUserEmail:EditText
    private lateinit var txtUserPassword:EditText

    //Cosas de firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtUserEmail = findViewById(R.id.TB_UserEmail)
        txtUserPassword = findViewById(R.id.TB_UserPassword)

        auth = FirebaseAuth.getInstance()
    }

    fun register(view: View){
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun forgetPassword(view: View){
        startActivity(Intent(this, ForgetPasswordActivity::class.java))
    }

    fun login(view: View){
        val email:String = txtUserEmail.text.toString()
        val password:String = txtUserPassword.text.toString()

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){
                    task ->
                    if(task.isSuccessful){

                        val user = FirebaseAuth.getInstance().currentUser

                        //user?.isEmailVerified recupera si está verificado se pone ? para evitar
                        //se cierre si llega vacio
                        if (user?.isEmailVerified == true){
                            //Enviar a vista usuario logeado
                            startActivity(Intent(this, MainActivity::class.java))
                        }else{
                            Toast.makeText(this, "Verificar correo",
                                Toast.LENGTH_LONG).show()

                            //Esto es por si se venció el correo de verificación
                            user?.sendEmailVerification()

                            FirebaseAuth.getInstance().signOut()
                        }

                    }else{
                        Toast.makeText(this, "Credenciales invalidas",
                            Toast.LENGTH_LONG).show()
                    }
                }
        }else{
            Toast.makeText(this, "Todos los campos deben estar llenos",
                Toast.LENGTH_LONG).show()
        }
    }
}
