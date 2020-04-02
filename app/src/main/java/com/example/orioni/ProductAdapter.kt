package com.example.orioni

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.item_product.view.*
import java.text.SimpleDateFormat
import java.util.*

class ProductAdapter(private val mContext: Context, private val listProducts: List<Product>) : ArrayAdapter<Product>(mContext, 0, listProducts) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout= LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false)

        val product = listProducts[position]

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a")
        val timestamp = product.created_at
        val date = timestamp.toDate()

        layout.name.text = product.name
        layout.price.text = "$${product.price}"
        layout.date.text = sdf.format(date).toString()
        //layout.image.setImageResource(product.image)

        return layout
    }
}