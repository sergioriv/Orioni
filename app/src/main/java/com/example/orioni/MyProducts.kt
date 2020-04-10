package com.example.orioni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyProducts : AppCompatActivity() {
    private lateinit var listProducts: ListView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)

        listProducts = findViewById(R.id.listProducts)

        this.listView()
    }

    private fun listView() {
        val listItems = arrayListOf<Product>()

        var userProductsRef =  db.collection("User")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .collection("Products")

        userProductsRef.get().addOnSuccessListener { products ->
            for(product in products){
                listItems.add(product.toObject(Product::class.java))
            }

            listItems.sortBy { product ->
                product.created_at
            }

            var adapter = ProductAdapter(this, listItems)
            listProducts.adapter = adapter
        }.addOnFailureListener { exception ->
            Log.w("Error", "e: ", exception)
        }
    }
}
