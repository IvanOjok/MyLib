package com.example.mylib.model

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.AddProfile
import com.example.mylib.Home
import com.example.mylib.R
import com.example.mylib.latest.HomeActivity

class ProfileAdapter(r: Context, options: List<ProfileClass>) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>(){
    var y = options
    var c = r
    private val prefsManager = PrefsManager.INSTANCE
    class ProfileViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(gender:String, name:String){

            val n = itemView.findViewById<TextView>(R.id.name)
            n.text = name

            val d = itemView.findViewById<ImageView>(R.id.image)
            if (gender == "M"){
                d.setImageResource(R.drawable.boy)
            }
            else if (gender == "F"){
                d.setImageResource(R.drawable.girl)
            }
            else if (gender == "Add"){
                d.setImageResource(R.drawable.add)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.profiles_list, parent, false)
        return ProfileViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.onBind(y[position].gender, y[position].name)
        holder.itemView.setOnClickListener {
            if (y[position].gender != "M" && y[position].gender != "F"){
                c.startActivity(Intent(c, AddProfile::class.java))
            }
            else{
                val intent = Intent(c, HomeActivity::class.java)
                prefsManager.onLoginProfile(Profile(y[position].id, y[position].name, y[position].gender, y[position].stdClass))
                intent.putExtra("name", y[position].name)
                c.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return  y.size
    }

}