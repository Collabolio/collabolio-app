package com.bangkit.collabolio.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
import android.widget.ArrayAdapter
=======
>>>>>>> ccc12ab5a75df3e79eb8ae8960e2b4af403407dc
import androidx.fragment.app.Fragment
import com.bangkit.collabolio.R
import com.bangkit.collabolio.adapters.CardsAdapter
import com.bangkit.collabolio.adapters.FavoriteAdapter
import com.bangkit.collabolio.databinding.FragmentFavoriteBinding
import com.bangkit.collabolio.utilities.Favorite
import com.bangkit.collabolio.utilities.UserSwipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    // get current user
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    // initialize firestore database and fetch user's data that currently login in this app
    @SuppressLint("StaticFieldLeak")
    val db = FirebaseFirestore.getInstance()

    // variables to fill the card layout using adapter
    private var favoriteAdapter: ArrayAdapter<Favorite>? = null
    private var rowItems = ArrayList<Favorite>()
    private var users = mutableListOf<Favorite>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

    /*val favoriteViewModel =
            ViewModelProvider(this)[FavoriteViewModel::class.java] */
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get a reference to the RecyclerView
        val recyclerView = binding.rvFavorite
        val userDocumentRef = db.collection("users").document(currentUser!!.uid)
        userDocumentRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userDocument = task.result
                val userName = userDocument.getString("username")
                val userEmail = userDocument.getString("email")
                val userProfileURL = userDocument.getString("profile.photoURL")

                fillingTheItems()

            } else {
                Log.d("PENG", "maaf ada error : ${task.exception}")
            }
        }
        // Create a new instance of the FavoriteAdapter class and pass it the users list
        val favoriteAdapter = FavoriteAdapter(requireContext(), users)
        Log.d("DATAA", users.toString())
        // Set the adapter of the RecyclerView to the FavoriteAdapter instance.
        recyclerView.adapter = favoriteAdapter
    }

    private fun fillingTheItems() {
        val cardsQuery = db.collection("users").whereEqualTo("profile.isMale", true).limit(5)
        cardsQuery.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val user = document.toObject(Favorite::class.java)
                    Log.d("SUKL", user.toString())
                    if (!users.contains(user)) {
                        users.add(user)
                        favoriteAdapter?.notifyDataSetChanged()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("KARTU", "get failed with : ", exception)
            }
    }
}