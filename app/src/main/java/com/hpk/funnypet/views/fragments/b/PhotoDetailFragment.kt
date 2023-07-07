package com.hpk.funnypet.views.fragments.b

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hpk.funnypet.databinding.FragmentPhotoDetailBinding
import com.hpk.funnypet.databinding.FragmentPhotoListBinding
import com.hpk.funnypet.model.Photo
import com.hpk.funnypet.utils.BundleKey
import com.hpk.funnypet.views.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotoDetailFragment : BaseFragment<FragmentPhotoDetailBinding>() {
    private val viewModel: PhotoDetailViewModel by viewModel()
    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        binding: FragmentPhotoDetailBinding
    ) {
        val photo: Photo? = arguments?.getParcelable(BundleKey.KEY_ARG_PHOTO)
        photo?.let {
            binding.model = it
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPhotoDetailBinding {
        return FragmentPhotoDetailBinding.inflate(inflater, container, false)
    }
}