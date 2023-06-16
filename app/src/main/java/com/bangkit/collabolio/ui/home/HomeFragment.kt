package com.bangkit.collabolio.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bangkit.collabolio.R
import com.bangkit.collabolio.adapters.CardsAdapter
import com.bangkit.collabolio.databinding.FragmentHomeBinding
import com.bangkit.collabolio.response.Connections
import com.bangkit.collabolio.ui.UserProfile
import com.bangkit.collabolio.ui.Users
import com.bangkit.collabolio.utilities.ApiRequest
import com.bangkit.collabolio.utilities.UserSwipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // get current user
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    // initialize firestore database and fetch user's data that currently login in this app
    @SuppressLint("StaticFieldLeak")
    val db = FirebaseFirestore.getInstance()

    // variables to fill the card layout using adapter
    private var cardsAdapter: ArrayAdapter<UserSwipe>? = null
    private var rowItems = ArrayList<UserSwipe>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]*/

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
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
                // apiReqForUids()
                //fetchAllUids()
                fillingTheItemsByApi()
            } else {
                Log.d("PENG", "maaf ada error : ${task.exception}")
            }
        }

        // initialize the card adapter
        cardsAdapter = CardsAdapter(context, resourceId = R.layout.item, users = rowItems)
        // add the adapter to the layout
        binding.frame.adapter = cardsAdapter
        // set the fling listener for the layout
        binding.frame.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                rowItems.removeAt(0)
                cardsAdapter?.notifyDataSetChanged()
            }

            override fun onLeftCardExit(p0: Any?) {
                //
            }

            override fun onRightCardExit(p0: Any?) {
                val selectedUser = p0 as UserSwipe
                val selectedUserId = selectedUser.uid
                if (!selectedUserId.isNullOrEmpty()) {
                    Toast.makeText(context, "${selectedUser.profile?.get("displayName")} ditambahkan ke favorit",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAdapterAboutToEmpty(p0: Int) {
                fillingTheItemsByApi()
            }

            override fun onScroll(p0: Float) {
                //
            }

        })

        /*homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.getUserSwipe() */

    }

    /*private fun fillingTheItems() {
        val cardsQuery = db.collection("users").whereEqualTo("profile.isMale", true)
        cardsQuery.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val user = document.toObject(UserSwipe::class.java)
                    if (!rowItems.contains(user)) {
                        rowItems.add(user)
                        cardsAdapter?.notifyDataSetChanged()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("KARTU", "get failed with : ", exception)
            }
    }*/

    private fun apiReqForUids(): MutableList<String> {
        val apiRequest = ApiRequest()
        val uidsResult = mutableListOf<String>()
        val thread = Thread(Runnable {
            val users = apiRequest.getUsers()
            for (user in users!!) {
                uidsResult.add(user.uid.toString())
            }
        })

        thread.start()
        thread.join()
        return uidsResult
    }

    private fun fillingTheItemsByApi() {
        val uids = apiReqForUids()
        val cardsQuery = db.collection("users").whereIn("uid",uids)
        cardsQuery.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val user = document.toObject(UserSwipe::class.java)
                    if (!rowItems.contains(user)) {
                        rowItems.add(user)
                        cardsAdapter?.notifyDataSetChanged()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("KARTU", "get failed with : ", exception)
            }
        // Log.d("APISA", cardsQuery.get().toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}