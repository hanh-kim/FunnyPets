package com.hpk.funnypet.views.fragments.a

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.hpk.funnypet.databinding.FragmentPhotoListBinding
import com.hpk.funnypet.extentions.observe
import com.hpk.funnypet.views.adapters.PhotoPagingAdapter
import com.hpk.funnypet.views.adapters.PhotoPagingAdapter.Companion.LOADING_ITEM
import com.hpk.funnypet.views.base.BaseFragment
import com.hpk.funnypet.views.others.LoadStateFooterAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotoListFragment : BaseFragment<FragmentPhotoListBinding>() {
    private val viewModel: PhotoListViewModel by viewModel()
    private val adapter = PhotoPagingAdapter()
    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        binding: FragmentPhotoListBinding
    ) {
        binding.apply {
            val layout = GridLayoutManager(requireContext(), 2)
            layout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.getItemViewType(position) == LOADING_ITEM) 1 else 2
                }
            }
            binding.rcvPhotos.layoutManager = layout
            rcvPhotos.adapter = adapter.withLoadStateFooter(LoadStateFooterAdapter())
        }
        with(viewModel) {
            observe(photoListLD){
                it?.let {
                    adapter.submitData(lifecycle, it)
                }
            }
        }

        adapter.onItemClickListener = {
            Log.v("kkkkk", "photo: ${it.title}\n-url: ${it.getUrl()}")
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPhotoListBinding {
        return FragmentPhotoListBinding.inflate(inflater, container, false)
    }
}