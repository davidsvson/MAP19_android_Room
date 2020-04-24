package com.example.map19roomintro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Database
import androidx.room.Room
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() , CoroutineScope {

    private lateinit var job : Job
    override val coroutineContext : CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "shopping-items")
            .fallbackToDestructiveMigration()
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

        // nu används vår activity som scope eftersom den implemneterar CoroutineScope
        // i stället kan vi använda GlobalScope i så fall skriver GlobalScope.launch i stället
        // men rekomendationen är att vara restrektiv med att använda globalscope
       launch {
            itemsByCat.await().forEach {
                println("!!! ${it.name}")
            }
        }
    }

    fun saveItem(item: Item) {
        async(Dispatchers.IO) {   db.itemDao().insert(item) }
    }

    fun loadAllItems() : Deferred<List<Item>>  =
         async(Dispatchers.IO) {
            db.itemDao().getAll()
        }

    fun loadByCategory(category: String) :  Deferred<List<Item>>  =
        async(Dispatchers.IO) {
            db.itemDao().findByCategory(category)
        }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()

    }

}
