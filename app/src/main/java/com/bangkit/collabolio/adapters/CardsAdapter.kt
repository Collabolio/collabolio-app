package com.bangkit.collabolio.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.collabolio.utilities.User
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bangkit.collabolio.R

class CardsAdapter(context: Context?, resourceId: Int, users: List<User>): ArrayAdapter<User>(
    context!!, resourceId, users) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // get the current user
        var user = getItem(position)
        val profile = user?.profile ?: emptyMap()
        val displayName = profile["displayName"] as? String
        val photoURL = profile["photoURL"] as? String
        val skills = profile["skills"] as ArrayList<Map<String, String>>
        for (skill in skills) {
            val skillName = skill["name"]
        }


        // get the layout if any or inflate the layout
        var finalView = convertView?: LayoutInflater.from(context).inflate(R.layout.item, parent, false)

        // get the ids of the fields
        var emailTV = finalView.findViewById<TextView>(R.id.emailTV)
        var nameTV = finalView.findViewById<TextView>(R.id.nameTV)
        var skillTV = finalView.findViewById<TextView>(R.id.skillTV)
        var image = finalView.findViewById<ImageView>(R.id.imageViewPhoto)

        // add content in the layout
        emailTV.text = "${user?.email}"
        nameTV.text = "$displayName"
        skillTV.text = skills.joinToString(", ") { it["name"].toString() }
        val into = Glide.with(context)
            .load(photoURL)
            .into(image)

        return finalView
    }
}