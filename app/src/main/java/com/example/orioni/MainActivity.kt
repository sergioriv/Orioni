package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.multidex.MultiDex
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var txtProductName:TextView
    private lateinit var txtProductPrice:TextView

    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        MultiDex.install(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtProductName = findViewById(R.id.TB_ProductName)
        txtProductPrice = findViewById(R.id.TB_ProductPrice)

        this.listView()
    }

    private fun listView() {

        val products = db.collection("products")
        val listItems = arrayListOf<Product>()

        products.get().addOnSuccessListener { documents ->
            for (document in documents) {
                //Log.d("Exito for", "${document.id} => ${document.data}")

                val product1 = Product(
                    document.getString("name") as String,
                    document.getDouble("price") as Double,
                    document.getTimestamp("created_at") as com.google.firebase.Timestamp
                )
                listItems.add(product1)

            }
            val adapter = ProductAdapter(this, listItems)
            listProducts.adapter = adapter

            listProducts.setOnItemClickListener { _, _, _, _ ->

                //Me daba error, cambié unas cosas
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("product", listItems)
                startActivity(intent)
            }
        }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
    }

    fun crearProductos() {
        //El cambio de ventanas también jode
        startActivity(Intent(this, CreateProduct::class.java))
    }
}
