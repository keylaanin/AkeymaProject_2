package com.akeyma.ewallet.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.akeyma.ewallet.activity.CustomerServiceActivity
import com.akeyma.ewallet.activity.SecurityActivity
import com.akeyma.ewallet.auth.LoginActivity
import com.akeyma.ewallet.databinding.FragmentProfileBinding
import com.akeyma.ewallet.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = SessionManager(requireContext())

        binding.tvProfileName.text = session.getUserName()
        binding.tvProfileEmail.text = session.getUserEmail()

        binding.menuCS.setOnClickListener {
            startActivity(Intent(requireContext(), CustomerServiceActivity::class.java))
        }
        binding.menuSecurity.setOnClickListener {
            startActivity(Intent(requireContext(), SecurityActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah kamu yakin ingin keluar?")
                .setPositiveButton("Keluar") { _, _ ->
                    session.clearSession()

                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finishAffinity()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}