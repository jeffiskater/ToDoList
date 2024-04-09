package com.example.hw1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val dataSet: MutableList<Task>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // Set the view of task name, time and delete button.
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
        val selectTimeTextView: TextView = view.findViewById(R.id.selectTimeTextView)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener(position)
                }
            }
        }
    }

    // Create a new ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    // Bind data to the ViewHolder.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = dataSet[position]
        holder.textView.text = task.name

        //If the task doesn't have a set time, make this view invisible.
        if (task.selectTime != "SELECT TIME") {
            holder.selectTimeTextView.visibility = View.VISIBLE
            holder.selectTimeTextView.text = task.selectTime
        } else {
            holder.selectTimeTextView.visibility = View.GONE
        }

        // Set the click event for the delete button.
        holder.deleteButton.setOnClickListener {
            dataSet.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // Return the size of the dataset.
    override fun getItemCount(): Int {
        return dataSet.size
    }
}

