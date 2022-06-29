package com.example.mylib.ui.store

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.Home
import com.example.mylib.R
import com.example.mylib.adapters.ThCartAdapter
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.*
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*


class CartFragment : Fragment() {
    private val prefsManager = PrefsManager.INSTANCE

    var title:String?=null
    var term:String?=null
    var price:String?=null
    var url :String?=null
    var bClass :String?=null
    lateinit var discount:TextView
    lateinit var subTotal:TextView
    lateinit var cartRecycler:RecyclerView
    lateinit var cart:TextView
    lateinit var addToCart:Button
    lateinit var phone:String
    var size: Int ?= null
    var sum: Int = 0
    val p = ArrayList<Cart>()

    lateinit var adapter: ThCartAdapter

    lateinit var viewModel: CartViewModel
    //lateinit var cart: ArrayList<Cart>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_cart, container, false)

//        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_cart, container, false )
//       binding.lifecycleOwner = this.viewLifecycleOwner
//
         val books = root.findViewById<TextView>(R.id.books)
        cart = root.findViewById(R.id.cart)
        cartRecycler = root.findViewById(R.id.cartRecycler)
        addToCart = root.findViewById(R.id.addToCart)
        subTotal = root.findViewById(R.id.subTotal)
        discount = root.findViewById(R.id.discount)


        prefsManager.setContext(requireActivity().application)
        phone = prefsManager.getUser().phoneNo!!

        cartRecycler = root.findViewById<RecyclerView>(R.id.cartRecycler)

        class getCartDao:GetCartDao{}
        val cartRepository = CartRepository(getCartDao())
        val factory = CartViewModelFactory(cartRepository)

        viewModel = ViewModelProvider(requireActivity(), factory).get(CartViewModel::class.java)



        val cartAdapter = CartRecyclerAdapter(requireContext(), phone, arrayListOf(), viewModel)
        cartRecycler.layoutManager = LinearLayoutManager(requireContext())
        cartRecycler.adapter = cartAdapter

        viewModel.allCartItems(phone).observe(viewLifecycleOwner, Observer {

            Log.d("pp", it.toString() )
            cartAdapter.list = it
            cartAdapter.notifyDataSetChanged()

            Log.d("pList", "${cartAdapter.cost}" )

            requireFragmentManager().beginTransaction().detach(this@CartFragment).attach(this@CartFragment).commit()


            //sum = cartAdapter.cost

            sum = 0
            for (i in 0 until cartAdapter.list.size){
                sum = 0
                sum += cartAdapter.list[i].price!!.toInt()

            }
            size = cartAdapter.list.size

            Log.d("pSum", "$sum" )

            cart.text = "Cart ($size)"
            subTotal.text = "SubTotal: UGX. $sum"
            discount.text = "Discount: 0"


        })


       //  requireFragmentManager().beginTransaction().detach(this@CartFragment).attach(this@CartFragment).commit()






        books.setOnClickListener {
            val storeFragment = StoreFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frag_container, storeFragment).addToBackStack(null).commit()
        }

        cart.setOnClickListener {
            requireFragmentManager().beginTransaction().detach(this).attach(this).commit()
        }


        prefsManager.setContext(requireActivity().application)

        val bundle = arguments
        if (bundle != null){
        title =bundle.getString("title")
        term = bundle.getString("term")
        price =bundle.getString("price")
        url = bundle.getString("url")
        bClass = bundle.getString("bClass")
        }

        phone = prefsManager.getUser().phoneNo!!
        if (title != null && term != null && price != null && url != null){
          //  val cartItem =
           //     Cart(title!!, title!!, term!!, price!!, url!!, "$title$term", bClass!!)

               Log.d("insert", "insertion")
            Log.d("price", price!!)

            //"Term 1, Term 2, Term 3"

            viewModel.insert(phone, title!!, term!!, price!!, url!!, bClass!!)
           // cartAdapter.list.clear()

        }


        addToCart.setOnClickListener {
            if (size!! > 0){ 

            val paymentFragment = PaymentFragment()
            val bundle = Bundle()
            bundle.putString("size", size.toString())
            bundle.putString("total", sum.toString())

            paymentFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frag_container, paymentFragment).addToBackStack(null).commit()
            }
        }

        return root
    }



}