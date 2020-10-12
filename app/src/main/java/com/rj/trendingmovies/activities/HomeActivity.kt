package com.rj.trendingmovies.activities

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.rj.trendingmovies.R
import com.rj.trendingmovies.adaptors.MovieListAdaptor
import com.rj.trendingmovies.apiCall.MakeApiCall
import com.rj.trendingmovies.constants.Constants
import com.rj.trendingmovies.interfaces.CallBackInterface
import com.rj.trendingmovies.models.MovieDetail
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.empty_error_layout.*
import kotlinx.android.synthetic.main.loader_layout.*
import org.json.JSONObject


class HomeActivity : AppCompatActivity(), CallBackInterface {
    companion object {
        var arrMovieDetails: ArrayList<MovieDetail> = ArrayList()
    }

    private var pageNo: Int = 1
    private var gson = Gson()

    private lateinit var recyclerView: RecyclerView
    private var recyclerViewAdapter: MovieListAdaptor? = null
    private lateinit var refreshRecyclerView: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initialize()
        makeApiCall()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun sendResponse(response: String) {
        val jsonObject = JSONObject(response)
        if (jsonObject.has(Constants.API_RESP_RESULT_KEY)) {
            val moviesArr = jsonObject.get(Constants.API_RESP_RESULT_KEY)
            arrMovieDetails.addAll(
                gson.fromJson(
                    moviesArr.toString(),
                    Array<MovieDetail>::class.java
                ).toList()
            )

            setMovieAdaptor()
            toggleVisibilityOnState(2)
        } else if (jsonObject.has(Constants.API_RESP_ERROR)) {
            toggleVisibilityOnState(3)
        }
        refreshRecyclerView.isRefreshing = false
    }
    
    private fun initialize() {
        recyclerView = findViewById(R.id.ll_movie_list)
        refreshRecyclerView = findViewById(R.id.refreshList)
        refreshRecyclerView.setOnRefreshListener {
            arrMovieDetails.clear()
            pageNo = 1
            refreshRecyclerView.isRefreshing = true
            makeApiCall()
        }
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener)

    }

    private fun makeApiCall() {
        toggleVisibilityOnState(1)
        //trending/all/week?api_key=ac7c42d44f3b039d4c30dfdbffa168d3&page=2
        val url = Constants.API_TRENDING_URL_END_POINT +
                Constants.API_PARAMETER_API_KEY + Constants.API_KEY +
                "&" + Constants.API_PARAMETER_PAGE_NO + pageNo
        MakeApiCall.apiCall(
            null,
            url,
            this
        )
    }

    /**
     * @param mode : 1 - Show Loader
     *               2 - Show Recycler view list
     *               3 - Show Error state
     */
    private fun toggleVisibilityOnState(mode: Int) {
        when (mode) {
            1 -> {
                loadingPanel.visibility = View.VISIBLE
                ll_movie_list.visibility = View.GONE
                iv_error_empty.visibility = View.GONE
            }
            2 -> {
                ll_movie_list.visibility = View.VISIBLE
                iv_error_empty.visibility = View.GONE
                loadingPanel.visibility = View.GONE
            }
            3 -> {
                iv_error_empty.visibility = View.VISIBLE
                ll_movie_list.visibility = View.GONE
                loadingPanel.visibility = View.GONE
            }
        }
    }


    private fun setMovieAdaptor() {
        if (pageNo == 1) {
            recyclerView.layoutManager = GridLayoutManager(this, 2)
            recyclerViewAdapter =
                MovieListAdaptor(
                    this
                )
            recyclerView.adapter = recyclerViewAdapter
        }
        recyclerViewAdapter!!.notifyDataSetChanged()
    }

    private val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                if (layoutManager.findLastVisibleItemPosition() == arrMovieDetails.count() - 1) {
                    pageNo++
                    makeApiCall()
                }
            }
        }
}
//https://image.tmdb.org/t/p/original/wcaDIAG1QdXQLRaj4vC1EFdBT2.jpg
//https://image.tmdb.org/t/p/w500/wcaDIAG1QdXQLRaj4vC1EFdBT2.jpg

//https://api.themoviedb.org/3/trending/all/week?api_key=ac7c42d44f3b039d4c30dfdbffa168d3&page=2
//https://api.themoviedb.org/3/movie/48866?api_key=ac7c42d44f3b039d4c30dfdbffa168d3

//https://developers.themoviedb.org/3/trending/get-trending

//to get specific movie detail
//https://developers.themoviedb.org/3/movies/get-movie-details