package com.example.mylib.ui.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mylib.R

class PaymentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_payment, container, false)

        val size = requireArguments().getString("size")
        val total = requireArguments().getString("total")

        val books = root.findViewById<TextView>(R.id.books)

        val cart = root.findViewById<TextView>(R.id.cart)

        val price = root.findViewById<TextView>(R.id.price)

        price.text = "UGX $total"
        cart.text = "Cart($size)"

        books.setOnClickListener {
            val storeFragment = StoreFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frag_container, storeFragment).addToBackStack(null).commit()
        }

        cart.setOnClickListener {
            val cartFragment = CartFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frag_container, cartFragment).addToBackStack(null).commit()
        }

        val mtn = root.findViewById<ImageView>(R.id.mtn)
        mtn.setOnClickListener {
            val finalFragment = FinalPaymentFragment()

            val bundle = Bundle()
            bundle.putString("size", size)
            bundle.putString("total", total)
            bundle.putInt("image", R.drawable.girl)

            finalFragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frag_container, finalFragment).addToBackStack(null).commit()

        }



        return root
    }

}