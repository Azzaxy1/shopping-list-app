package com.example.daftarbelanjaapp

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShoppingAdapter(
    private val items: MutableList<ShoppingItem>,
    private val onStatusChange: () -> Unit // Callback untuk update total barang & harga
) : RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>() {

    // ViewHolder untuk item
    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.itemName)
        private val details = itemView.findViewById<TextView>(R.id.itemDetails)
        private val toggleButton = itemView.findViewById<Button>(R.id.toggleStatusButton)
        private val editButton = itemView.findViewById<Button>(R.id.editButton)
        private val deleteButton = itemView.findViewById<Button>(R.id.deleteButton)

        // Bind data ke ViewHolder
        fun bind(item: ShoppingItem) {
            name.text = item.name
            details.text = "Jumlah: ${item.quantity} | Harga: Rp ${item.price * item.quantity}"
            toggleButton.text = if (item.isPurchased) "Sudah Dibeli" else "Belum Dibeli"

            // Toggle status pembelian
            toggleButton.setOnClickListener {
                item.isPurchased = !item.isPurchased
                onStatusChange() // Update total barang & harga
                notifyItemChanged(adapterPosition)
            }

            // Edit item
            editButton.setOnClickListener {
                showEditDialog(item)
            }

            // Delete item
            deleteButton.setOnClickListener {
                items.removeAt(adapterPosition) // Hapus item dari list
                notifyItemRemoved(adapterPosition) // Update RecyclerView
                notifyItemRangeChanged(adapterPosition, items.size)
                onStatusChange() // Update total barang & harga
            }
        }

        // Dialog untuk mengedit item
        private fun showEditDialog(item: ShoppingItem) {
            val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_edit_item, null)
            val editName = dialogView.findViewById<EditText>(R.id.editItemName)
            val editQuantity = dialogView.findViewById<EditText>(R.id.editItemQuantity)
            val editPrice = dialogView.findViewById<EditText>(R.id.editItemPrice)

            // Isi data item ke dialog
            editName.setText(item.name)
            editQuantity.setText(item.quantity.toString())
            editPrice.setText(item.price.toString())

            // Buat dialog edit
            AlertDialog.Builder(itemView.context)
                .setTitle("Edit Barang")
                .setView(dialogView)
                .setPositiveButton("Simpan") { _, _ ->
                    // Update item dengan data baru
                    item.name = editName.text.toString()
                    item.quantity = editQuantity.text.toString().toInt()
                    item.price = editPrice.text.toString().toInt()
                    notifyItemChanged(adapterPosition) // Update RecyclerView
                    onStatusChange() // Update total barang & harga
                }
                .setNegativeButton("Batal", null)
                .create()
                .show()
        }
    }

    // Fungsi untuk membuat ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ShoppingViewHolder(view)
    }

    // Bind data ke ViewHolder
    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    // Total item dalam RecyclerView
    override fun getItemCount(): Int = items.size
}
