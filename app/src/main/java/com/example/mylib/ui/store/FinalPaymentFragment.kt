package com.example.mylib.ui.store

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mylib.R
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.Cart
import com.example.mylib.model.Library
import com.example.mylib.model.PrefsManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FinalPaymentFragment : Fragment() {

    private val prefsManager = PrefsManager.INSTANCE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_final_payment, container, false)

        prefsManager.setContext(requireActivity().application)
        val phone = prefsManager.getUser().phoneNo
        val profileName = prefsManager.getProfile().name

        val size = requireArguments().getString("size")
        val total = requireArguments().getString("total")
        val image = requireArguments().getInt("image")

        val cart = root.findViewById<TextView>(R.id.cart)
        cart.text = "Cart($size)"

        val mtn = root.findViewById<ImageView>(R.id.mtn)
        mtn.setImageResource(image)

        val editText = root.findViewById<EditText>(R.id.editText)
        editText.setText(phone)

        val p = ArrayList<Cart>()
        val pay  = root.findViewById<Button>(R.id.pay)
        pay.setOnClickListener {
            Log.d("gfh", "rdtgfh")
            FirebaseDatabase.getInstance().getReference("/cart").child(phone!!).
            addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (y in snapshot.children){
                        val item = y.getValue(Cart::class.java)
                        p.add(item!!)

                        val startDate = Calendar.getInstance().time
                        val sDf = SimpleDateFormat("dd-MMM-yyyy")
                        val createDate = sDf.format(startDate)

                        val first = startDate.toString()
                        val c = Calendar.getInstance()
                        c.time
                        c.add(Calendar.DATE, 365)
                        val endDate = sDf.format(c.time)


                            val id = item.id
                            val title= item.title
                            val term = item.term
                            val price = item.price
                            val url = item.url
                            val titleTerm =item.titleTerm
                            val bClass = item.bookClass
                            val lib = Library(id!!, title!!, term!!, price!!, url!!, titleTerm!!, createDate, endDate, bClass!!)
                            FirebaseDatabase.getInstance().getReference("/library").child(phone).child(profileName!!).child(bClass).push().setValue(lib)
                    }

                    FirebaseDatabase.getInstance().getReference("/cart").child(phone).removeValue()

                    startActivity(Intent(requireContext(), HomeActivity::class.java))
//
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "An $error occured", Toast.LENGTH_LONG).show()
                }

            })
        }


        return root

    }

}