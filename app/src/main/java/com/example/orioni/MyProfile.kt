package com.example.orioni

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MyProfile : AppCompatActivity() {
    private lateinit var eTxtUserFirstName: EditText
    private lateinit var eTxtUserLastName: EditText
    private lateinit var txtUserFirstName: TextView
    private lateinit var txtUserLastName: TextView
    private lateinit var edit:Button
    private lateinit var save:Button
    private lateinit var cancel:Button
    private lateinit var changePassword:Button

    private lateinit var listProducts: ListView

    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        eTxtUserFirstName = findViewById(R.id.ET_UserFirstName)
        eTxtUserLastName = findViewById(R.id.ET_UserLastName)
        txtUserFirstName = findViewById(R.id.TB_UserFirstName)
        txtUserLastName = findViewById(R.id.TB_UserLastName)
        edit = findViewById(R.id.B_edit)
        save = findViewById(R.id.B_Save)
        cancel = findViewById(R.id.B_Cancel)
        changePassword = findViewById(R.id.B_ChangePassword)

        db.collection("User").document(user?.uid.toString()).get().addOnSuccessListener { document ->
            eTxtUserFirstName.setText(document.getString("firstName"))
            eTxtUserLastName.setText(document.getString("lastName"))
            txtUserFirstName.text = document.getString("firstName")
            txtUserLastName.text = document.getString("lastName")
        }

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

    fun verProductos(view: View){
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun editUser(view:View){
        eTxtUserFirstName.isEnabled = true
        eTxtUserLastName.isEnabled = true

        txtUserFirstName.visibility = View.GONE
        txtUserLastName.visibility = View.GONE
        edit.visibility = View.GONE
        changePassword.visibility = View.GONE
        eTxtUserFirstName.visibility = View.VISIBLE
        eTxtUserLastName.visibility = View.VISIBLE
        save.visibility = View.VISIBLE
        cancel.visibility = View.VISIBLE
    }

    fun saveUser(view:View){
        Toast.makeText(this, "Procesando...", Toast.LENGTH_SHORT).show()

        db.collection("User").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .update("firstName", eTxtUserFirstName.text.toString(),
            "lastName", eTxtUserLastName.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    txtUserFirstName.text = eTxtUserFirstName.text
                    txtUserLastName.text = eTxtUserLastName.text

                    eTxtUserFirstName.isEnabled = false
                    eTxtUserLastName.isEnabled = false

                    txtUserFirstName.visibility = View.VISIBLE
                    txtUserLastName.visibility = View.VISIBLE
                    edit.visibility = View.VISIBLE
                    changePassword.visibility = View.VISIBLE
                    eTxtUserFirstName.visibility = View.GONE
                    eTxtUserLastName.visibility = View.GONE
                    save.visibility = View.GONE
                    cancel.visibility = View.GONE

                    Toast.makeText(this, "Datos actualizados",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    fun cancelEdit(view:View){
        eTxtUserFirstName.setText(txtUserFirstName.text)
        eTxtUserLastName.setText(txtUserLastName.text)

        eTxtUserFirstName.isEnabled = false
        eTxtUserLastName.isEnabled = false

        txtUserFirstName.visibility = View.VISIBLE
        txtUserLastName.visibility = View.VISIBLE
        edit.visibility = View.VISIBLE
        changePassword.visibility = View.VISIBLE
        eTxtUserFirstName.visibility = View.GONE
        eTxtUserLastName.visibility = View.GONE
        save.visibility = View.GONE
        cancel.visibility = View.GONE
    }

    fun changePassword(view:View){
        startActivity(Intent(this, ChangePassword::class.java))
    }
}
