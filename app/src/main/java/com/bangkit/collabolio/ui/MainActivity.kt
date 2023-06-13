package com.bangkit.collabolio.ui


import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentContainerView
import com.bangkit.collabolio.CollabolioCallback
import com.bangkit.collabolio.R
import com.bangkit.collabolio.databinding.ActivityMainBinding
import com.bangkit.collabolio.fragments.ProfileFragment
import com.bangkit.collabolio.fragments.SwipeFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), CollabolioCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    // creating variables for the fragments
    private var swipeFragment: SwipeFragment? = null
    private var profileFragment: ProfileFragment? = null

    // creating variables for the tab layout
    private var profileTab: TabLayout.Tab? = null
    private var swipeTab: TabLayout.Tab? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth
        // val db = Firebase.firestore
        // getDataInLog()

        // dynamically creating new tabs in the tab layout
        profileTab = binding.navigationTabs.newTab()
        swipeTab = binding.navigationTabs.newTab()

        // setting the icons of the tab
        profileTab?.icon = ContextCompat.getDrawable(this, R.drawable.profile)
        swipeTab?.icon = ContextCompat.getDrawable(this, R.drawable.swipe)

        // adding to the tab layout
        binding.navigationTabs.addTab(profileTab!!)
        binding.navigationTabs.addTab(swipeTab!!)

        // adding tab selected listener to the tab layout
        binding.navigationTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab){
                    profileTab -> {
                        if(profileFragment == null){
                            profileFragment = ProfileFragment()
                        }
                        replaceFragment(profileFragment!!)
                    }
                    swipeTab -> {
                        if(swipeFragment == null){
                            swipeFragment = SwipeFragment()
                        }
                        replaceFragment(swipeFragment!!)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                onTabSelected(tab)
            }
        })
        swipeTab?.select()
    }

    fun replaceFragment (fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun getDataInLog() {
        val db = Firebase.firestore
        // Create a reference to the users collection
        val usersRef = db.collection("users").document("06yJpLuZ79Dbzyky0TQL")
        val docsRef = db.collection("users")
        val docsFilter = docsRef.whereEqualTo("profile.age",25).limit(4)
     // val docsRefOrder = docsFilter.orderBy("updatedAt", Query.Direction.DESCENDING).limit(4)

        usersRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val profile = document.data!!["profile"] as MutableMap<*,*>
                    val skills = profile["skills"]
                    if (skills != null) {
                        for (skill in skills as ArrayList<*>) {
                            val skillName = (skill as Map<*,*>)["name"]
                            Log.d("SINGHS", "${document.id} => $skillName")
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("SINGHS","get failed with ", exception)
            }

        /*docsFilter.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val profile = document.data["profile"] as MutableMap<String, Any>
                    val displayName = profile["displayName"]
                    Log.d("SINGH", "${document.id} => $displayName")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("SINGH", "get failed with ", exception)
            }*/

    }

    override fun onSignOut() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}