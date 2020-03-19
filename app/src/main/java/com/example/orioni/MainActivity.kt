package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.multidex.MultiDex
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var TB_ProductName:TextView
    private lateinit var TB_ProductPrice:TextView
    private lateinit var TB_ProductDate:TextView

    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()
    //Acceso a la base de datos colección products, al documento en especifico RoDhLdPlPoQuDwKOpGAD
    //Esas llaves primarias están bien vergas, pero la base noSQL se encarga de que sean unicas
    private val docRef = db.collection("products").document("RoDhLdPlPoQuDwKOpGAD")

    override fun onCreate(savedInstanceState: Bundle?) {
        MultiDex.install(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TB_ProductName = findViewById(R.id.TB_ProductName)
        TB_ProductPrice = findViewById(R.id.TB_ProductPrice)
        TB_ProductDate = findViewById(R.id.TB_ProductDate)

        //esto es la incersión pero no me deja los cambios de ventana
        //el codigo está en CreatrProduct
        /*val name:String = "camisa"
        val price:Double = 10000.0
        val date: Timestamp = Timestamp.now()

        val product = hashMapOf(
            "name" to name,
            "price" to price,
            "date" to date
        )

        db.collection("products")
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.d("Exito", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("SinExito", "Error adding document", e)
            }*/
    }

    //Separo esto para ver si el error es de mi red
    fun consultar(view: View){
        //Acceso al documento
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("Exito", "DocumentSnapshot data: ${document.data}")

                    //Asignamos los valores a los textbox
                    TB_ProductName.text = document.getString("name")

                    //El error está al querer de procesar mas de dos cosas
                    /*TB_ProductPrice.text = document.getString("price")
                    TB_ProductDate.text = document.getString("created_at")*/

                    //val nombre:String = document.getString("name").toString()
                    //val price:Int = document.getString("price").toString().toInt()
                    //val date:String =  document.getString("created_at").toString()

                    //Pongo estas alertas Provisionales
                    //Toast.makeText(this, "Nombre: ${document.getString("name")}", Toast.LENGTH_LONG).show()
                    //Toast.makeText(this, "Nombre: ${document.getString("price")}", Toast.LENGTH_LONG).show()
                    //Toast.makeText(this, "Nombre: ${document.getString("create_at")}", Toast.LENGTH_LONG).show()

                    //Toast.makeText(this, "Nombre: ${nombre}", Toast.LENGTH_LONG).show()
                    //Toast.makeText(this, "Nombre: ${price}", Toast.LENGTH_LONG).show()
                    //Toast.makeText(this, "Nombre: ${date}", Toast.LENGTH_LONG).show()
                } else {
                    Log.d("SinExito", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ErrorBD", "get failed with ", exception)
            }
    }

    fun crearProductos(view: View){
        //El cambio de ventanas también jode
        startActivity(Intent(this, CreateProduct::class.java))
    }
}
