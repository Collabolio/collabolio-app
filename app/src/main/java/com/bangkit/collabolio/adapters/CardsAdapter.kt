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
        // get the layout if any or inflate the layout
        var finalView = convertView?: LayoutInflater.from(context).inflate(R.layout.item, parent, false)

        // get the ids of the fields
        var name = finalView.findViewById<TextView>(R.id.nameTV)
        var image = finalView.findViewById<ImageView>(R.id.imageViewPhoto)

        // add content in the layout
        name.text = "${user?.username}, ${user?.email}"
        val into = Glide.with(context)
            .load(user?.photoURL)
            .into(image)

        return finalView
    }
}