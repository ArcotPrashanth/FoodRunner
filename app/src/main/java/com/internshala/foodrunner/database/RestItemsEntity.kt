package com.internshala.foodrunner.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurantItems")
data class RestItemsEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "cost_for_one") val costForOne: Int,
    @ColumnInfo(name = "restaurant_id") val restaurantId: Int
)
