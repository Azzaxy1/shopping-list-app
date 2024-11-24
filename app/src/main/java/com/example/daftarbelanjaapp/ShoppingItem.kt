package com.example.daftarbelanjaapp

data class ShoppingItem(
    val id: Int, // ID Barang
    var name: String, // Nama Barang
    var quantity: Int, // Jumlah Barang
    var price: Int, // Harga Barang per unit
    var isPurchased: Boolean = false // Status Pembelian
)
