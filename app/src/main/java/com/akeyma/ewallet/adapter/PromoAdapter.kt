package com.akeyma.ewallet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.akeyma.ewallet.databinding.ItemPromoBinding
import com.akeyma.ewallet.model.Promo

class PromoAdapter(private val items: List<Promo>) :
    RecyclerView.Adapter<PromoAdapter.VH>() {

    inner class VH(val binding: ItemPromoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemPromoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.tvPromoTitle.text = item.title
        holder.binding.tvPromoDesc.text = item.description
        holder.binding.tvExpiry.text = item.expiry
        holder.binding.tvDiscount.text = item.discount
        holder.binding.btnUse.setOnClickListener {
            Toast.makeText(it.context, "Promo '${item.title}' digunakan!", Toast.LENGTH_SHORT).show()
        }
    }
}