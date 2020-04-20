package com.example.orioni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

class ProductDetails : AppCompatActivity() {
    private lateinit var productName:TextView
    private lateinit var eTxtProductName:EditText
    private lateinit var productPrice:TextView
    private lateinit var eTxtProductPrice:EditText
    private lateinit var productDate:TextView
    private lateinit var productOwner:TextView
    private lateinit var mainMenu:Button
    private lateinit var myProducts:Button
    private lateinit var edit:Button
    private lateinit var save:Button
    private lateinit var cancel:Button
    private lateinit var delete:Button
    private lateinit var exchange:Button

    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        productName = findViewById(R.id.TB_ProductName)
        eTxtProductName = findViewById(R.id.ET_ProductName)
        eTxtProductPrice = findViewById(R.id.ET_ProductPrice)
        productPrice = findViewById(R.id.TB_ProductPrice)
        productDate = findViewById(R.id.TB_ProductDate)
        productOwner = findViewById(R.id.TB_ProductOwner)
        mainMenu = findViewById(R.id.B_MainMenu)
        myProducts = findViewById(R.id.B_MyProducts)
        edit = findViewById(R.id.B_edit)
        save = findViewById(R.id.B_Save)
        cancel = findViewById(R.id.B_Cancel)
        delete = findViewById(R.id.B_Delete)
        exchange = findViewById(R.id.B_Exchange)

        if(intent.getBooleanExtra("own", false)){
            edit.visibility = View.VISIBLE
            delete.visibility = View.VISIBLE
            myProducts.visibility = View.VISIBLE
            exchange.visibility = View.GONE
            mainMenu.visibility = View.GONE
        }else{
            edit.visibility = View.GONE
            delete.visibility = View.GONE
            myProducts.visibility = View.GONE
            exchange.visibility = View.VISIBLE
            mainMenu.visibility = View.VISIBLE
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
            eTxtProductName.setText(prod?.name)
            productPrice.text = prod?.price.toString()
            eTxtProductPrice.setText(prod?.price.toString())
            productDate.text = sdf.format(date).toString()
            db.collection("User").document(prod?.userId.toString()).get().addOnSuccessListener { document ->
                productOwner.text = document.getString("firstName") + " " + document.getString("lastName")
            }
        }
    }

    fun mainMenu(view: View){
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun editProduct(view:View){
        eTxtProductName.isEnabled = true
        eTxtProductPrice.isEnabled = true

        productName.visibility = View.GONE
        productPrice.visibility = View.GONE
        edit.visibility = View.GONE
        eTxtProductName.visibility = View.VISIBLE
        eTxtProductPrice.visibility = View.VISIBLE
        save.visibility = View.VISIBLE
        cancel.visibility = View.VISIBLE

    }

    fun saveProduct(view:View){
        Toast.makeText(this, "Procesando...", Toast.LENGTH_SHORT).show()

        var productId = intent.getStringExtra("productId")
        var userId = intent.getStringExtra("userId")

        db.collection("User").document(userId)
            .collection("Products").document(productId)
            .update("name", eTxtProductName.text.toString(),
            "price", eTxtProductPrice.text.toString().toDouble()).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    productName.text = eTxtProductName.text
                    productPrice.text = eTxtProductPrice.text

                    eTxtProductName.isEnabled = false
                    eTxtProductPrice.isEnabled = false

                    productName.visibility = View.VISIBLE
                    productPrice.visibility = View.VISIBLE
                    edit.visibility = View.VISIBLE
                    eTxtProductName.visibility = View.GONE
                    eTxtProductPrice.visibility = View.GONE
                    save.visibility = View.GONE
                    cancel.visibility = View.GONE

                    Toast.makeText(this, "Producto actualizado",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    fun cancelEdit(view:View){
        eTxtProductName.setText(productName.text)
        eTxtProductPrice.setText(productPrice.text)

        eTxtProductName.isEnabled = false
        eTxtProductPrice.isEnabled = false

        productName.visibility = View.VISIBLE
        productPrice.visibility = View.VISIBLE
        edit.visibility = View.VISIBLE
        eTxtProductName.visibility = View.GONE
        eTxtProductPrice.visibility = View.GONE
        save.visibility = View.GONE
        cancel.visibility = View.GONE
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
                    startActivity(Intent(this, MyProfile::class.java))
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

    fun verPerfil(view:View){
        startActivity(Intent(this, MyProfile::class.java))
    }
}