package com.rj.trendingmovies.adaptors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.DiskCache
import com.rj.trendingmovies.R
import com.rj.trendingmovies.activities.HomeActivity
import com.rj.trendingmovies.activities.MovieDetailsActivity
import com.rj.trendingmovies.constants.Constants
import com.rj.trendingmovies.models.MovieDetail
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.item_movie_detail.view.*
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


class MovieListAdaptor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context

    constructor(context: Context) : this() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieListHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie_detail, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return HomeActivity.arrMovieDetails.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(Constants.INTENT_MOVE_POSITION_KEY , position)
            context.startActivity(intent)
            (context as AppCompatActivity).overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }

        if (HomeActivity.arrMovieDetails[position].original_title != null) {
            holder.itemView.tv_id_movie_name.text = HomeActivity.arrMovieDetails[position].original_title
        } else {
            holder.itemView.tv_id_movie_name.text = HomeActivity.arrMovieDetails[position].original_name
        }

        if(HomeActivity.arrMovieDetails[position].vote_average != null) {
            holder.itemView.tv_id_rating.text = HomeActivity.arrMovieDetails[position].vote_average.toString()
        } else{
            holder.itemView.tv_id_rating.visibility = View.GONE
        }

        val imageFinalUrl = Constants.API_END_POINT_IMAGE +
                Constants.adaptor_image_quality +
                HomeActivity.arrMovieDetails[position].poster_path
        Glide.with(context)
            .load(imageFinalUrl)
            .placeholder(R.drawable.place_holder_white)
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(holder.itemView.iv_id_movie_image)


    }



    class MovieListHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}