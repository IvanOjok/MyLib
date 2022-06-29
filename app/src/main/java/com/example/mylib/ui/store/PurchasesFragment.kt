package com.example.mylib.ui.store

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.StoreClassAdapter
import com.example.mylib.adapters.TermsAdapter
import com.example.mylib.model.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PurchasesFragment : Fragment() {

    lateinit var terms:RecyclerView
    val bList = ArrayList<String>()
    private val prefsManager = PrefsManager.INSTANCE
    var phone: String ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_purchases, container, false)

        val title = requireArguments().getString("title")
        val description = requireArguments().getString("description")
        val price = requireArguments().getString("price")
        val url = requireArguments().getString("url")
        val color = requireArguments().getString("color")
        val lessons = requireArguments().getString("lessons")
        val topic = requireArguments().getString("topics")
        val bClass = requireArguments().getString("class")

        val bar = root.findViewById<MaterialToolbar>(R.id.bar)
        //bar.setBackgroundColor()

        val titleText = root.findViewById<TextView>(R.id.title)
        titleText.text = title

        val descriptionText = root.findViewById<TextView>(R.id.description)
        descriptionText.text = description

        val topics = root.findViewById<TextView>(R.id.topics)
        topics.text = "Topics: $topic"

        val lesson = root.findViewById<TextView>(R.id.lessons)
        lesson.text = "Lessons: $lessons"

        val buy = root.findViewById<TextView>(R.id.price)
        buy.text = "Buy One Year Access Now  \n UGX. $price"

        phone = prefsManager.getUser().phoneNo!!
        buy.setOnClickListener {
            val db = FirebaseDatabase.getInstance().getReference("/cart")
            val ref = db.child(phone!!).push()
            val id = ref.key
            val cartItem =
                Cart(id!!, title!!, "Term 1, Term 2, Term 3", price!!, url!!, "$title\"Term 1, Term 2, Term 3\"", bClass!!)
            ref.setValue(cartItem)

            val paymentFragment = PaymentFragment()
            val bundle = Bundle()
            bundle.putString("size", "1")
            bundle.putString("total", price.toString())

            paymentFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frag_container, paymentFragment).addToBackStack(null).commit()

        }

        val addToCart = root.findViewById<Button>(R.id.addToCart)
        addToCart.setOnClickListener {
            val cartFragment = CartFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("term", "Term 1, Term 2, Term 3")
            bundle.putString("price", price)
            bundle.putString("url", url)
            bundle.putString("bClass", bClass)

            Log.d("BPrice", price!!)
            cartFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frag_container, cartFragment).addToBackStack(null).commit()

        }


        val card = root.findViewById<CardView>(R.id.card)
        val term = root.findViewById<TextView>(R.id.term)
        term.setOnClickListener {
            card.visibility = View.INVISIBLE
            getTerms(title!!)
        }

        terms = root.findViewById<RecyclerView>(R.id.terms)

        val books = root.findViewById<TextView>(R.id.books)
        books.setOnClickListener {
            terms.visibility = View.INVISIBLE
            card.visibility = View.VISIBLE

        }


        return root
    }

    fun getTerms(titleText:String){
        val detail = ArrayList<Term>()
        val dbRef = FirebaseDatabase.getInstance().getReference("/terms").child(titleText).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //dialog!!.dismiss()
                Log.d("snap", "$snapshot")
                for (k in snapshot.children) {
                    val key = k.key
                    Log.d("key", k.toString())
                    val z = k.getValue(Term::class.java)
                    Log.d("z", z.toString())
                    //if(titleText == key){
                        detail.add(z!!)
                    //}

                    val bAdapter = TermsAdapter(requireContext(), detail, phone!!)
                    terms.visibility = View.VISIBLE
                    terms.adapter = bAdapter
                    terms.layoutManager =
                        LinearLayoutManager(requireContext())

                }

            }

            override fun onCancelled(error: DatabaseError) {
               // dialog!!.dismiss()
                Toast.makeText(requireContext(), "An $error occured", Toast.LENGTH_LONG).show()
            }

        })
    }


}