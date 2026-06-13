package com.akeyma.ewallet.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akeyma.ewallet.MainActivity
import com.akeyma.ewallet.databinding.ActivityRegisterBinding
import com.akeyma.ewallet.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val pin = binding.etPin.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            when {
                name.isEmpty() -> binding.tilName.error = "Nama wajib diisi"
                email.isEmpty() -> binding.tilEmail.error = "Email wajib diisi"
                phone.isEmpty() -> binding.tilPhone.error = "Telepon wajib diisi"
                pin.length != 6 -> binding.tilPin.error = "PIN harus 6 digit"
                password.length < 6 -> binding.tilPassword.error = "Password minimal 6 karakter"
                else -> {
                    binding.btnRegister.isEnabled = false
                    registerUserToFirebase(name, email, phone, pin, password)
                }
            }
        }

        binding.tvLogin.setOnClickListener { finish() } // Kembali ke LoginActivity
    }

    private fun registerUserToFirebase(name: String, email: String, phone: String, pin: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = firebaseAuth.currentUser?.uid
                    if (userId != null) {
                        val userMap = hashMapOf(
                            "user_id" to userId,
                            "username" to name,
                            "email" to email,
                            "phone" to phone,
                            "security_pin" to pin,
                            "balance" to 0L
                        )

                        firestore.collection("users").document(userId)
                            .set(userMap)
                            .addOnSuccessListener {
                                sessionManager.saveLoginSession(email, name, phone, "0")

                                Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finishAffinity()
                            }
                            .addOnFailureListener { e ->
                                binding.btnRegister.isEnabled = true
                                Toast.makeText(this, "Gagal menyimpan data: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                            }
                    }
                } else {
                    binding.btnRegister.isEnabled = true
                    val error = task.exception?.localizedMessage ?: "Registrasi gagal."
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
            }
    }
}