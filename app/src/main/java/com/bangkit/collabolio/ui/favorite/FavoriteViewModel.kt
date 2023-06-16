package com.bangkit.collabolio.ui.favorite


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



class FavoriteViewModel : ViewModel() {
<<<<<<< Updated upstream
    private val _userFavoriteData = MutableLiveData<List<UserProfileResponse?>>()
    val userFavoriteData : LiveData<List<UserProfileResponse?>> = _userFavoriteData
    fun getUserFavorite() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        if (userId != null) {
            db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val userConnections = documentSnapshot.toObject(UserProfileResponse::class.java)?.connections
                        if (!userConnections.isNullOrEmpty()) {
                            val connectionUIDs = userConnections.map { it.uid }

                            db.collection("users").whereIn("uid", connectionUIDs)
                                .get()
                                .addOnSuccessListener { querySnapshot ->
                                    if (querySnapshot != null) {
                                        val userProfile = documentSnapshot.toObject(
                                            UserProfileResponse::class.java)
                                        _userFavoriteData.value = listOf(userProfile)
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d("UserViewModel", "error : $exception")
                                }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("UserViewModel", "error : $exception")
                }
        }
=======
    private val _text = MutableLiveData<String>().apply {
        value = "Coming Soon"
>>>>>>> Stashed changes
    }
    val text: LiveData<String> = _text
}