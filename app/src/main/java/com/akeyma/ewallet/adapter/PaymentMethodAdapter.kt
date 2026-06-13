package com.akeyma.ewallet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.akeyma.ewallet.databinding.ItemTransactionBinding
import com.akeyma.ewallet.model.PaymentMethod

class PaymentMethodAdapter(private val items: List<PaymentMethod>) :
    RecyclerView.Adapter<PaymentMethodAdapter.VH>() {

    inner class VH(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val b = holder.binding
        b.tvEmoji.text = item.emoji
        b.tvTitle.text = item.name
        b.tvDate.text = item.detail
        b.tvAmount.text = if (item.isActive) "✅ Aktif" else "Tambah"
        holder.itemView.setOnClickListener {
            Toast.makeText(it.context, "${item.name} dipilih", Toast.LENGTH_SHORT).show()
        }
    }
}