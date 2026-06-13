package com.akeyma.ewallet.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akeyma.ewallet.databinding.ActivitySecurityBinding

class SecurityActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecurityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecurityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.menuChangePin.setOnClickListener {
            Toast.makeText(this, "Fitur Ganti PIN segera hadir", Toast.LENGTH_SHORT).show()
        }
        binding.menuChangePassword.setOnClickListener {
            Toast.makeText(this, "Fitur Ganti Password segera hadir", Toast.LENGTH_SHORT).show()
        }
        binding.menuLoginHistory.setOnClickListener {
            Toast.makeText(this, "Riwayat Login: 15 Jan 2025, 09:30 WIB", Toast.LENGTH_LONG).show()
        }
        binding.switchBiometric.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) "Biometrik diaktifkan ✅" else "Biometrik dinonaktifkan"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}