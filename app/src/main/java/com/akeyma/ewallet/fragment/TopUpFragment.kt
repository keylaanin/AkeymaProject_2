package com.akeyma.ewallet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akeyma.ewallet.databinding.FragmentTopUpBinding
import com.akeyma.ewallet.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TopUpFragment : Fragment() {

    private var _binding: FragmentTopUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTopUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = SessionManager(requireContext())

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val chipMap = mapOf(
            binding.chip10k to "10000",
            binding.chip20k to "20000",
            binding.chip50k to "50000",
            binding.chip100k to "100000",
            binding.chip200k to "200000",
            binding.chip500k to "500000"
        )
        chipMap.forEach { (chip, value) ->
            chip.setOnClickListener { binding.etAmount.setText(value) }
        }

        binding.btnTopUp.setOnClickListener {
            val amountStr = binding.etAmount.text.toString()
            if (amountStr.isEmpty()) {
                binding.tilAmount.error = "Masukkan nominal top up"
                return@setOnClickListener
            }
            val amount = amountStr.toLongOrNull() ?: 0L
            if (amount < 10000) {
                binding.tilAmount.error = "Minimal top up Rp 10.000"
                return@setOnClickListener
            }

            val userId = firebaseAuth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(requireContext(), "Sesi habis, silakan login kembali", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnTopUp.isEnabled = false
            val currentDateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            val transactionRef = firestore.collection("topup_transactions").document()

            val transactionData = hashMapOf(
                "transaction_id" to transactionRef.id,
                "user_id" to userId,
                "amount" to amount,
                "payment_method" to "Instant Top Up",
                "status" to "SUCCESS",
                "date_string" to currentDateStr,
                "timestamp" to FieldValue.serverTimestamp()
            )

            val batch = firestore.batch()
            val userRef = firestore.collection("users").document(userId)
            val updateData = mapOf("balance" to FieldValue.increment(amount))
            batch.set(userRef, updateData, SetOptions.merge())
            batch.set(transactionRef, transactionData)

            batch.commit()
                .addOnSuccessListener {
                    val currentBalance = session.getBalance().toLongOrNull() ?: 0L
                    val newBalance = currentBalance + amount
                    session.updateBalance(newBalance.toString())

                    Toast.makeText(requireContext(), "Top Up Rp $amount berhasil! 🎉", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                .addOnFailureListener { e ->
                    binding.btnTopUp.isEnabled = true
                    Toast.makeText(requireContext(), "Gagal melakukan top up: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}