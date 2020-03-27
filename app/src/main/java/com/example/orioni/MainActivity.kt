package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.multidex.MultiDex
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firestore.v1.ArrayValue
import java.sql.Date
import java.sql.Time

class MainActivity : AppCompatActivity() {
    private lateinit var txtProductName:TextView
    private lateinit var txtProductPrice:TextView
    private lateinit var txtProductDate:TextView
    private lateinit var listView: ListView

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
        txtProductName = findViewById(R.id.txtProductName)
        txtProductPrice = findViewById(R.id.txtProductPrice)
        txtProductDate = findViewById(R.id.txtProductDate)
*/
        listProducts();
    }

    fun listProducts(){

        /*
            List Products
         */
        val products = db.collection("products")
        listView = findViewById(R.id.listProducts)
        val listItems = arrayListOf<String>()

        products.get().addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d("Exito for", "${document.id} => ${document.data}")

                listItems.add(document.get("name") as String)
            }
            val arrayadapter=ArrayAdapter(this@MainActivity,android.R.layout.simple_expandable_list_item_1,listItems)
            listView.adapter=arrayadapter

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
                    txtProductName.text = document.getString("name")
                    txtProductPrice.text = document.getLong("price").toString()
                    //Pongo ? al final de getTimestamp por si sale vacio
                    txtProductDate.text = document.getTimestamp("created_at")?.toDate().toString()
                } else {
                    Log.d("SinExito", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ErrorBD", "get failed with ", exception)
            }
    }

    fun crearProductos(view: View){
        startActivity(Intent(this, CreateProduct::class.java))
    }

    fun register(view: View){
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun login(view: View){
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
