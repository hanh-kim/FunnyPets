package com.hpk.funnypet.views.fragments.b

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.lifecycleScope
import com.hpk.funnypet.databinding.FragmentPhotoDetailBinding
import com.hpk.funnypet.extentions.observe
import com.hpk.funnypet.model.Photo
import com.hpk.funnypet.utils.BundleKey
import com.hpk.funnypet.utils.Constants
import com.hpk.funnypet.utils.GlideUtil
import com.hpk.funnypet.views.base.BaseFragment
import fadeInOut
import loadImage
import loadImageUrl
import onAvoidDoubleClick
import org.koin.androidx.viewmodel.ext.android.viewModel
import shareImage
import java.text.MessageFormat

class PhotoDetailFragment : BaseFragment<FragmentPhotoDetailBinding>() {
    private val viewModel: PhotoDetailViewModel by viewModel()
    private var isHideControl = false

    private var onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Download complete
            // Check if the broadcast message is for our enqueued download
            val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (referenceId != -1L) {
                binding?.viewToastSuccess?.fadeInOut(lifecycleScope, 2000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.registerReceiver(
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(onDownloadComplete)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPhotoDetailBinding {
        return FragmentPhotoDetailBinding.inflate(inflater, container, false)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        binding: FragmentPhotoDetailBinding
    ) {
        val photo: Photo? = arguments?.getParcelable(BundleKey.KEY_ARG_PHOTO)
        viewModel.photo = photo
        photo?.let {
            binding.title = it.title
            binding.imgUrl = it.getUrl()
        }

        with(viewModel) {
            observe(saveImage) {
                if (it == true) {
                    hideProgressBar(progressBar = binding.progressBar)
                    binding.viewToastSuccess.fadeInOut(lifecycleScope, 2000)
                }
            }
        }

        binding.apply {
            header.tvTitle.isSelected = true
            header.tvTitle.isSingleLine = true
        }

        initListener()
    }

    private fun initListener() {
        binding?.apply {

            header.btnBack.setOnClickListener {
                backPressed()
            }

            footer.btnShare.onAvoidDoubleClick {
                GlideUtil.getImageBitmap(
                    requireContext(), viewModel.photo?.getUrl()
                ) { bitmap ->
                    activity?.shareImage(bitmap)
                }

            }
            footer.btnDownload.onAvoidDoubleClick {
                onDownloadPhoto()
            }
        }
    }

    private fun onDownloadPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            onPermissionGranted()
        } else {
            //requestPermission
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result: Map<String, Boolean> ->
            val deniedList: List<String> = result.filter { !it.value }.map { it.key }
            when {
                deniedList.isNotEmpty() -> {}
                else -> {
                    onPermissionGranted()
                }
            }
        }

    private fun onPermissionGranted() {
        viewModel.photo?.getUrl()?.let {
            val fileName =
                MessageFormat.format(Constants.PHOTO_DOWNLOAD_NAME, System.currentTimeMillis())
            viewModel.saveImage(
                url = it,
                name = fileName,
                description = viewModel.photo?.title ?: ""
            )
        }
        GlideUtil.getImageBitmap(requireContext(), viewModel.photo?.getUrl()) { bitmap ->
            binding?.apply {
//                showProgressBar(progressBar)
//                viewModel.saveImage(bitmap = bitmap, "hpk_${System.currentTimeMillis()}")
            }
        }
    }
}