package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChangePassword : AppCompatActivity() {
    private lateinit var actualPassword: EditText
    private lateinit var newPassword: EditText
    private lateinit var confirmPassword: EditText

    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        actualPassword = findViewById(R.id.ET_ActualPassword)
        newPassword = findViewById(R.id.ET_NewPassword)
        confirmPassword = findViewById(R.id.ET_ConfirmPassword)
    }

    fun updatePassword(view:View){
        Toast.makeText(this, "Procesando...", Toast.LENGTH_SHORT).show()

        //Según documentación debe de reautenticarse recientemente el usuario
        // para poder cambiar la contraseña
        val credential = EmailAuthProvider.getCredential(user?.email.toString(), actualPassword.text.toString())
        user?.reauthenticate(credential)?.addOnCompleteListener { task ->
            if(task.isSuccessful){
                if(newPassword.text.toString().length >= 6){
                    if (newPassword.text.toString() == confirmPassword.text.toString()){
                        user?.updatePassword(newPassword.text.toString())?.addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                Toast.makeText(this, "contraseña actualizada", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MyProfile::class.java))
                            }else{
                                Toast.makeText(this, "Error al actualizar contraseña", Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "La contraseña deben ser de minimo 6 caracteres", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "Contraseña actual incorrecta", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun verPerfil(view: View){
        startActivity(Intent(this, MyProfile::class.java))
    }
}
