package com.example.mylib.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylib.R
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.Cart
import com.example.mylib.ui.store.CartFragment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.InternalCoroutinesApi


//class ThCartAdapter: FirebaseRecyclerAdapter(CartAdapter.CartViewHolder,  Cart()) {
//
//    class CartViewHolder() : RecyclerView.ViewHolder() {
//        constructor(v: View):super {
//        }
//
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
//}
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
//
//        }
//
//    }
//}

class ThCartAdapter(r:Context,
    options: FirebaseRecyclerOptions<Cart?>?, phoneNo:String
) :
    FirebaseRecyclerAdapter<Cart, ThCartAdapter.personsViewholder>(
        options!!
    ) {
    // Function to bind the view in Card view(here
    // "person.xml") with data in
    // model class(here "person.class")
    val c = r
    val query = options
    val phone = phoneNo

     @OptIn(InternalCoroutinesApi::class)
     override fun onBindViewHolder(
        holder: personsViewholder,
        position: Int,  model: Cart
    ) {

        holder.onBind(c, model.title!!, model.term!!, model.price!!, model.url!!)
        val titleterm = model.titleTerm
        //val id = model.id

         holder.remove.setOnClickListener {

             if (model == null){
                 kotlinx.coroutines.internal.synchronized(model) {
                     val f = CartFragment()
                     val r = c as HomeActivity
                     r.supportFragmentManager.beginTransaction().detach(f).attach(f).commit()
                 }
             }
             else {
                 val ref = getRef(position)
                 val id = ref.key
                 Log.d("remove", id!!)
                 val t = FirebaseDatabase.getInstance().getReference("/cart").child(phone).child(id)
                     .removeValue()

             }

         }
    }



    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown

    override fun onCreateViewHolder(
         parent: ViewGroup,
        viewType: Int
    ): personsViewholder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_list, parent, false)
        return personsViewholder(view)
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    inner class personsViewholder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun onBind(c: Context, title:String, term: String, price:String, url: String) {
            val bookTitle = itemView.findViewById<TextView>(R.id.title)
            val bookTerm = itemView.findViewById<TextView>(R.id.term)
            val bookPrice = itemView.findViewById<TextView>(R.id.price)
            val image = itemView.findViewById<ImageView>(R.id.image)
            bookTitle.text =  title
            bookTerm.text = term
            bookPrice.text = price
            Glide.with(c).load(url).into(image)
        }

        val remove = itemView.findViewById<ImageView>(R.id.remove)

    }


//    private val mListener: ChildEventListener = object : ChildEventListener {
//        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
//            val key = dataSnapshot.key
//            if (!keys.contains(key)) {
//                val item = getConvertedObject(dataSnapshot)
//                val insertedPosition: Int
//                insertedPosition = if (previousChildName == null) {
//                    items.add(0, item!!)
//                    keys.add(0, key!!)
//                    0
//                } else {
//                    val previousIndex = keys.indexOf(previousChildName)
//                    val nextIndex = previousIndex + 1
//                    if (nextIndex == items.size) {
//                        items.add(item!!)
//                        keys.add(key!!)
//                    } else {
//                        items.add(nextIndex, item!!)
//                        keys.add(nextIndex, key!!)
//                    }
//                    nextIndex
//                }
//                notifyItemInserted(insertedPosition)
//                itemAdded(item, key, insertedPosition)
//            }
//        }
//
//        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
//            val key = dataSnapshot.key
//            if (keys.contains(key)) {
//                val index = keys.indexOf(key)
//                val oldItem: T? = items[index]
//                val newItem = getConvertedObject(dataSnapshot)
//                items.set(index, newItem!!)
//                notifyItemChanged(index)
//                itemChanged(oldItem, newItem, key, index)
//            }
//        }
//
//        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
//            val key = dataSnapshot.key
//            if (keys.contains(key)) {
//                val index = keys.indexOf(key)
//                val item: T? = items[index]
//                keys.removeAt(index)
//                items.removeAt(index)
//                notifyItemRemoved(index)
//                itemRemoved(item, key, index)
//            }
//        }
//
//        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
//            val key = dataSnapshot.key
//            val index = keys.indexOf(key)
//            val item = getConvertedObject(dataSnapshot)
//            items.removeAt(index)
//            keys.removeAt(index)
//            val newPosition: Int
//            newPosition = if (previousChildName == null) {
//                items.add(0, item!!)
//                keys.add(0, key!!)
//                0
//            } else {
//                val previousIndex = keys.indexOf(previousChildName)
//                val nextIndex = previousIndex + 1
//                if (nextIndex == items.size) {
//                    items.add(item!!)
//                    keys.add(key!!)
//                } else {
//                    items.add(nextIndex, item!!)
//                    keys.add(nextIndex, key!!)
//                }
//                nextIndex
//            }
//            notifyItemMoved(index, newPosition)
//            itemMoved(item, key, index, newPosition)
//        }
//
//        override fun onCancelled(databaseError: DatabaseError) {
//            Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur.")
//        }
//    }



//    val t = FirebaseDatabase.getInstance().getReference("/cart").child(phone)
//        .addChildEventListener(object : ChildEventListener{
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                val r = snapshot.getValue(Cart::class.java)
//                snapshot.ref.removeValue()
//                Log.d("Added", r.toString())
//            }
//
//            override fun onChildChanged(
//                snapshot: DataSnapshot,
//                previousChildName: String?
//            ) {
//                val r = snapshot.getValue(Cart::class.java)
//                Log.d("Changed", r.toString())
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//                val r = snapshot.getValue(Cart::class.java)
//                Log.d("Removed", r.toString())
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                val r = snapshot.getValue(Cart::class.java)
//                Log.d("Moved", r.toString())
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                val r = error.message
//                Log.d("Added", r)
//            }
//
//        })
}