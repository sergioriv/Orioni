package com.example.orioni

import com.google.firebase.Timestamp

//Tuve que poner valores por defecto para que los mostrara en el document.toObject :)
class Product (var name:String = "nombre", var price: Double=0.0, var created_at: Timestamp= Timestamp.now(),
               var userId:String = "userId")