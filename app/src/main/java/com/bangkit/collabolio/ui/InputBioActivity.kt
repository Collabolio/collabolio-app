package com.bangkit.collabolio.ui

//noinspection SuspiciousImport
import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import com.bangkit.collabolio.databinding.ActivityInputbioBinding
import com.bangkit.collabolio.utilities.Skill
import com.bangkit.collabolio.utilities.UserProfile
import com.bangkit.collabolio.utilities.User
import com.bangkit.collabolio.utilities.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class InputBioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputbioBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputbioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        fetchSkills()

        binding.btnInputBio.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val displayName = binding.inputName.text.toString()
        val phoneNumber = binding.inputNumber.text.toString()
        val birthDate = binding.inputDate.text.toString()
        val isMale = binding.radioButtonMale.isChecked
        val location = binding.inputAddress.text.toString()
        val educations = binding.inputEducation.text.toString()
        val bio = binding.inputBio.text.toString()
        val userSkills = binding.mactvSkills.text.toString()
        val arraySkills = userSkills.split(",").map{it.trim()}

        val skillsList = mutableListOf<Skill>()
        for (skillName in arraySkills) {
            val skill = Skill(skillName)
            skillsList.add(skill)
        }



        val userProfile = UserProfile(displayName, phoneNumber, birthDate, isMale, location, educations, bio,
            skillsList)
        val users = Users((userProfile))

        /*val user = hashMapOf(
            "profile" to hashMapOf(
                "displayName" to displayName,
                "phoneNumber" to phoneNumber,
                "birthDate" to birthDate,
                "isMale" to isMale,
                "bio" to bio
            )
        )*/
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            val userId = it.uid
            val userRef = firestore.collection("users").document(userId)
            when {
                displayName.isEmpty() -> {
                    binding.inputName.error = "masukkan Nama"
                }
                phoneNumber.isEmpty() -> {
                    binding.inputNumber.error = "Masukkan Nomor Telepon"
                }
                birthDate.isEmpty() -> {
                    binding.inputNumber.error = "Masukkan Tanggal Lahir"
                }else -> {

                userRef.set(users, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Profil gagal diperbarui", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            }
        }
    }
    private fun fetchSkills() {
        val skillsCollection = firestore.collection("skills")

        skillsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val skillsList = mutableListOf<String>()

                for (document in querySnapshot.documents) {
                    val skill = document.getString("name")
                    skill?.let { skillsList.add(it) }
                }

                val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, skillsList)
                binding.mactvSkills.setAdapter(adapter)
                binding.mactvSkills.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
            }
            .addOnFailureListener {
            }
    }
}
