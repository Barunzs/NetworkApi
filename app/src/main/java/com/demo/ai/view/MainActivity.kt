package com.demo.ai.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.demo.ai.R
import com.demo.ai.model.Media
import com.demo.ai.model.Movie
import com.demo.networklibrary.ApiService

import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.canonicalName

    //https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=325a2832e37f32ffdf4b2c29749922d8
    //https://api.themoviedb.org/3/movie/550?api_key=325a2832e37f32ffdf4b2c29749922d8
    val BASEURL = "https://api.themoviedb.org/"
    val END_POINT =
        "3/discover/movie?sort_by=popularity.desc&api_key=325a2832e37f32ffdf4b2c29749922d8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            try {
                ApiService<Movie>().getDataFromServer(
                    END_POINT,
                    Movie::class.java,
                    Movie(),
                    BASEURL
                )?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : Subscriber<Movie>() {
                        override fun onNext(media: Movie?) {
                            for (movie in media?.results!!) {
                                Log.d(
                                    TAG,
                                    "onNext::${movie.title}"
                                )
                            }

                        }

                        override fun onCompleted() {
                            Log.d(TAG, "onCompleted")
                        }

                        override fun onError(e: Throwable?) {
                            Log.e(TAG, "onError${e?.message}")
                        }
                    })

            } catch (ex: Exception) {
                Log.e(TAG, "Exception::${ex.message}")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
