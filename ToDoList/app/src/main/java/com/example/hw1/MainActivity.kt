package com.example.hw1

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var dialogLayout: LinearLayout
    private lateinit var taskNameEditText: EditText
    private lateinit var taskDescriptionEditText: EditText

    private lateinit var adapter: MyAdapter
    private val dataSet = mutableListOf<Task>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        // Create an adapter and set up the click event handling function.
        adapter = MyAdapter(dataSet) { position ->
            showNewTaskDialog(false, position)
        }

        //Set the RecyclerView's layout manager and adapter.
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val newTaskButton = findViewById<Button>(R.id.new_task)
        newTaskButton.setOnClickListener {
            showNewTaskDialog(true, -1)
        }
    }

    private fun showNewTaskDialog(isNewTask: Boolean, position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
        val dialogBuilder = AlertDialog.Builder(this)

        // Determine the dialog title and button text based on whether it is a new task
        if (isNewTask) {
            dialogBuilder.setTitle("New Task")
        } else {
            dialogBuilder.setTitle("Edit Task")
            // Populate the current task's name, description, and time into the EditText and Button.
            val task = dataSet[position]
            dialogView.findViewById<EditText>(R.id.task_name).setText(task.name)
            dialogView.findViewById<EditText>(R.id.task_description).setText(task.description)
            dialogView.findViewById<Button>(R.id.select_time).text = task.selectTime
        }

        dialogBuilder.setView(dialogView)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        // Retrieve the EditText and buttons from the dialog.
        taskNameEditText = dialogView.findViewById(R.id.task_name)
        taskDescriptionEditText = dialogView.findViewById(R.id.task_description)
        val selectTimeButton: Button = dialogView.findViewById(R.id.select_time)
        val saveButton: Button = dialogView.findViewById(R.id.save)

        selectTimeButton.setOnClickListener {
            // Clicking the SELECT TIME button will prompt a Time Picker Dialog.
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    // Handle the selected time, update the button text, and the task's selectTime.
                    val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    selectTimeButton.text = selectedTime // update the button text
                    if (!isNewTask) {
                        // If it's an edited task, update the task's selectTime.
                        val editedTask = dataSet[position]
                        editedTask.selectTime = selectedTime
                    }
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }

        saveButton.setOnClickListener {
            val taskName = taskNameEditText.text.toString()
            val taskDescription = taskDescriptionEditText.text.toString()
            if (taskName.isNotEmpty()) {
                if (isNewTask) {
                    // create new task
                    val newTask = Task(taskName, taskDescription, selectTimeButton.text.toString())
                    dataSet.add(newTask)
                } else {
                    // edit task
                    val editedTask = dataSet[position]
                    editedTask.name = taskName
                    editedTask.description = taskDescription
                }
                adapter.notifyDataSetChanged()
                taskNameEditText.text.clear()
                taskDescriptionEditText.text.clear()
                alertDialog.dismiss() // turn off the dialog after clicking the save button
            }
        }
    }

}


