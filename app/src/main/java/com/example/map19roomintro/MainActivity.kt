package com.example.map19roomintro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Database
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MainActivity : AppCompatActivity() {

    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "shopping-items")
            .build()


        val item = Item(0, "Mjölk", false, "kyl" )


       // saveItem(item)
    }

    fun saveItem(item: Item) {
        GlobalScope.async(Dispatchers.IO) {   db.itemDao().insert(item) }
    }

}
