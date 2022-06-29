package com.example.mylib.ui.library

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylib.R
import com.example.mylib.adapters.QuizAdapter
import com.example.mylib.model.*
import com.example.mylib.model.offline.AppDatabase
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizActivity : AppCompatActivity() {
   lateinit var radioGroup: RadioGroup
    lateinit var multipleChoice:LinearLayout
    lateinit var quizRecycler:RecyclerView
    lateinit var shortAnswer:EditText

    lateinit var image:ImageView
    lateinit var question:TextView
    lateinit var choice:TextView
      var index = 0
    lateinit var complete:TextView

    var one = mutableListOf<String>()
    var short = mutableListOf<String>()
    var multiple = mutableListOf<String>()

    private val prefsManager = PrefsManager.INSTANCE
    lateinit var dbase: AppDatabase

    lateinit var navigation:BottomNavigationView
    lateinit var quiz:Button

    lateinit var bTitle:String
    lateinit var bTopic:String
   lateinit var bTerm:String
    lateinit var bClass:String
    lateinit var lesson:String
    lateinit var phone:String
    lateinit var profileName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        dbase = AppDatabase.getInstance(applicationContext)
        prefsManager.setContext(this.application)

        phone = prefsManager.getUser().phoneNo!!
        profileName = prefsManager.getProfile().name!!

        radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        multipleChoice = findViewById(R.id.multipleChoice)
        quizRecycler = findViewById<RecyclerView>(R.id.quizRecycler)
        shortAnswer = findViewById<EditText>(R.id.shortAnswer)
        image = findViewById<ImageView>(R.id.image)
        question = findViewById<TextView>(R.id.question)
        choice = findViewById<TextView>(R.id.choice)

        quizRecycler.visibility = View.INVISIBLE

        bTitle = intent.getStringExtra("bookTitle")!!
        bTopic = intent.getStringExtra("topicTitle")!!
        bTerm = intent.getStringExtra("termTitle")!!
        bClass = intent.getStringExtra("bookClass")!!
        lesson = intent.getStringExtra("lessonId")!!

        val bookTitle = findViewById<TextView>(R.id.title)
        bookTitle.text = lesson

        var color:String = "faff0cac"
        val bar = findViewById<MaterialToolbar>(R.id.bar)
        complete = findViewById<TextView>(R.id.complete)

        FirebaseDatabase.getInstance().getReference("/book").child(bClass).child(bTitle!!).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val z = snapshot.getValue(Book::class.java)
                color = z!!.bookColor!!
                bar.setBackgroundColor(Color.parseColor(color))

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        //val quizRecycler = findViewById<RecyclerView>(R.id.quizRecycler)

        val t = ArrayList<Quiz>()
        FirebaseDatabase.getInstance().getReference("/quiz").child(bTitle).child(bTerm!!).child(bTopic!!).child(lesson!!)     //.orderByChild("bookClass").equalTo(bClass)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (k in snapshot.children){
                        val keys = k
                        val z = k.getValue(Quiz::class.java)
                        t.add(z!!)
                    }
                    if(t.size > 0){
                        setQuestion(t, 0)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Error", "$error")
                }
            })

        navigation = findViewById<BottomNavigationView>(R.id.nav_quiz_view)

        navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                  R.id.navigation_back -> {
                      if(index > 0){
                          index -= 1
                          setQuestion(t, index)
                      }
                  }

                 R.id.navigation_next -> {
                     if(index < t.size-1){
                         index += 1
                         setQuestion(t, index)
                     }

                 }
            }
            true
        }

        quiz = findViewById<Button>(R.id.quiz)
        quiz.setOnClickListener {
            val i = index
            if (t.size > 0){
                val type = t[i].choiceType
                var correct = mutableListOf<Int>()
                if (type == "Short Answer"){
                    val answer = shortAnswer.text.toString()
                    short.add(answer)

                    if(short.isNotEmpty()) {
                        for (j in t[i].answers!!) {
                            for (k in short) {
                                if (j == k) {
                                    correct.add(t[i].answers!!.indexOf(j))
                                }
                            }
                        }

                                FirebaseDatabase.getInstance().getReference("/quiz").child(bTitle).
                                child(bTerm).child(bTopic).child(lesson).orderByChild("question").equalTo(t[i].question)
                                    .addValueEventListener(object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                var key: String ?= null
                                                var res = Quiz()
                                                if (snapshot.exists()){
                                                    for(r in snapshot.children){
                                                        key = r.key
                                                        res = r.getValue(Quiz::class.java)!!
                                                    }
                                                FirebaseDatabase.getInstance().getReference("/progress").child(phone!!)
                                                    .child(profileName!!).child(bTitle).child(bTerm).child(bTopic)
                                                    .child(lesson).child(key!!).setValue(Submission(res,correct, short))
                                            }

                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.d("Error", "$error")
                                        }
                                    })

                        //added to increment index to next quiz
                        if(index < t.size-1){
                            index += 1
                            t.removeAt(i)
                            setQuestion(t, index)
                        }
                        else if(index == t.size-1 && t.size > 1){
                            index -= 1
                            t.removeAt(i)
                            setQuestion(t, index)
                        }
                        else if(index == t.size-1 && t.size == 1){
                            displayLast()
                        }


                    }else{
                        Toast.makeText(this, "Fill in an answer", Toast.LENGTH_SHORT).show()
                    }

                }


                else if(type == "One Answer"){
                    var a: String ?= null
                    //button.setOnClickListener {
                        val id = radioGroup.checkedRadioButtonId
                        val b = findViewById<RadioButton>(id)
                        a = b.text.toString()
                        Log.d("answerButton", a)
                    one.add(a)

                    if (one.isEmpty()){
                        Toast.makeText(this, "Fill in the correct option", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "One is ${one.get(0)}", Toast.LENGTH_SHORT).show()

                        for (j in t[i].answers!!) {
                            for(k in one){
                                if (j == k){
                                   correct.add(t[i].answers!!.indexOf(j))
                                }
                            }
                        }

                        FirebaseDatabase.getInstance().getReference("/quiz").child(bTitle).
                        child(bTerm).child(bTopic).child(lesson).orderByChild("question").equalTo(t[i].question)
                            .addValueEventListener(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                        var key: String ?= null
                                        var res = Quiz()
                                        if (snapshot.exists()){
                                            for(r in snapshot.children){
                                                key = r.key
                                                res = r.getValue(Quiz::class.java)!!
                                            }
                                        FirebaseDatabase.getInstance().getReference("/progress").child(phone!!)
                                            .child(profileName!!).child(bTitle).child(bTerm).child(bTopic)
                                            .child(lesson).child(key!!).setValue(Submission(res,correct, one))
                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.d("Error", "$error")
                                }
                            })

                        //added to increment index to next quiz
                        if(index < t.size-1){
                            index += 1
                            t.removeAt(i)
                            setQuestion(t, index)
                        }
                        else if(index == t.size-1 && t.size > 1){
                            index -= 1
                            t.removeAt(i)
                            setQuestion(t, index)
                        }
                        else if(index == t.size-1 && t.size == 1){
                            displayLast()
                        }

                    }
                }


                else if(type == "Multiple Choice"){
                    val total = multipleChoice.childCount
                    for (q in 0 until total){
                        val checkBox = multipleChoice.getChildAt(q)
                        val check = checkBox as CheckBox

                        //val check = findViewById<CheckBox>(id)
                        if (check.isChecked){

                            multiple.add(check.text.toString())
                            Log.d("multipleadd", check.text.toString())
                            Log.d("multipleaddlist", multiple.size.toString())
                        }
                    }

                    if (multiple.isEmpty()){
                        Toast.makeText(this, "Fill in the correct option", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Multiple is ${multiple.get(0)}", Toast.LENGTH_SHORT).show()

                        for (j in t[i].answers!!){
                            for (k in multiple){
                                if (j == k){
                                    correct.add(t[i].answers!!.indexOf(j))
                                }
                            }
                        }
                        FirebaseDatabase.getInstance().getReference("/quiz").child(bTitle).
                        child(bTerm).child(bTopic).child(lesson).orderByChild("question").equalTo(t[i].question)
                            .addValueEventListener(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var key: String ?= null
                                    var res = Quiz()
                                    if (snapshot.exists()){
                                        for(r in snapshot.children){
                                            key = r.key
                                            res = r.getValue(Quiz::class.java)!!
                                        }
                                        FirebaseDatabase.getInstance().getReference("/progress").child(phone!!)
                                            .child(profileName!!).child(bTitle).child(bTerm).child(bTopic)
                                            .child(lesson).child(key!!).setValue(Submission(res,correct, multiple))
                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.d("Error", "$error")
                                }
                            })

                        //added to increment index to next quiz
                        if(index < t.size-1){
                            index += 1
                            t.removeAt(i)
                            setQuestion(t, index)
                        }
                        else if(index == t.size-1 && t.size > 1){
                            index -= 1
                            t.removeAt(i)
                            setQuestion(t, index)
                        }
                        else if(index == t.size-1 && t.size == 1){
                            displayLast()
                        }

                    }

                }

            }


        }

    }

    fun displayLast(){

            quiz.setText("Submit for Assessment")
            quiz.visibility = View.INVISIBLE
            navigation.visibility = View.INVISIBLE
            val scroll = findViewById<ScrollView>(R.id.scroll)
            scroll.visibility = View.INVISIBLE
            quizRecycler.visibility = View.VISIBLE

            val answerList = ArrayList<Submission>()
            FirebaseDatabase.getInstance().getReference("/progress").child(phone)
                .child(profileName).child(bTitle).child(bTerm).child(bTopic)
                .child(lesson)
                .addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            for(r in snapshot.children){
                                val res = r.getValue(Submission::class.java)!!
                                answerList.add(res)
                            }

                            val bAdapter = QuizAdapter(this@QuizActivity, answerList)
                            quizRecycler.adapter = bAdapter
                            Log.d("bAdapter", "${bAdapter.y}")
                            quizRecycler.layoutManager = LinearLayoutManager(this@QuizActivity)

                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("Error", "$error")
                    }
                })
    }

    fun setQuestion(t: ArrayList<Quiz>, i:Int){
        question.text = t[i].question

        choice.text = "${t[i].choice} \n (${t[i].choiceType})"
        complete.text = "Score ${i+1}/${t.size}"
        if (t[i].mediaType == "image"){
            Glide.with(this).load(t[i].media).into(image)
        }


        val choiceType = t[i].choiceType
        if (choiceType == "Short Answer"){
            radioGroup.removeAllViews()
            multipleChoice.removeAllViews()
            shortAnswer.visibility = View.VISIBLE
            radioGroup.visibility = View.INVISIBLE
            multipleChoice.visibility = View.INVISIBLE
            Log.d("short", choiceType)

        }
        else if(choiceType == "One Answer"){
            radioGroup.removeAllViews()
            shortAnswer.visibility = View.INVISIBLE
            multipleChoice.visibility = View.INVISIBLE
            radioGroup.visibility = View.VISIBLE
            radioGroup.orientation = LinearLayout.VERTICAL
            for (k in 0 until t[i].questionOptions!!.size){
               val button = RadioButton(this)
                button.id = View.generateViewId()
                Log.d("buttonid", button.id.toString())
                button.setText(t[i].questionOptions!![k])
                // button.setOnClickListener(this)
                radioGroup.addView(button)

            }
        }
        else if(choiceType == "Multiple Choice"){
            multipleChoice.removeAllViews()
            shortAnswer.visibility = View.INVISIBLE
            radioGroup.visibility = View.INVISIBLE
            multipleChoice.visibility = View.VISIBLE
            //multipleChoice = LinearLayout(this)
            multipleChoice.orientation = LinearLayout.VERTICAL
            if (t[i].questionOptions != null){
                for (k in t[i].questionOptions!!){
                    val box = CheckBox(this)
                    box.id = View.generateViewId()
                    Log.d("boxid", box.id.toString())
                    box.setText(k)
                    box.setTextColor(resources.getColor(R.color.black))
                    multipleChoice.addView(box)
//                    multiple = arrayListOf<String>()
//                    if (box.isChecked){
//                        multiple.add(k)
//                        Log.d("multipleadd", k)
//                        Log.d("multipleaddlist", multiple.size.toString())
//                    }
//
                }
            }

        }
//
//        if(i == t.size-1) {
//            quiz.setText("Submit for Assessment")
//        }
    }

}