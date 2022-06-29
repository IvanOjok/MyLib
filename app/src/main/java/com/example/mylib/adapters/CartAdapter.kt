package com.example.mylib.adapters
//
//import android.content.Context
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.mylib.R
//import com.example.mylib.latest.HomeActivity
//import com.example.mylib.model.Cart
//import com.example.mylib.ui.store.CartFragment
//import com.example.mylib.ui.store.StoreFragment
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener

//class CartAdapter(r: Context, options: ArrayList<Cart>, phoned: String, ) :  RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
//    var y = options
//    var c = r
//    val phone = phoned
//
//    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        //val cart = itemView.findViewById<Button>(R.id.addToCart)
//        val remove = itemView.findViewById<ImageView>(R.id.remove)
//
//        fun onBind(c: Context, title:String, term: String, price:String, url: String) {
//
//            val bookTitle = itemView.findViewById<TextView>(R.id.title)
//
//            val bookTerm = itemView.findViewById<TextView>(R.id.term)
//
//            val bookPrice = itemView.findViewById<TextView>(R.id.price)
//
//            val image = itemView.findViewById<ImageView>(R.id.image)
//
//            bookTitle.text =  title
//            bookTerm.text = term
//            bookPrice.text = price
//            Glide.with(c).load(url).into(image)
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
//        val inflate =
//            LayoutInflater.from(parent.context).inflate(R.layout.cart_list, parent, false)
//        return CartViewHolder(inflate)
//    }
//
//    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        holder.onBind(c, y[position].title!!, y[position].term!!, y[position].price!!, y[position].url!!)
//
//        val titleterm = y[position].titleTerm
//        val id = y[position].id
//
//        holder.remove.setOnClickListener {
//
//            Log.d("array", y.size.toString())
//            //FirebaseDatabase.getInstance().getReference("/cart").child(phone).child(y[holder.adapterPosition].id!!).removeValue()
//
//
//            Log.d("adaper position", "${ y[holder.adapterPosition].id!! }")
//            removeItem(position)
//
////            val f = CartFragment()
////           val r = c as HomeActivity
////            r.supportFragmentManager.beginTransaction().detach(f).attach(f).commit()
//
//
//        }
//
//    }
//
//    fun removeItem(position: Int){
//
//        Log.d("index", position.toString())
//        Log.d("list", y[position].toString())
//
//
//        FirebaseDatabase.getInstance().getReference("/cart").child(phone).child(y[position].id!!).removeValue()
//        y.removeAt(position)
//        notifyItemRemoved(position)
//        // notifyItemRangeChanged(position, y.size)
//        notifyItemRangeInserted(position, y.size-1)
//
//
//
//         y.clear()
//
////        val f = StoreFragment()
////        val r = c as HomeActivity
////        r.supportFragmentManager.beginTransaction().replace(R.id.frag_container, f).addToBackStack(null).commit()
//
//
////        val f = CartFragment()
////        val r = c as HomeActivity
////        r.supportFragmentManager.beginTransaction().replace(R.id.frag_container, f).addToBackStack(null).commit()
//
//
////        if (!x.isCanceled){
//////            y.removeAt(position)
////            notifyItemRemoved(position)
//////            notifyItemRangeChanged(position, y.size)
//// //           notifyDataSetChanged()
////
////            val f = CartFragment()
////            val r = c as HomeActivity
////            r.supportFragmentManager.beginTransaction().replace(R.id.frag_container, f).addToBackStack(null).commit()
////
////        }
//
//        Log.d("index", position.toString())
//        Log.d("list", y.toString())
//    }
//
//    override fun getItemCount(): Int {
//        Log.d("initial", y.size.toString())
//        return y.size
//    }
//
//}
//
