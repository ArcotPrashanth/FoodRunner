package com.internshala.foodrunner.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestItemsEntity::class], version = 1)
abstract class RestItemsDatabase : RoomDatabase() {

    abstract fun restItemsDao(): RestItemsDao

}
