package com.akeyma.ewallet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.akeyma.ewallet.adapter.TransactionAdapter
import com.akeyma.ewallet.databinding.FragmentTransactionBinding
import com.akeyma.ewallet.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    private val transactionList = mutableListOf<Transaction>()
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())

        fetchTransactionsFromFirebase()

        binding.chipAll.setOnClickListener { showTransactions(transactionList) }
        binding.chipIn.setOnClickListener {
            showTransactions(transactionList.filter { it.amount > 0 })
        }
        binding.chipOut.setOnClickListener {
            showTransactions(transactionList.filter { it.amount < 0 })
        }
        binding.chipTopUp.setOnClickListener {
            showTransactions(transactionList.filter { it.type == "credit" })
        }
    }

    private fun fetchTransactionsFromFirebase() {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) return

        firestore.collection("topup_transactions")
            .whereEqualTo("user_id", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "Gagal memuat riwayat: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    transactionList.clear()
                    for (document in snapshots) {
                        val id = document.getString("transaction_id") ?: ""
                        val amount = document.getLong("amount") ?: 0L
                        val paymentMethod = document.getString("payment_method") ?: ""
                        val date = document.getString("date_string") ?: "Hari ini"
                        val transactionTitle: String
                        val transactionType: String
                        val transactionEmoji: String

                        if (amount >= 0) {
                            transactionTitle = "Top Up via $paymentMethod"
                            transactionType = "credit"
                            transactionEmoji = "✅"
                        } else {
                            transactionTitle = "Transfer $paymentMethod"
                            transactionType = "debit"
                            transactionEmoji = "💸"
                        }

                        val transaction = Transaction(
                            id = id,
                            title = transactionTitle,
                            date = date,
                            amount = amount,
                            type = transactionType,
                            emoji = transactionEmoji
                        )
                        transactionList.add(transaction)
                    }
                    showTransactions(transactionList)
                }
            }
    }

    private fun showTransactions(list: List<Transaction>) {
        binding.rvTransactions.adapter = TransactionAdapter(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}