package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.multidex.MultiDex
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var listProducts:ListView
    private lateinit var login:Button
    private lateinit var signOut:Button

    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        MultiDex.install(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listProducts = findViewById(R.id.listProducts)
        login = findViewById(R.id.login)
        signOut = findViewById(R.id.signOut)

        if(FirebaseAuth.getInstance().currentUser != null){
            login.visibility = View.GONE
            signOut.visibility = View.VISIBLE
        }else{
            login.visibility = View.VISIBLE
            signOut.visibility = View.GONE
        }

        this.listView()
    }

    private fun listView() {
        var userCollection = db.collection("User")
        /*val products = db.collection("User").document("qdjDQpntShM7WqFcoLjN3bFoG2S2")
            .collection("Products")*/

        val listItems = arrayListOf<Product>()


        userCollection.get().addOnSuccessListener { documents ->
            for (document in documents){

                var userProductsRef =  db.collection("User").document(document.id)
                    .collection("Products")

                userProductsRef.get().addOnSuccessListener { documents ->
                    for(document in documents){
                        listItems.add(document.toObject(Product::class.java))
                        Log.w("ProductName", "Name: ${document.toObject(Product::class.java).name}")
                    }
                    val adapter = ProductAdapter(this, listItems)
                    listProducts.adapter = adapter
                    Log.w("ProductList", "Name: ${listItems}")
                }.addOnFailureListener { exception ->
                    Log.w("Error", "e: ", exception)
                }

                /*var user = User(document.id)

                listItems.addAll(user.getProduct())

                Log.w("Success", "${document.id} Exito: ${listItems}")

                val adapter = ProductAdapter(this, listItems)
                listProducts.adapter = adapter
                listProducts.setOnItemClickListener { _, _, _, _ ->

                    //Me daba error, cambié unas cosas
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("product", listItems)
                    startActivity(intent)
                }*/
            }
        }.addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
        }


        /*products.get().addOnSuccessListener { documents ->
            for (document in documents) {
                //Log.d("Exito for", "${document.id} => ${document.data}")
                val product1 = Product(
                    document.getString("name") as String,
                    document.getDouble("price") as Double,
                    document.getTimestamp("created_at") as Timestamp
                )
                listItems.add(product1)

                //listItems.add(document.toObject(Product::class.java))

            }
            Log.w("ProductList", "Name: ${listItems}")
            val adapter = ProductAdapter(this, listItems)
            listProducts.adapter = adapter

            /*listProducts.setOnItemClickListener { _, _, _, _ ->

                //Me daba error, cambié unas cosas
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("product", listItems)
                startActivity(intent)
            }*/
        }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }*/
    }

    fun crearProductos(view:View) {
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
}
