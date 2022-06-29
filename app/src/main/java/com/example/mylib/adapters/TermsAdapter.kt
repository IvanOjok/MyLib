package com.example.mylib.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylib.Home
import com.example.mylib.R
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.Cart
import com.example.mylib.model.Term
import com.example.mylib.ui.store.CartFragment
import com.example.mylib.ui.store.PaymentFragment
import com.example.mylib.ui.store.PurchasesFragment
import com.google.firebase.database.FirebaseDatabase

class TermsAdapter(r: Context, options: ArrayList<Term>, phone:String) : RecyclerView.Adapter<TermsAdapter.TermsViewHolder>() {
    var y = options
    var c = r
    val x = phone

    class TermsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cart = itemView.findViewById<Button>(R.id.addToCart)
        val buy = itemView.findViewById<TextView>(R.id.price)

        fun onBind(c: Context, term:String, topic: String, lessons:String, price: String) {

            val termTitle = itemView.findViewById<TextView>(R.id.term)
            termTitle.text =  term

            val topics = itemView.findViewById<TextView>(R.id.topics)
            topics.text = "Topics: $topic"

       val lesson = itemView.findViewById<TextView>(R.id.lessons)
        lesson.text = "Lessons: $lessons"


        buy.text = "Buy One Year Access Now  \n UGX. $price"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermsViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.terms_list, parent, false)
        return TermsViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: TermsViewHolder, position: Int) {
        holder.onBind(c, y[position].termTitle!!, y[position].topics!!, y[position].lessons!!, y[position].termPrice!!)

        holder.cart.setOnClickListener {
            val x = c as HomeActivity
            val cart = CartFragment()
            val bundle = Bundle()
            bundle.putString("title", y[position].bookTitle)
            bundle.putString("term", y[position].termTitle)
            bundle.putString("price", y[position].termPrice)
            bundle.putString("url", y[position].bookUrl)
            bundle.putString("bClass", y[position].bookClass)
//            bundle.putString("color", y[position].bookColor)
//            bundle.putString("lessons", y[position].lessons)
//            bundle.putString("topics", y[position].topics)

            cart.arguments = bundle
            x.supportFragmentManager.beginTransaction().replace(R.id.frag_container, cart).addToBackStack("Store").commit()

        }

        holder.buy.setOnClickListener {
            val db = FirebaseDatabase.getInstance().getReference("/cart")
            val ref = db.child(x).push()
            val id = ref.key
            val cartItem =
                Cart(id!!, y[position].bookTitle!!, y[position].termTitle!!, y[position].termPrice!!,
                    y[position].bookUrl!!, "${y[position].bookTitle!!} + ${y[position].termTitle}", y[position].bookClass!!)
            ref.setValue(cartItem)

            val x = c as HomeActivity
            val paymentFragment = PaymentFragment()
            val bundle = Bundle()
            bundle.putString("size", "1")
            bundle.putString("total", y[position].termPrice)

            paymentFragment.arguments = bundle
            x.supportFragmentManager.beginTransaction().replace(R.id.frag_container, paymentFragment).addToBackStack(null).commit()

        }

    }

    override fun getItemCount(): Int {
        return y.size
    }
}

