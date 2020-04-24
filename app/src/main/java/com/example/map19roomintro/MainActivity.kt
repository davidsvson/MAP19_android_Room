package com.example.map19roomintro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Database
import androidx.room.Room
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "shopping-items")
            .build()


        val item = Item(0, "Smör", false, "kyl" )
        val item2 = Item(0, "ost", false, "kyl" )
        val item3 = Item(0, "bönor", false, "grönsak" )
/*
        saveItem(item)
        saveItem(item2)
        saveItem(item3)
*/
        val items = loadAllItems()
        val itemsByCat = loadByCategory("grönsak")

        GlobalScope.launch {
            itemsByCat.await().forEach {
                println("!!! ${it.name}")
            }
        }
    }

    fun saveItem(item: Item) {
        GlobalScope.async(Dispatchers.IO) {   db.itemDao().insert(item) }
    }

    fun loadAllItems() : Deferred<List<Item>>  =
         GlobalScope.async(Dispatchers.IO) {
            db.itemDao().getAll()
        }

    fun loadByCategory(category: String) :  Deferred<List<Item>>  =
        GlobalScope.async(Dispatchers.IO) {
            db.itemDao().findByCategory(category)
        }






}
