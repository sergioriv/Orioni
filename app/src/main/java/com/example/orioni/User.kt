package com.example.orioni

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class User (var userId:String){
    private val db = FirebaseFirestore.getInstance()
    var userDataRef =  db.collection("User").document(userId)
    var userProductsRef =  db.collection("User").document(userId)
        .collection("Products")

    //No tengo idea porqué no envía desde la clase, o porqué llegan vacios, voy a pasar el codigo a
    //la actividad
    fun getProduct():ArrayList<Product> {
        var listItems = arrayListOf<Product>()

        userProductsRef.get().addOnSuccessListener { documents ->
            for(document in documents){
                listItems.add(document.toObject(Product::class.java))
                Log.w("ProductName", "Name: ${document.toObject(Product::class.java).name}")
            }
            Log.w("id", "Name: ${userId}")
        }.addOnFailureListener { exception ->
            Log.w("Error", "e: ", exception)
        }

        Log.w("List", "List User: ${listItems}")
        return listItems
    }

    fun createProduct(product:Product):Boolean{
        //var success = userProductsRef.add(product).isSuccessful

        /*userProductsRef.add(product).addOnCompleteListener { task ->
            success = task.isComplete
            Log.w("Create", "succes: ${task.isComplete}")
        }*/

        return userProductsRef.add(product).isSuccessful
    }
}