package com.internshala.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.foodrunner.R
import com.internshala.foodrunner.activity.RestItemActivity
import com.internshala.foodrunner.database.RestaurantDatabase
import com.internshala.foodrunner.database.RestaurantEntity
import com.internshala.foodrunner.model.Rest
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val itemList: ArrayList<Rest>, val context: Context) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_single_row, parent, false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val rest = itemList.get(position)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.resThumbnail.clipToOutline = true
        }
        holder.restaurantName.text = rest.name
        holder.rating.text = rest.rating
        val cost = "${rest.cost_for_one.toString()}/person"
        holder.cost.text = cost
        //holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(rest.image_url).error(R.drawable.default_res_cover)
            .into(holder.resThumbnail)

        val listOfFavourites = GetAllFavAsyncTask(context).execute().get()

        if (listOfFavourites.isNotEmpty() && listOfFavourites.contains(rest.id.toString())) {
            holder.favImage.setImageResource(R.drawable.ic_action_fav_checked)
        } else {
            holder.favImage.setImageResource(R.drawable.ic_action_fav)
        }

        holder.favImage.setOnClickListener {
            val restaurantEntity = RestaurantEntity(
                rest.id,
                rest.name,
                rest.rating,
                rest.cost_for_one.toString(),
                rest.image_url
            )

            if (!DBAsyncTask(context, restaurantEntity, 1).execute().get()) {
                val async =
                    DBAsyncTask(context, restaurantEntity, 2).execute()
                val result = async.get()
                if (result) {
                    holder.favImage.setImageResource(R.drawable.ic_action_fav_checked)
                }
            } else {
                val async = DBAsyncTask(context, restaurantEntity, 3).execute()
                val result = async.get()

                if (result) {
                    holder.favImage.setImageResource(R.drawable.ic_action_fav)
                }
            }

            holder.cardRestaurant.setOnClickListener {
                val intent = Intent(context, RestItemActivity::class.java)
                intent.putExtra("id", rest.id)
                context.startActivity(intent)
            }
        }
    }

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val resThumbnail = view.findViewById(R.id.imgRestaurantThumbnail) as ImageView
        val restaurantName = view.findViewById(R.id.txtRestaurantName) as TextView
        val rating = view.findViewById(R.id.txtRestaurantRating) as TextView
        val cost = view.findViewById(R.id.txtCostForTwo) as TextView
        val cardRestaurant = view.findViewById(R.id.cardRestaurant) as CardView
        val favImage = view.findViewById(R.id.imgIsFav) as ImageView

    }

    class DBAsyncTask(context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "res-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {
                    val res: RestaurantEntity? =
                        db.restaurantDao().getRestaurantById(restaurantEntity.id.toString())
                    db.close()
                    return res != null
                }

                2 -> {
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }

                3 -> {
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
            }

            return false
        }

    }

    class GetAllFavAsyncTask(
        context: Context
    ) :
        AsyncTask<Void, Void, List<String>>() {

        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "res-db").build()
        override fun doInBackground(vararg params: Void?): List<String> {

            val list = db.restaurantDao().getAllRestaurants()
            val listOfIds = arrayListOf<String>()
            for (i in list) {
                listOfIds.add(i.id.toString())
            }
            return listOfIds
        }
    }
}