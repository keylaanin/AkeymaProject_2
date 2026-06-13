package com.akeyma.ewallet.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akeyma.ewallet.databinding.ActivityCustomerServiceBinding

class CustomerServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnChat.setOnClickListener {
            Toast.makeText(this, "Live Chat sedang diinisiasi...", Toast.LENGTH_SHORT).show()
        }

        binding.btnWhatsApp.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://wa.me/6281234567890?text=Halo%20CS%20AKEYMA")
                }
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "WhatsApp tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:cs@akeyma.id")
                putExtra(Intent.EXTRA_SUBJECT, "Bantuan AKEYMA E-Wallet")
            }
            startActivity(Intent.createChooser(intent, "Kirim Email"))
        }
    }
}