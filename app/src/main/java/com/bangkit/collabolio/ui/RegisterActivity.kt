package com.bangkit.collabolio.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.bangkit.collabolio.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        actionClick()
    }

    private fun actionClick() {
        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (password == confirmPassword) {
                registerUser(email, password)
            } else {
                // Handle password mismatch error
            }
        }
        binding.toLogin.setOnClickListener {
            goToLoginActivity()
        }
    }
    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    AlertDialog.Builder(this).apply {
                        setTitle("Registrasi Berhasil")
                        setMessage("Kembali ke halaman Sign In untuk Masuk menggunakan akun yang sudah dibuat")
                        setPositiveButton("Ok") { _, _ ->
                            goToLoginActivity()
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                        // Handle login error here
                }
            }
    }
    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}