package com.laylamac.madesubmission2.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.base.BaseAppCompatActivity
import com.laylamac.madesubmission2.view.TabAdapter
import com.laylamac.madesubmission2.view.movie.MovieFragment
import com.laylamac.madesubmission2.view.tvshow.TvShowFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseAppCompatActivity() {

    private var pageMovie: Fragment? = MovieFragment()
    private var pageTvShow : Fragment? = TvShowFragment()
    private var title = "Home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBarTitle(title)

        pageMovie = MovieFragment()
        pageTvShow = TvShowFragment()

        if (savedInstanceState == null) {
            val adapter = TabAdapter(supportFragmentManager)
            adapter.addFragment(pageMovie, getString(R.string.movies))
            adapter.addFragment(pageTvShow, getString(R.string.tv_shows))

            main_viewpager.adapter = adapter
            main_tab_layout.setupWithViewPager(main_viewpager)
        } else {
            title = savedInstanceState.getString(KEY_TITLE)!!
            setActionBarTitle(title)

            val adapter = TabAdapter(supportFragmentManager)
            pageMovie = adapter.getFragment(savedInstanceState, KEY_MOVIE_FRAGMENT)

            adapter.addFragment(pageMovie, "Movie")
            adapter.addFragment(pageTvShow, "TvShow")
            main_viewpager.adapter = adapter
            main_tab_layout.setupWithViewPager(main_viewpager)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.setClassName("com.android.settings", "com.android.settings.LanguageSettings")
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    // To maintain data that has been loaded when there is a change in orientation
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_TITLE, title)
        if (pageMovie?.isAdded!!) {
            supportFragmentManager.putFragment(outState, KEY_MOVIE_FRAGMENT, pageMovie!!)
        }

        if (pageTvShow?.isAdded!!){
            supportFragmentManager.putFragment(outState, KEY_TVSHOW_FRAGMENT, pageTvShow!!)
        }
        super.onSaveInstanceState(outState)
    }

    @Suppress("SameParameterValue")
    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = title
        }

    }
}
