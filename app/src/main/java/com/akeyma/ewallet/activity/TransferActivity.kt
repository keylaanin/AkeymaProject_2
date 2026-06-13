package com.akeyma.ewallet.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akeyma.ewallet.databinding.ActivityTransferBinding
import com.akeyma.ewallet.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransferBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        sessionManager = SessionManager(this)

        val transferType = intent.getStringExtra("EXTRA_TRANSFER_TYPE") ?: "Bank"

        binding.tvTitleTransfer.text = "Transfer $transferType"

        binding.btnBack.setOnClickListener { finish() }

        binding.btnTransfer.setOnClickListener {
            val targetNumber = binding.etDestination.text.toString().trim()
            val amountStr = binding.etAmount.text.toString().trim()

            if (targetNumber.isEmpty()) {
                binding.etDestination.error = "Nomor atau Rekening tujuan wajib diisi"
                binding.etDestination.requestFocus()
                return@setOnClickListener
            }
            if (amountStr.isEmpty()) {
                binding.etAmount.error = "Nominal transfer wajib diisi"
                binding.etAmount.requestFocus()
                return@setOnClickListener
            }

            val amount = amountStr.toLongOrNull() ?: 0L
            if (amount < 10000) {
                binding.etAmount.error = "Minimal transfer Rp 10.000"
                binding.etAmount.requestFocus()
                return@setOnClickListener
            }

            val currentBalance = sessionManager.getBalance().toLongOrNull() ?: 0L
            if (amount > currentBalance) {
                binding.etAmount.error = "Saldo Anda tidak mencukupi untuk transaksi ini"
                binding.etAmount.requestFocus()
                Toast.makeText(this, "Saldo tidak cukup!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = firebaseAuth.currentUser?.uid ?: return@setOnClickListener

            binding.btnTransfer.isEnabled = false

            eksekusiTransferFirebase(userId, transferType, targetNumber, amount)
        }
    }

    private fun eksekusiTransferFirebase(userId: String, type: String, target: String, amount: Long) {
        val transferAmount = amount * -1
        val transactionRef = firestore.collection("topup_transactions").document()
        val currentDateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val paymentMethodString = "$type ke $target"

        val transactionData = hashMapOf(
            "transaction_id" to transactionRef.id,
            "user_id" to userId,
            "amount" to transferAmount,
            "payment_method" to paymentMethodString,
            "status" to "SUCCESS",
            "date_string" to currentDateStr,
            "timestamp" to FieldValue.serverTimestamp()
        )

        val batch = firestore.batch()
        val userRef = firestore.collection("users").document(userId)

        batch.update(userRef, "balance", FieldValue.increment(transferAmount))
        batch.set(transactionRef, transactionData)

        batch.commit()
            .addOnSuccessListener {
                Toast.makeText(this, "Transfer ke $paymentMethodString Berhasil!", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener { e ->
                binding.btnTransfer.isEnabled = true
                Toast.makeText(this, "Gagal melakukan transfer: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }
}