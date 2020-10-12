package com.rj.trendingmovies.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.rj.trendingmovies.R
import com.rj.trendingmovies.constants.Constants
import com.rj.trendingmovies.models.MovieDetail
import kotlinx.android.synthetic.main.activity_movie_details.*


class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var oMovieDetails: MovieDetail
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Get Intent Extras Data
        fnCheckAndSetIntentExtras()
        setContentView(R.layout.activity_movie_details)
        supportActionBar?.hide()
        initialize()


    }

    private fun fnCheckAndSetIntentExtras() {
        val moviePosition = intent.extras?.get(Constants.INTENT_MOVE_POSITION_KEY)
        oMovieDetails = HomeActivity.arrMovieDetails[moviePosition as Int]
    }

    private fun initialize() {
        try {
            if (oMovieDetails.original_title != null) {
                tv_id_movie_title.text = oMovieDetails.original_title
            } else {
                tv_id_movie_title.text = oMovieDetails.original_name
            }

            tv_id_imdb.text = oMovieDetails.vote_average.toString()
            tv_id_description.text = oMovieDetails.overview
            tv_id_type.text = oMovieDetails.media_type
            tv_id_language.text = oMovieDetails.original_language
            if(oMovieDetails.release_date != null){
                tv_id_release_date.text = oMovieDetails.release_date
            } else {
                tv_id_release_date.text = "NA"
            }


            val imageFinalUrl = Constants.API_END_POINT_IMAGE +
                    Constants.activity_image_quality +
                    oMovieDetails.poster_path

            Glide.with(this)
                .load(imageFinalUrl)
                .placeholder(R.drawable.place_holder_white)
                .priority(Priority.IMMEDIATE)
                .into(iv_movie_poster)
        } catch (e: Exception) {
            Log.d(this.localClassName, e.printStackTrace().toString())
        }
    }

    override fun onPause() {
        super.onPause()
        if(isFinishing) {
            this.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }
    }

}
