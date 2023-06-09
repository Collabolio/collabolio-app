package com.bangkit.collabolio.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.collabolio.R
import com.bangkit.collabolio.databinding.FragmentProfileBinding
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.auth.User

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userProfileRef = db.collection("users").document(currentUser!!.uid)
        userProfileRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val profile = document.data!!["profile"] as MutableMap<*, *>
                val displayName = profile["displayName"]
                val age = profile["age"]
                val email = document.data!!["email"]

                binding.edtName.text = displayName
                binding.edtAge.text = age
                binding.edtEmail.text = email

            }
            else {
                Log.d("PROF", "ada error nih :")
            }
        }
    }

}