package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var txtUserFirstName:EditText
    private lateinit var txtUserLastName:EditText
    private lateinit var txtUserEmail:EditText
    private lateinit var txtUserPassword:EditText
    private lateinit var txtConfirmUserPassword:EditText

    //Cosas de Firebase
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtUserFirstName = findViewById(R.id.Tb_UserFirstName)
        txtUserLastName = findViewById(R.id.Tb_UserLastName)
        txtUserEmail = findViewById(R.id.TB_UserEmail)
        txtUserPassword = findViewById(R.id.TB_UserPassword)
        txtConfirmUserPassword = findViewById(R.id.TB_ConfirmUserPassword)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    fun register (view: View){
        val firstName:String = txtUserFirstName.text.toString()
        val lastName:String = txtUserLastName.text.toString()
        val email:String = txtUserEmail.text.toString()
        val password:String = txtUserPassword.text.toString()
        val confirmPassword:String = txtConfirmUserPassword.text.toString()

        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email)
            && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)){
            if (password.length >= 6){
                if (password == confirmPassword){
                    Toast.makeText(this, "Registrando",
                        Toast.LENGTH_SHORT).show()
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this){
                                task ->
                            if (task.isComplete){
                                val user:FirebaseUser ?= auth.currentUser

                                //Correo verificación
                                user?.sendEmailVerification()
                                    ?.addOnCompleteListener(this){
                                            task ->
                                        if(task.isComplete){
                                            Toast.makeText(this, "Correo de verificación enviado",
                                                Toast.LENGTH_LONG).show()

                                            val completeUser = hashMapOf(
                                                "firstName" to firstName,
                                                "lastName" to lastName
                                            )

                                            //Nos dirijimos a la collección user, nos metemos en el documento con el id
                                            // de la persona registrada por auth, la creamos y le insertamos el nombre.
                                            //Al ser noSQL si no existe la crea.
                                            db.collection("User").document(user?.uid).set(completeUser)

                                            startActivity(Intent(this, LoginActivity::class.java))
                                        }else{
                                            Toast.makeText(this, "Error al enviar el correo de verificación",
                                                Toast.LENGTH_LONG).show()
                                        }
                                    }
                            }
                        }
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden",
                        Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "La contraseña debe tener mínimo 6 caracteres",
                    Toast.LENGTH_LONG).show()
            }

        }else{
            Toast.makeText(this, "Todos los campos deben ser completados",
                Toast.LENGTH_LONG).show()
        }
    }
}
