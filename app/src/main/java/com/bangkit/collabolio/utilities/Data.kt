package com.bangkit.collabolio.utilities

import com.google.firebase.firestore.DocumentSnapshot

data class User(
    val profile: Map<String,Any>? = null,
    val uid: String? = "",
    val displayName: String? = "",
    val email: String? = "",
    val photoURL: String? = "",
    val skills: ArrayList<String>? = null,
    val skill: Map<*,*>? = null,
    val skillName: String? = ""
)

data class Users(
    val profile: UserProfile?
)
data class UserProfile(
    val displayName: String,
    val phoneNumber: String,
    val birthDate: String,
    val isMale: Boolean,
    val Location: String,
    val educations: String,
    val bio: String,
    val skills : List<Skill>

)

data class Skill(
    val name: String
)