package com.example.orioni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TB_ProductName = findViewById(R.id.TB_ProductName)
        TB_ProductPrice = findViewById(R.id.TB_ProductPrice)
        TB_ProductDate = findViewById(R.id.TB_ProductDate)

        //Acceso al documento
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("Existe", "DocumentSnapshot data: ${document.data}")

                    //Asignamos los valores a los textbox
                    TB_ProductName.text = document.getString("name")
                    TB_ProductPrice.text = document.getString("price")
                    TB_ProductDate.text = document.getString("created_at")
                } else {
                    Log.d("No Existe", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Error BD", "get failed with ", exception)
            }
    }
}
