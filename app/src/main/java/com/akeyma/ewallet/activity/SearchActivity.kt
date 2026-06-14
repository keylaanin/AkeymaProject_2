package com.akeyma.ewallet.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.akeyma.ewallet.adapter.TransactionAdapter
import com.akeyma.ewallet.databinding.ActivitySearchBinding
import com.akeyma.ewallet.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val allTransactions = mutableListOf<Transaction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvSearchResults.layoutManager = LinearLayoutManager(this)

        binding.btnBack.setOnClickListener { finish() }

        fetchTransactionsFromFirebase()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterTransactions()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.chipRecent.setOnClickListener {
            binding.etSearch.text?.clear()
            showResults(allTransactions)
        }
        binding.chipFavorite.setOnClickListener {
            binding.etSearch.text?.clear()
            showResults(allTransactions.filter { it.amount >= 0 })
        }
        binding.chipTopUp2.setOnClickListener {
            binding.etSearch.text?.clear()
            showResults(allTransactions.filter { it.type == "credit" })
        }
        binding.chipTransfer.setOnClickListener {
            binding.etSearch.text?.clear()
            showResults(allTransactions.filter { it.type == "debit" })
        }
    }

    private fun fetchTransactionsFromFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("topup_transactions")
            .whereEqualTo("user_id", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshots ->
                allTransactions.clear()
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

                    allTransactions.add(
                        Transaction(id, transactionTitle, date, amount, transactionType, transactionEmoji)
                    )
                }
                showResults(allTransactions)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal memuat data: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun filterTransactions() {
        val keyword = binding.etSearch.text.toString()
            .trim()
            .lowercase()

        if (keyword.isEmpty()) {
            showResults(allTransactions)
            return
        }

        val filtered = allTransactions.filter { transaction ->
            transaction.title.lowercase().contains(keyword) ||
                    transaction.date.lowercase().contains(keyword) ||
                    transaction.amount.toString().contains(keyword)
        }

        showResults(filtered)
    }

    private fun showResults(list: List<Transaction>) {
        binding.rvSearchResults.adapter = TransactionAdapter(list)
    }
}