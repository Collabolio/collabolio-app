package com.bangkit.collabolio.utilities

data class User(
    val uid: String? = "",
    val username: String? = "",
    val email: String? = "",
    val photoURL: String? = ""
)

data class Profile(
    val skill: List<Skill>
)

data class Skill(
    val name: String
)