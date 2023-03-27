package com.example.myshoppal

import android.os.Binder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class MainActivity : AppCompatActivity() {

   // private lateinit var binding : ActivityMainBinding
    //private lateinit var database : DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        }
        setContentView(R.layout.activity_main)


        // get reference to button
        val btn = findViewById<Button>(R.id.button)
        val textInput = findViewById<EditText>(R.id.editTextTextMultiLine)

        // set on-click listener
        btn.setOnClickListener {
            // your code to perform when the user clicks on the button
            // Write a message to the database
            //val database = Firebase.database
            //val myRef = database.getReference("journal")

            val database=FirebaseDatabase.getInstance();
            val myRef = database.getReference()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS")
            val current = LocalDateTime.now().format(formatter)
            myRef.child("datas").child(current.toString()).setValue(textInput.text.toString())
            textInput.text.clear()

        }



    }
}