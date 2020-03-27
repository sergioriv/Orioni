package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime

class CreateProduct : AppCompatActivity() {
    private lateinit var txtProductName:EditText
    private lateinit var txtProductPrice:EditText
    private lateinit var txtProductDate:EditText


    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()
    //Solo hacemos referencia hasta la colección ya que vamos a agreagar documentos
    private val docRef = db.collection("products")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        txtProductName = findViewById(R.id.TB_ProductName)
        txtProductPrice = findViewById(R.id.TB_ProductPrice)
    }

    fun crear(view: View){
        val name:String = txtProductName.text.toString()
        val price:Double = txtProductPrice.text.toString().toDouble()
        val date:Timestamp = Timestamp.now()

        val product = hashMapOf(
            "name" to name,
            "price" to price,
            "created_at" to date
        )

        db.collection("products")
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.d("Exito", "DocumentSnapshot added with ID: ${documentReference.id}")

                //No puedo limpiar
                /*txtProductName.text.clear()
                txtProductDate.text.clear()*/
                Toast.makeText(this, "Subido correctamente", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Log.w("SinExito", "Error adding document", e)
            }
    }

    fun verProductos(view: View){
        //El cambio de ventanas también jode
        startActivity(Intent(this, MainActivity::class.java))
    }
}
