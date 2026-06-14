package com.akeyma.ewallet.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akeyma.ewallet.R
import com.akeyma.ewallet.activity.QrisActivity
import com.akeyma.ewallet.activity.SearchActivity
import com.akeyma.ewallet.adapter.TransactionAdapter
import com.akeyma.ewallet.databinding.FragmentHomeBinding
import com.akeyma.ewallet.model.Transaction
import com.akeyma.ewallet.utils.SessionManager
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private var balanceListener: ListenerRegistration? = null
    private var transactionListener: ListenerRegistration? = null
    private var isBalanceVisible = true
    private var currentBalanceText = "Rp 0"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        binding.tvUserName.text = sessionManager.getUserName()

        val initialBalance = sessionManager.getBalance().toLongOrNull() ?: 0L
        updateBalanceUI(initialBalance)

        listenToUserBalance()
        fetchRecentTransactions()

        binding.btnToggleBalance.setOnClickListener {

            isBalanceVisible = !isBalanceVisible

            if (isBalanceVisible) {
                binding.tvBalance.text = currentBalanceText

                binding.btnToggleBalance.setImageResource(
                    android.R.drawable.ic_menu_view
                )

                binding.btnToggleBalance.setColorFilter(
                    resources.getColor(android.R.color.white, null)
                )

            } else {
                binding.tvBalance.text = "Rp ••••••••"

                binding.btnToggleBalance.setImageResource(
                    android.R.drawable.ic_menu_view
                )

                binding.btnToggleBalance.setColorFilter(
                    resources.getColor(R.color.green_light, null)
                )
            }
        }

        binding.tvSeeAllTransaction.setOnClickListener {
            findNavController().navigate(R.id.transactionFragment)
        }

        binding.tvSeeAllPromo.setOnClickListener {
            findNavController().navigate(R.id.promoFragment)
        }

        binding.btnQuickQris.setOnClickListener {
            startActivity(Intent(requireContext(), QrisActivity::class.java))
        }

        binding.btnQuickTopUp.setOnClickListener {
            findNavController().navigate(R.id.topUpFragment)
        }

        binding.btnTransfer.setOnClickListener {
            val transferSheet = TransferBottomSheetFragment()
            transferSheet.show(parentFragmentManager, "TransferBottomSheet")
        }

        binding.btnSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }
    }

    private fun listenToUserBalance() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        balanceListener =
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .addSnapshotListener { snapshot, error ->

                if (_binding == null) return@addSnapshotListener
                if (!isAdded) return@addSnapshotListener
                if (error != null) return@addSnapshotListener

                val currentBalance =
                    snapshot?.getLong("balance") ?: 0L

                updateBalanceUI(currentBalance)
                sessionManager.updateBalance(currentBalance.toString())
            }
    }

    private fun fetchRecentTransactions() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        transactionListener =
            db.collection("topup_transactions")
                .whereEqualTo("user_id", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5)
                .addSnapshotListener { snapshots, error ->

                    if (_binding == null) return@addSnapshotListener
                    if (!isAdded) return@addSnapshotListener
                    if (error != null) return@addSnapshotListener

                if (snapshots != null) {
                    val recentList = mutableListOf<Transaction>()
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

                        recentList.add(Transaction(id, transactionTitle, date, amount, transactionType, transactionEmoji))
                    }

                    binding.rvRecentTransactions.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvRecentTransactions.adapter = TransactionAdapter(recentList)
                }
            }
    }

    private fun updateBalanceUI(balance: Long) {
        val formatted = NumberFormat.getInstance(Locale("id", "ID")).format(balance)

        currentBalanceText = "Rp $formatted"

        if (isBalanceVisible) {
            binding.tvBalance.text = currentBalanceText
        } else {
            binding.tvBalance.text = "Rp ••••••••"
        }
    }

    override fun onDestroyView() {

        balanceListener?.remove()
        transactionListener?.remove()

        _binding = null
        super.onDestroyView()
    }
}