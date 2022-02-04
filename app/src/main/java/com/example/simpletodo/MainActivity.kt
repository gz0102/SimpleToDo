package com.example.simpletodo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import org.apache.commons.io.FileUtils;
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // remove the item from the list
                listOfTasks.removeAt(position)
                //notify the adapter that our data has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        //1. Detect when the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener{
//            //Code in here is going to be executed when user clicks on a button
//            Log.i("Test","User clicked on button")
//        }

        loadItems()

        //look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
         adapter = TaskItemAdapter(listOfTasks,onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up the button and input field, so that user can enter a task and add it to the list
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //Get a reference to the button
        //and the set an onclicklistener
        findViewById<Button>(R.id.button).setOnClickListener{
            // Grab the text the user has inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            // Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            //notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // Save the data that user has inputted
    // Save the data by writing and reading from a file

    // Create a method to get the file we need
    fun getDataFile() : File{

        //every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException : IOException){
            ioException.printStackTrace()
        }

    }

    //Save items by writing them into our data file
    fun saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }

    }
}