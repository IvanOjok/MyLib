package com.example.mylib.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylib.R
import com.example.mylib.model.Submission
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class QuizAdapter(r: Context, options: List<Submission>) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>(){
    var y = options
    var c = r

    class QuizViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {
        val question = itemView.findViewById<TextView>(R.id.question)
        val choice = itemView.findViewById<TextView>(R.id.choice)
        val questionImage = itemView.findViewById<ImageView>(R.id.image)
        val radioGroup = itemView.findViewById<RadioGroup>(R.id.radioGroup)
        val multipleChoice = itemView.findViewById<LinearLayout>(R.id.multipleChoice)
        //oneAnswer = findViewById<RecyclerView>(R.id.oneAnswer)
        val shortAnswer = itemView.findViewById<EditText>(R.id.shortAnswer)
        val image = itemView.findViewById<ImageView>(R.id.image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.quiz_layout_list, parent, false)
        return QuizViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {

        holder.question.text = y[position].quiz.question
        holder.choice.text = "${y[position].quiz.choice} \n (${y[position].quiz.choiceType})"

        if (y[position].quiz.mediaType == "image"){
            Glide.with(c).load(y[position].quiz.media).into(holder.questionImage)
        }


        val choiceType = y[position].quiz.choiceType
        if (choiceType == "Short Answer"){
            holder.radioGroup.removeAllViews()
            holder.multipleChoice.removeAllViews()
            holder.shortAnswer.visibility = View.VISIBLE
            holder.radioGroup.visibility = View.INVISIBLE
            holder.multipleChoice.visibility = View.INVISIBLE
            Log.d("short", choiceType)

        }
        else if(choiceType == "One Answer"){
            holder.radioGroup.removeAllViews()
            holder.shortAnswer.visibility = View.INVISIBLE
            holder.multipleChoice.visibility = View.INVISIBLE
            holder.radioGroup.visibility = View.VISIBLE
            holder.radioGroup.orientation = LinearLayout.VERTICAL
            for (k in 0 until y[position].quiz.questionOptions!!.size){
                val button = RadioButton(c)
                button.id = View.generateViewId()
                Log.d("buttonid", button.id.toString())
                button.setText(y[position].quiz.questionOptions!![k])
                // button.setOnClickListener(this)
                holder.radioGroup.addView(button)

            }
        }
        else if(choiceType == "Multiple Choice"){
            holder.multipleChoice.removeAllViews()
            holder.shortAnswer.visibility = View.INVISIBLE
            holder.radioGroup.visibility = View.INVISIBLE
            holder.multipleChoice.visibility = View.VISIBLE
            //multipleChoice = LinearLayout(this)
            holder.multipleChoice.orientation = LinearLayout.VERTICAL
            if (y[position].quiz.questionOptions != null){
                for (k in y[position].quiz.questionOptions!!){
                    val box = CheckBox(c)
                    box.id = View.generateViewId()
                    Log.d("boxid", box.id.toString())
                    box.setText(k)
                    box.setTextColor(c.resources.getColor(R.color.black))
                    holder.multipleChoice.addView(box)
//
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return  y.size
    }

}