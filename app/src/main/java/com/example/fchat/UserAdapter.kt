package com.example.fchat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class UserAdapter(context: Context, users: List<UserModel>) :
    ArrayAdapter<UserModel>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false)
        }

        val user = getItem(position)

        val usernameTextView: TextView = itemView!!.findViewById(R.id.usernameTextView)
        usernameTextView.text = user?.username

        return itemView
    }
}
