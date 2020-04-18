package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateProduct : AppCompatActivity() {
    private lateinit var txtProductName:EditText
    private lateinit var txtProductPrice:EditText


    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        txtProductName = findViewById(R.id.TB_ProductName)
        txtProductPrice = findViewById(R.id.TB_ProductPrice)
    }

    fun crear(view: View){
        Toast.makeText(this, "Procesando...",
            Toast.LENGTH_SHORT).show()
        val name:String = txtProductName.text.toString()
        val price:Double = txtProductPrice.text.toString().toDouble()
        val date:Timestamp = Timestamp.now()

        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val product = Product(name, price, date, userId)

        var userProductsRef =  db.collection("User").document(userId)
            .collection("Products")

        userProductsRef.add(product).addOnCompleteListener { task ->
            if(task.isComplete){
                txtProductName.text.clear()
                txtProductPrice.text.clear()

                Toast.makeText(this, "Producto creado",
                    Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Error al crear producto",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    fun verProductos(view: View){
        startActivity(Intent(this, MainActivity::class.java))
    }
}
