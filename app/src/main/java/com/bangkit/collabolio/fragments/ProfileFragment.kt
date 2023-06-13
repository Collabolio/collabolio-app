package com.bangkit.collabolio.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bangkit.collabolio.CollabolioCallback
import com.bangkit.collabolio.databinding.FragmentProfileBinding
import com.bangkit.collabolio.ui.LoginActivity
import com.bangkit.collabolio.ui.MainActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding
    private var callback: CollabolioCallback? = null

    fun setCallback(callback: CollabolioCallback) {
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding.btnSignout.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                auth.signOut()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        })


        binding.progressLayout.setOnTouchListener { view, event -> true }
        binding.progressLayout.visibility = View.VISIBLE
        val userProfileRef = db.collection("users").document(currentUser!!.uid)
        userProfileRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val profile = document.data!!["profile"] as MutableMap<*, *>
                val displayName = profile["displayName"]
                val age = profile["age"]
                val email = document.data!!["email"]
                val photoURL = profile["photoURL"].toString()

                binding.edtName.setText(displayName.toString(), TextView.BufferType.EDITABLE)
                binding.edtAge.setText(age.toString(), TextView.BufferType.EDITABLE)
                binding.edtEmail.setText(email.toString(), TextView.BufferType.EDITABLE)
                populateImage(photoURL)
                binding.progressLayout.visibility = View.GONE
            }
        }
    }
    fun populateImage(uri: String){
        Glide.with(this)
            .load(uri)
            .into(binding.imageViewPhoto)
    }
}