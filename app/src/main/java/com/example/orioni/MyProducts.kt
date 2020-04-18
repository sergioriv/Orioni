package com.example.orioni

import android.content.Intent
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
                if (product.getBoolean("deleted") == null){
                    var prod = product.toObject(Product::class.java)
                    prod.id = product.id //Le añado el id para hacer consulta de si aún está disponible
                    listItems.add(prod)
                }
            }

            listItems.sortBy { product ->
                product.created_at
            }

            var adapter = ProductAdapter(this, listItems)
            listProducts.adapter = adapter

            listProducts.setOnItemClickListener{ parent, view, position, id ->

                Log.w("mine", "id: ${listItems.get(position).id}")
                Log.w("mine", "user: ${listItems.get(position).userId}")

                startActivity(Intent(this, ProductDetails::class.java)
                    .putExtra("productId", listItems.get(position).id)
                    .putExtra("userId", listItems.get(position).userId)
                    .putExtra("own", true))
            }
        }.addOnFailureListener { exception ->
            Log.w("Error", "e: ", exception)
        }
    }
}
