package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.multidex.MultiDex
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Timestamp

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
/*
        TB_ProductName = findViewById(R.id.TB_ProductName)
        TB_ProductPrice = findViewById(R.id.TB_ProductPrice)
        TB_ProductDate = findViewById(R.id.TB_ProductDate)
*/
        listView()
    }

    fun listView() {

        val products = db.collection("products")
        val listItems = arrayListOf<Product>()

        products.get().addOnSuccessListener { documents ->
            for (document in documents) {
                //Log.d("Exito for", "${document.id} => ${document.data}")

                var product1: Product = Product(
                    document.getString("name") as String,
                    document.getDouble("price") as Double,
                    document.getTimestamp("created_at") as com.google.firebase.Timestamp
                )
                listItems.add(product1)

            }
            val adapter = ProductAdapter(this, listItems)
            listProducts.adapter = adapter
        }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
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
                    TB_ProductPrice.text = document.getLong("price").toString()
                    //Pongo ? al final de getTimestamp por si sale vacio
                    TB_ProductDate.text = document.getTimestamp("created_at")?.toDate().toString()
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
