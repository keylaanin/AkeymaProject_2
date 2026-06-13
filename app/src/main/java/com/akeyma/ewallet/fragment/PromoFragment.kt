package com.akeyma.ewallet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.akeyma.ewallet.adapter.PromoAdapter
import com.akeyma.ewallet.databinding.FragmentPromoBinding
import com.akeyma.ewallet.model.Promo

class PromoFragment : Fragment() {

    private var _binding: FragmentPromoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPromoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val promos = listOf(
            Promo("Cashback 20%", "Cashback untuk transaksi pertama", "Berlaku s/d 31 Jan 2025", "20%"),
            Promo("Gratis Biaya Admin", "Transfer tanpa biaya admin", "Berlaku s/d 28 Feb 2025", "FREE"),
            Promo("Bonus Top Up", "Bonus 10% setiap top up min Rp 100.000", "Berlaku s/d 15 Mar 2025", "+10%"),
            Promo("Diskon QRIS", "Diskon 5% bayar via QRIS", "Berlaku s/d 30 Mar 2025", "5% OFF"),
        )

        binding.rvPromo.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPromo.adapter = PromoAdapter(promos)

        binding.btnRedeem.setOnClickListener {
            val code = binding.etVoucher.text.toString().uppercase()
            when (code) {
                "AKEYMA10" -> Toast.makeText(requireContext(), "✅ Voucher berhasil! Diskon 10%", Toast.LENGTH_SHORT).show()
                "GREENWALLET" -> Toast.makeText(requireContext(), "✅ Voucher berhasil! Cashback Rp 20.000", Toast.LENGTH_SHORT).show()
                "" -> binding.tilVoucher.error = "Masukkan kode voucher"
                else -> Toast.makeText(requireContext(), "❌ Kode voucher tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}