package com.akeyma.ewallet.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akeyma.ewallet.databinding.ItemTransactionBinding
import com.akeyma.ewallet.model.Transaction
import java.text.NumberFormat
import java.util.Locale

class TransactionAdapter(private val items: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.VH>() {

    inner class VH(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val b = holder.binding
        b.tvEmoji.text = item.emoji
        b.tvTitle.text = item.title
        b.tvDate.text = item.date
        val fmt = NumberFormat.getInstance(Locale("id", "ID"))
        if (item.amount >= 0) {
            b.tvAmount.text = "+ Rp ${fmt.format(item.amount)}"
            b.tvAmount.setTextColor(Color.parseColor("#2E7D32"))
        } else {
            b.tvAmount.text = "- Rp ${fmt.format(-item.amount)}"
            b.tvAmount.setTextColor(Color.parseColor("#F44336"))
        }
    }
}