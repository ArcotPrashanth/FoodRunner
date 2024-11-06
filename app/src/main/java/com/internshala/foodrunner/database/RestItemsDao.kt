package com.internshala.foodrunner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/* Dao for accessing the data present inside the DB*/

@Dao
interface RestItemsDao {

    @Insert
    fun insertRestItem(restItemsEntity: RestItemsEntity)

    @Delete
    fun deleteRestItem(restItemsEntity: RestItemsEntity)

    @Query("SELECT * FROM restaurantItems")
    fun getAllRestItems(): List<RestItemsEntity>

    @Query("SELECT * FROM restaurantItems WHERE id = :resItemId")
    fun getRestItemById(resItemId: Int): RestItemsEntity
}