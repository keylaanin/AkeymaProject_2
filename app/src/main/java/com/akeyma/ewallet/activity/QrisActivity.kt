package com.akeyma.ewallet.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akeyma.ewallet.databinding.ActivityQrisBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

class QrisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        generateQRCode("AKEYMA-EWALLET-881234567890")

        binding.btnGenerateQr.setOnClickListener {
            val amount = binding.etQrisAmount.text.toString()
            val qrData = if (amount.isNotEmpty()) {
                "AKEYMA-EWALLET-881234567890-AMOUNT-$amount"
            } else {
                "AKEYMA-EWALLET-881234567890"
            }
            generateQRCode(qrData)
            Toast.makeText(this, "QR Code berhasil dibuat!", Toast.LENGTH_SHORT).show()
        }

        binding.btnScanQr.setOnClickListener {
            Toast.makeText(this, "Fitur scan QR akan segera hadir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateQRCode(content: String) {
        try {
            val writer = MultiFormatWriter()
            val matrix = writer.encode(content, BarcodeFormat.QR_CODE, 600, 600)
            val encoder = BarcodeEncoder()
            val bitmap: Bitmap = encoder.createBitmap(matrix)
            binding.ivQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal membuat QR Code", Toast.LENGTH_SHORT).show()
        }
    }
}