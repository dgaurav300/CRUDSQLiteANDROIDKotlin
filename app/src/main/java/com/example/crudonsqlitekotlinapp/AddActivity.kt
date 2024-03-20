package com.example.crudonsqlitekotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat

class AddActivity : AppCompatActivity() {
    var dbHandler: DatabaseHandler? = null
    var isEditMode = false
    lateinit var btn_delete:AppCompatButton
    lateinit var input_name:EditText
    lateinit var input_desc:EditText
lateinit var swt_completed:SwitchCompat
lateinit var btn_save:AppCompatButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        btn_delete=findViewById(R.id.btn_delete)
        input_name=findViewById(R.id.input_name)
        input_desc=findViewById(R.id.input_desc)
        swt_completed=findViewById(R.id.swt_completed)
        btn_save=findViewById(R.id.btn_save)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initDB()
        initOperations()

    }

    private fun initDB() {
        dbHandler = DatabaseHandler(this)
        btn_delete.visibility = View.INVISIBLE
        if (intent != null && intent.getStringExtra("Mode") == "E") {
            isEditMode = true
            val tasks: Tasks = dbHandler!!.getTask(intent.getIntExtra("Id",0))
            input_name.setText(tasks.name)
            input_desc.setText(tasks.desc)
            swt_completed.isChecked = tasks.completed == "Y"
            btn_delete.visibility = View.VISIBLE
        }
    }

    private fun initOperations() {
        btn_save.setOnClickListener({
            var success: Boolean = false
            if (!isEditMode) {
                val tasks: Tasks = Tasks()
                tasks.name = input_name.text.toString()
                tasks.desc = input_desc.text.toString()
                if (swt_completed.isChecked)
                    tasks.completed = "Y"
                else
                    tasks.completed = "N"
                success = dbHandler?.addTask(tasks) as Boolean
            } else {
                val tasks: Tasks = Tasks()
                tasks.id = intent.getIntExtra("Id", 0)
                tasks.name = input_name.text.toString()
                tasks.desc = input_desc.text.toString()
                if (swt_completed.isChecked)
                    tasks.completed = "Y"
                else
                    tasks.completed = "N"
                success = dbHandler?.updateTask(tasks) as Boolean
            }

           // if (success)
                //finish()
        })

        btn_delete.setOnClickListener({
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click 'YES' Delete the Task.")
                .setPositiveButton("YES", { dialog, i ->
                    val success = dbHandler?.deleteTask(intent.getIntExtra("Id", 0)) as Boolean
                    if (success)
                        finish()
                    dialog.dismiss()
                })
                .setNegativeButton("NO", { dialog, i ->
                    dialog.dismiss()
                })
            dialog.show()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}