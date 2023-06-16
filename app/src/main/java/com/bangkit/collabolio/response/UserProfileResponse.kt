package com.bangkit.collabolio.response

data class UserProfileResponse(
    val email: String = "",
    val profile: Profile = Profile(),
    val uid: String = "",
<<<<<<< HEAD
=======
    val connections : List<Connections> = listOf()

>>>>>>> ccc12ab5a75df3e79eb8ae8960e2b4af403407dc
)