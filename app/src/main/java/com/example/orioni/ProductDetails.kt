package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

class ProductDetails : AppCompatActivity() {
    private lateinit var productName:TextView
    private lateinit var productPrice:TextView
    private lateinit var productDate:TextView
    private lateinit var productOwner:TextView
    private lateinit var delete:Button
    private lateinit var exchange:Button

    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        productName = findViewById(R.id.TB_ProductName)
        productPrice = findViewById(R.id.TB_ProductPrice)
        productDate = findViewById(R.id.TB_ProductDate)
        productOwner = findViewById(R.id.TB_ProductOwner)
        delete = findViewById(R.id.B_Delete)
        exchange = findViewById(R.id.B_Exchange)

        if(intent.getBooleanExtra("own", false)){
            delete.visibility = View.VISIBLE
            exchange.visibility = View.GONE
        }else{
            delete.visibility = View.GONE
            exchange.visibility = View.VISIBLE
        }

        var productId = intent.getStringExtra("productId")
        var userId = intent.getStringExtra("userId")

        var userProduct =  db.collection("User").document(userId)
            .collection("Products").document(productId)

        userProduct.get().addOnSuccessListener { product ->
            var prod = product.toObject(Product::class.java)

            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a")
            val timestamp = prod?.created_at
            val date = timestamp?.toDate()

            productName.text = prod?.name
            productPrice.text = prod?.price.toString()
            productDate.text = sdf.format(date).toString()
            db.collection("User").document(prod?.userId.toString()).get().addOnSuccessListener { document ->
                productOwner.text = document.getString("firstName") + " " + document.getString("lastName")
            }
        }
    }

    fun delete(view:View){
        var productId = intent.getStringExtra("productId")
        var userId = intent.getStringExtra("userId")

        db.collection("User").document(userId)
            .collection("Products").document(productId).update("deleted", true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Producto eliminado",
                        Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MyProducts::class.java))
                }else{
                    Toast.makeText(this, "Error al eliminar el producto",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    fun exchange(view:View){
        Toast.makeText(this, "Aún no disponible xdxd",
            Toast.LENGTH_LONG).show()
    }
}
