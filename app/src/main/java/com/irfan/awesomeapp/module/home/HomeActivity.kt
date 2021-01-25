package com.irfan.awesomeapp.module.home

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.irfan.awesomeapp.R
import com.irfan.awesomeapp.base.BaseActivity
import com.irfan.awesomeapp.data.model.ObjectPhoto
import com.irfan.awesomeapp.data.model.Photos
import com.irfan.awesomeapp.databinding.ActivityHomeBinding
import com.irfan.awesomeapp.module.detail.DetailActivity
import com.irfan.awesomeapp.module.home.adapter.PhotoListAdapter
import com.irfan.awesomeapp.utils.Constants
import com.irfan.awesomeapp.utils.load
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.queryLast
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.startActivity


@Suppress("UNCHECKED_CAST")
class HomeActivity : BaseActivity() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var skeleton: Skeleton
    private var layoutManager: GridLayoutManager? = null
    private var adapter: PhotoListAdapter? = null
    private var page: Int = 1
    private var checkAvailablePhoto: List<Photos> = emptyList()
    private var isLoadMore: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        /**
         * Setup toolbar
         */
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        /**
         * Init layout manager
         * recyclerview
         */
        layoutManager = GridLayoutManager(this, 1)
        recyclerview_photos.layoutManager = layoutManager
        adapter = PhotoListAdapter(
            layoutManager = layoutManager,
            callback = object : PhotoListAdapter.PhotoItemCallback {
                override fun onClickItem(data: Photos) {
                    startActivity<DetailActivity>(Constants.BUNDLE_PHOTOS to data)
                }

            })

        recyclerview_photos.adapter = adapter

        /**
         * Setup skeleton loading
         */
        skeleton = recyclerview_photos.applySkeleton(R.layout.item_list_photo, 5)
        skeleton.shimmerColor = Color.LTGRAY

        requestPhotos()
        observerViewModel()
        scrollingList()
    }

    /**
     * Request to view model
     * for getting data
     */
    private fun requestPhotos() {
        if (!isLoadMore) {
            skeleton.showSkeleton()
        }
        viewModel.getPhotos(page = page)
    }

    /**
     * Observer view model
     * live data
     */
    private fun observerViewModel() {
        viewModel.formResult.observe(this, Observer {
            val result = it ?: return@Observer

            skeleton.showOriginal()
            hideLoading()
            isLoadMore = false

            if (result.isSuccess) {
                val photos = result.data as ObjectPhoto
                page = photos.page
                checkAvailablePhoto = photos.photos

                setupPhotoList()
            } else {
                showError(result.error)
            }
        })
    }

    /**
     * Setup data
     * to recyclerview
     */
    private fun setupPhotoList() {
        val data = Photos().queryAll()
        val image = data.lastOrNull()?.src?.landscape
        imageview_cover.load(image ?: "")

        if (data.isNullOrEmpty()) {
            view_container.let {
                addEmptyList(
                    it,
                    R.drawable.icon_empty,
                    getString(R.string.wah_kosong),
                    getString(R.string.tidak_ada_photo)
                )
            }
        } else {
            view_container.let {
                removeEmptyList(it)
            }
        }
        adapter?.refreshAdapter(data as ArrayList<Photos>)
    }

    /**
     * When scrolling recyclerview
     * read last of list
     */
    private fun scrollingList() {
        nested_scrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1)
                        .measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    val visibleItemCount = layoutManager?.childCount
                    val totalItemCount = layoutManager?.itemCount
                    val pastVisiblesItems = layoutManager?.findFirstVisibleItemPosition()
                    if (!isLoadMore && checkAvailablePhoto.isNotEmpty()) {
                        if (visibleItemCount != null) {
                            if (visibleItemCount + pastVisiblesItems!! >= totalItemCount!!) {
                                isLoadMore = true
                                showLoading()
                                page = page.plus(1)
                                requestPhotos()
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    /**
     * Option menu selected
     * menu grid or list
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_grid -> {
                layoutManager?.spanCount = 3
            }
            else -> {
                layoutManager?.spanCount = 1
            }

        }

        adapter?.notifyItemRangeChanged(0, adapter?.itemCount ?: 0)
        return super.onOptionsItemSelected(item)
    }
}