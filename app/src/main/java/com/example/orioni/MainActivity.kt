package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.multidex.MultiDex
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var listProducts:ListView
    private lateinit var login:Button
    private lateinit var signOut:Button
    private lateinit var myProducts:Button

    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        MultiDex.install(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listProducts = findViewById(R.id.listProducts)
        login = findViewById(R.id.login)
        signOut = findViewById(R.id.signOut)
        myProducts = findViewById(R.id.myProducts)

        if(FirebaseAuth.getInstance().currentUser != null){
            login.visibility = View.GONE
            signOut.visibility = View.VISIBLE
            myProducts.visibility = View.VISIBLE
        }else{
            login.visibility = View.VISIBLE
            signOut.visibility = View.GONE
            myProducts.visibility = View.GONE
        }

        this.listView()
    }

    private fun listView() {
        //Solo accedo a la collección de usuarios
        var userCollection = db.collection("User")

        val listItems = arrayListOf<Product>()

        userCollection.get().addOnSuccessListener { documents ->
            for (document in documents){
                var userProductsRef =  db.collection("User").document(document.id)
                    .collection("Products")

                userProductsRef.get().addOnSuccessListener { products ->
                    for(product in products){
                        if (product.getBoolean("deleted") == null){
                            var prod = product.toObject(Product::class.java)
                            prod.id = product.id //Le añado el id para hacer consulta de si aún está disponible
                            listItems.add(prod)
                            Log.w("Product", "name: ${prod.name}")
                        }
                    }

                    listItems.sortBy { product ->
                        product.created_at
                    }

                    var adapter = ProductAdapter(this, listItems)
                    listProducts.adapter = adapter

                    listProducts.setOnItemClickListener{ parent, view, position, id ->
                        startActivity(Intent(this, ProductDetails::class.java)
                            .putExtra("productId", listItems.get(position).id)
                            .putExtra("userId", listItems.get(position).userId)
                            .putExtra("own", false))
                    }
                }.addOnFailureListener { exception ->
                    Log.w("Error", "e: ", exception)
                }
            }
        }.addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
        }
    }

    fun createProducts(view:View) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null){
            startActivity(Intent(this, CreateProduct::class.java))
        }else{
            Toast.makeText(this, "Debe iniciar sesión",
                Toast.LENGTH_LONG).show()
        }
    }

    fun loginView(view:View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun signOut(view:View){
        FirebaseAuth.getInstance().signOut()

        Toast.makeText(this, "Sesión cerrada",
            Toast.LENGTH_LONG).show()

        startActivity(Intent(this, MainActivity::class.java))
    }

    fun myProducts(view:View){
        startActivity(Intent(this, MyProducts::class.java))
    }
}
