package com.internshala.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.foodrunner.R
import com.internshala.foodrunner.activity.RestItemActivity
import com.internshala.foodrunner.database.RestItemsDatabase
import com.internshala.foodrunner.database.RestItemsEntity
import com.internshala.foodrunner.database.RestaurantDatabase
import com.internshala.foodrunner.database.RestaurantEntity
import com.internshala.foodrunner.model.Rest
import com.internshala.foodrunner.model.RestItem
import com.squareup.picasso.Picasso

class RestItemRecyclerAdapter(val itemList: ArrayList<RestItem>, val context: Context) :
    RecyclerView.Adapter<RestItemRecyclerAdapter.RestItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_rest_item_single_row, parent, false)
        return RestItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(itemHolder: RestItemViewHolder, position: Int) {
        val restItem = itemList.get(position)
        itemHolder.txtRestItemName.text = restItem.name
        val cost = "${restItem.costForOne.toString()}/person"
        itemHolder.cost.text = cost
        itemHolder.txtRestIemId.text = ((restItem.id % restItem.restaurant_id).toString())
        //holder.imgBookImage.setImageResource(book.bookImage)

        itemHolder.btnAdd.setOnClickListener {
            val restItemsEntity = RestItemsEntity(
                restItem.id,
                restItem.name,
                restItem.costForOne,
                restItem.restaurant_id
            )

            if (!DBAsyncTask(context, restItemsEntity, 1).execute().get()) {
                val async =
                    DBAsyncTask(context, restItemsEntity, 2).execute()
                val result = async.get()
                if (result) {
                    itemHolder.btnAdd.visibility = View.VISIBLE
                }
            } else {
                val async = DBAsyncTask(context, restItemsEntity, 3).execute()
                val result = async.get()

                if (result) {
                    itemHolder.btnAdd.visibility = View.GONE
                }
            }
        }
    }

    class RestItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtRestItemName = view.findViewById(R.id.txtRestItemName) as TextView
        val cost = view.findViewById(R.id.txtRICostForOne) as TextView
        val btnAdd = view.findViewById(R.id.btnAddToCart) as Button
        val txtRestIemId = view.findViewById(R.id.txtRestItem) as TextView
        val cardRestItem = view.findViewById(R.id.cardRestItem) as CardView
    }

    class DBAsyncTask(context: Context, val restItemsEntity: RestItemsEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val dbs =
            Room.databaseBuilder(context, RestItemsDatabase::class.java, "res-item-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {
                    val resItem: RestItemsEntity =
                        dbs.restItemsDao().getRestItemById(restItemsEntity.id)
                    dbs.close()
                    return resItem != null
                }

                2 -> {
                    dbs.restItemsDao().insertRestItem(restItemsEntity)
                    dbs.close()
                    return true
                }

                3 -> {
                    dbs.restItemsDao().deleteRestItem(restItemsEntity)
                    dbs.close()
                    return true
                }
            }
            return false
        }

    }
}
