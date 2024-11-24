package com.example.daftarbelanjaapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
//import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ShoppingAdapter
    private val items = mutableListOf<ShoppingItem>()
    private var totalItems = 0
    private var totalPrice = 0
    private var purchasedItems = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ShoppingAdapter(items) { calculateSummary() }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.addItemButton).setOnClickListener {
            addItemDialog()
        }
    }

    private fun addItemDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Tambah Barang")
            .setPositiveButton("Tambah") { _, _ ->
                val name = dialogView.findViewById<EditText>(R.id.nameInput).text.toString()
                val quantity = dialogView.findViewById<EditText>(R.id.quantityInput).text.toString().toInt()
                val price = dialogView.findViewById<EditText>(R.id.priceInput).text.toString().toInt()

                val newItem = ShoppingItem(
                    id = items.size + 1,
                    name = name,
                    quantity = quantity,
                    price = price
                )
                items.add(newItem)
                adapter.notifyItemInserted(items.size - 1)
                calculateSummary()
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()
    }

    private fun calculateSummary() {
        totalItems = items.sumOf { it.quantity }
        totalPrice = items.filter { it.isPurchased }.sumOf { it.quantity * it.price }
        purchasedItems = items.count { it.isPurchased }

        findViewById<TextView>(R.id.summaryText).text =
            "Total Barang: $totalItems | Total Harga: Rp $totalPrice | Dibeli: $purchasedItems"
    }
}