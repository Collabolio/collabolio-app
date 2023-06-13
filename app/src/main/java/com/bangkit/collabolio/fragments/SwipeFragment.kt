package com.bangkit.collabolio.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.bangkit.collabolio.R
import com.bangkit.collabolio.adapters.CardsAdapter
import com.bangkit.collabolio.databinding.FragmentSwipeBinding
import com.bangkit.collabolio.utilities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lorentzos.flingswipe.SwipeFlingAdapterView

// get current user
val firebaseAuth = FirebaseAuth.getInstance()
val currentUser = firebaseAuth.currentUser

// initialize firestore database and fetch user's data that currently login in this app
@SuppressLint("StaticFieldLeak")
val db = FirebaseFirestore.getInstance()

// variables to fill the card layout using adapter
private var cardsAdapter: ArrayAdapter<User>? = null
private var rowItems = ArrayList<User>()

class SwipeFragment : Fragment() {

    private lateinit var binding: FragmentSwipeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSwipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDocumentRef = db.collection("users").document(currentUser!!.uid)
        userDocumentRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userDocument = task.result
                val userName = userDocument.getString("username")
                val userEmail = userDocument.getString("email")
                val userProfileURL = userDocument.getString("profile.photoURL")
                // fillingTheItems()
                fillingTheItems()

                Log.d("PENG", "username kamu adalah : $userName")
                Log.d("PENG", "email kamu adalah : $userEmail")
                Log.d("PENG", "url foto kamu adalah : $userProfileURL")
            } else {
                Log.d("PENG", "maaf ada error : ${task.exception}")
            }
        }

        // initialize the card adapter
        cardsAdapter = CardsAdapter(context, R.layout.item, rowItems)
        // add the adapter to the layout
        binding.frame.adapter = cardsAdapter
        // set the fling listener for the layout
        binding.frame.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                rowItems.removeAt(0)
                cardsAdapter?.notifyDataSetChanged()
            }

            override fun onLeftCardExit(p0: Any?) {

            }

            override fun onRightCardExit(p0: Any?) {

            }

            override fun onAdapterAboutToEmpty(p0: Int) {

            }

            override fun onScroll(p0: Float) {

            }
        })
    }

    private fun fillingTheItems() {
        val cardsQuery = db.collection("users").whereEqualTo("profile.isMale", true).limit(10)
        cardsQuery.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // val profile = document.data["profile"] as MutableMap<*, *>
                    // val displayName = profile["displayName"]
                    // Log.d("KARTU", "${document.id} => $displayName")
                    val user = document.toObject(User::class.java)
                    // Log.d("TESTI", "$user")
                    if (!rowItems.contains(user)) {
                        rowItems.add(user)
                        cardsAdapter?.notifyDataSetChanged()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("KARTU","get failed with : ", exception)
            }
    }
}


    /* MENCOBA UNTUK NAMPILIN TEKS DARI FIRESTORE CLOUD DATABASE (BERHASIL COY)
            val docRef = db.collection("users").document("06yJpLuZ79Dbzyky0TQL")
            val textDisplayName: TextView = view.findViewById(R.id.text_DisplayName)

            docRef.get()
                .addOnSuccessListener { documentSnapshot ->

                    textDisplayName.text = documentSnapshot.getString("profile.displayName")
                } */
