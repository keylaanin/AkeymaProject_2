package com.akeyma.ewallet.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.akeyma.ewallet.adapter.PaymentMethodAdapter
import com.akeyma.ewallet.databinding.ActivityPaymentMethodBinding
import com.akeyma.ewallet.model.PaymentMethod

class PaymentMethodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentMethodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val methods = listOf(
            PaymentMethod("BCA Virtual Account", "8812345678", "🏦", true),
            PaymentMethod("BNI Virtual Account", "8812345679", "🏦", false),
            PaymentMethod("Mandiri Virtual Account", "8812345680", "🏦", false),
            PaymentMethod("QRIS", "Scan to Pay", "📱", true),
            PaymentMethod("Kartu Kredit Visa", "**** 4242", "💳", true),
            PaymentMethod("GoPay", "081234567890", "💚", false),
            PaymentMethod("OVO", "081234567890", "💜", false),
            PaymentMethod("Dana", "081234567890", "🩵", false),
        )

        binding.rvPayments.layoutManager = LinearLayoutManager(this)
        binding.rvPayments.adapter = PaymentMethodAdapter(methods)
    }
}