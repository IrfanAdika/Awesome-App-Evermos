package com.irfan.awesomeapp.module.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.irfan.awesomeapp.R
import com.irfan.awesomeapp.base.BaseActivity
import com.irfan.awesomeapp.data.model.Photos
import com.irfan.awesomeapp.databinding.ActivityDetailBinding
import com.irfan.awesomeapp.utils.Constants
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : BaseActivity() {
    private var viewBinding: ActivityDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        if (intent.extras != null) {
            val photo = intent.getSerializableExtra(Constants.BUNDLE_PHOTOS) as? Photos
            setupDetail(photo = photo)

            label_detail_url.setOnClickListener {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(photo?.url))
                startActivity(browserIntent)
            }
        }

    }

    private fun setupDetail(photo: Photos?) {
        viewBinding?.photo = photo
    }
}