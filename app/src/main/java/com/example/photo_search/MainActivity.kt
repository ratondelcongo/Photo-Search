package com.example.photo_search

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photo_search.data.FlickrApi
import com.example.photo_search.data.Photo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.example.photo_search.ui.SearchFragment
import kotlinx.coroutines.*

class MainActivity : FragmentActivity(), View.OnClickListener, SearchFragment.OnSomeEventListener {

    private lateinit var galleryTitleTextView: TextView
    private lateinit var appLogoImageView: ImageView
    private lateinit var searchImageButton: ImageButton
    private lateinit var galleryRecycleView: RecyclerView
    private lateinit var photosRVAdapter: GalleryAdapter
    private lateinit var searchFragment: SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()

        getRecentPhotos()
    }

    private fun initUI() {
        galleryTitleTextView = findViewById(R.id.title)
        galleryTitleTextView.text = getString(R.string.trending_title)
        appLogoImageView = findViewById(R.id.app_logo)
        appLogoImageView.setImageResource(R.drawable.logo)
        searchImageButton = findViewById(R.id.search_button)
        galleryRecycleView = findViewById(R.id.idRVPhotos)

        searchImageButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        showSearchFragment();
    }

    private fun showSearchFragment() {
        searchFragment = SearchFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, searchFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun someEvent(query: String?) {
        if (query != null) {
            galleryTitleTextView.text = getString(R.string.search_result_title, query)
            getSearchedPhotos(query)
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadRecycleView(photoList: List<Photo>) {
        galleryRecycleView.layoutManager = GridLayoutManager(this, 3)
        photosRVAdapter = GalleryAdapter(photoList as ArrayList<Photo>)
        galleryRecycleView.adapter = photosRVAdapter
    }

    private fun getRecentPhotos() {
        val flickrApi = getRetrofit().create(FlickrApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val call = flickrApi.getRecentPhotos(
                API_GET_RECENT,
                API_KEY
            ).execute()

            val photoList = call.body()?.photos?.photo
            runBlocking {
                photoList?.map { it -> launch { it.setAdditionalData(flickrApi) } }
            }

            runOnUiThread {
                if (photoList != null) {
                    loadRecycleView(photoList)
                }
            }
        }
    }

    private fun getSearchedPhotos(text: String) {
        val flickrApi = getRetrofit().create(FlickrApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val call = flickrApi.searchPhotos(
                API_SEARCH,
                API_KEY,
                text
            ).execute()

            val photoList = call.body()?.photos?.photo
            runBlocking {
                photoList?.map { it -> launch { it.setAdditionalData(flickrApi) } }
            }

            runOnUiThread {
                if (photoList != null) {
                    loadRecycleView(photoList)
                }
            }
        }
    }
}