package com.akeyma.ewallet.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akeyma.ewallet.MainActivity
import com.akeyma.ewallet.databinding.ActivityLoginBinding
import com.akeyma.ewallet.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AlertDialog
import android.widget.EditText

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            when {
                email.isEmpty() -> binding.tilEmail.error = "Email wajib diisi"
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    binding.tilEmail.error = "Format email tidak valid"
                password.isEmpty() -> binding.tilPassword.error = "Kata sandi wajib diisi"
                password.length < 6 -> binding.tilPassword.error = "Minimal 6 karakter"
                else -> {
                    binding.tilEmail.error = null
                    binding.tilPassword.error = null

                    loginWithFirebase(email, password)
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {

            val editText = EditText(this)
            editText.hint = "Masukkan email Anda"

            AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setMessage("Masukkan email yang terdaftar")
                .setView(editText)
                .setPositiveButton("Kirim") { _, _ ->

                    val email = editText.text.toString().trim()

                    if (email.isNotEmpty()) {

                        firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->

                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Link reset password telah dikirim ke email Anda",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this,
                                        task.exception?.message ?: "Gagal mengirim email reset",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                    } else {
                        Toast.makeText(
                            this,
                            "Email tidak boleh kosong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun loginWithFirebase(email: String, password: String) {
        binding.btnLogin.isEnabled = false

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.btnLogin.isEnabled = true

                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        sessionManager.saveLoginSession(
                            user.email ?: email,
                            "User Akeyma",
                            "-",
                            "0"
                        )

                        Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Login gagal, silakan coba lagi."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }
}