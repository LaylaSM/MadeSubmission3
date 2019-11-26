package com.laylamac.madesubmission2.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.api.Api
import com.laylamac.madesubmission2.model.MovieMdl
import com.laylamac.madesubmission2.model.TvShowMdl
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    private lateinit var mCustomList: ImageView
    private lateinit var mFavList: ImageView
    private lateinit var mTrailer: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setActionBarTitle("Details")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pb_detail_activity.visibility = View.VISIBLE
        mCustomList = findViewById(R.id.iv_custom_list_detail)
        mFavList = findViewById(R.id.iv_fav_list_detail)
        mTrailer = findViewById(R.id.iv_trailer_detail)

        mCustomList.setOnClickListener {
            Toast.makeText(this, "Login to create and edit custom lists", Toast.LENGTH_SHORT).show()
        }

        mFavList.setOnClickListener {
            Toast.makeText(
                this,
                "Login to add this movie to your favorite list",
                Toast.LENGTH_SHORT
            ).show()
        }

        mTrailer.setOnClickListener {
            Toast.makeText(this, "Sorry, Unavailable yet", Toast.LENGTH_SHORT).show()
        }

        val type = intent.getStringExtra("extra_type")
        val params = RequestParams()
        val apiKey = Api()
        params.put("api_key", apiKey.API_KEY)
        params.put("language", "en-US")
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/discover/" + type

        if (type == "movie") {
            val id = intent.getStringExtra("extra_movie_id")
            client.get(url, params, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
                ) {
                    try {
                        val result = String(responseBody)
                        val responseJson = JSONObject(result)
                        val list = responseJson.getJSONArray("results")
                        for (i in 0 until list.length()) {
                            if (list.getJSONObject(i).getString("id") == id) {
                                val movie = MovieMdl(list.getJSONObject(i))
                                tv_title_detail.text = movie.title
                                Glide.with(applicationContext)
                                    .load("https://image.tmdb.org/t/p/w185" + movie.poster)
                                    .into(iv_poster_detail)
                                tv_release_date_detail.text = movie.release
                                tv_desc_detail.text = movie.description
                            }

                        }

                    } catch (e: Exception) {
                        Log.d("error", e.message!!)
                    }
                    pb_detail_activity.visibility = View.GONE
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Toast.makeText(applicationContext, error!!.message, Toast.LENGTH_SHORT).show()
                }

            })
        } else if (type == "tv") {
            val id = intent.getStringExtra("extra_tv_show_id")
            client.get(url, params, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
                ) {
                    try {
                        val result = String(responseBody)
                        val responseJson = JSONObject(result)
                        val json = responseJson.getJSONArray("results")
                        for (i in 0 until json.length()) {
                            if (json.getJSONObject(i).getString("id") == id) {
                                val tvShow = TvShowMdl(json.getJSONObject(i))
                                tv_title_detail.text = tvShow.title
                                Glide.with(applicationContext)
                                    .load("https://image.tmdb.org/t/p/w185" + tvShow.poster)
                                    .into(iv_poster_detail)
                                tv_release_date_detail.text = tvShow.release
                                tv_desc_detail.text = tvShow.description

                            }
                        }
                    } catch (e: Exception) {
                        Log.d("error", e.message!!)
                    }
                    pb_detail_activity.visibility = View.GONE
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Toast.makeText(applicationContext, error!!.message, Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Suppress("SameParameterValue")
    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = title
        }

    }
}
