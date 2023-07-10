package com.hpk.funnypet.views.fragments.a

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.hpk.funnypet.R
import com.hpk.funnypet.databinding.FragmentPhotoListBinding
import com.hpk.funnypet.extentions.observe
import com.hpk.funnypet.utils.BundleKey
import com.hpk.funnypet.views.adapters.PhotoPagingAdapter
import com.hpk.funnypet.views.adapters.PhotoPagingAdapter.Companion.LOADING_ITEM
import com.hpk.funnypet.views.base.BaseFragment
import com.hpk.funnypet.views.fragments.b.PhotoDetailFragment
import com.hpk.funnypet.views.others.LoadStateFooterAdapter
import gone
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import visible

class PhotoListFragment : BaseFragment<FragmentPhotoListBinding>() {
    private val viewModel: PhotoListViewModel by viewModel()
    private val adapter = PhotoPagingAdapter()
    private val historyAdapter = PhotoPagingAdapter(true)
    private var viewSkeleton: Skeleton? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSkeleton()
    }
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
            rcvPhotos.layoutManager = layout
            rcvPhotoHistory.layoutManager = GridLayoutManager(requireContext(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter.getItemViewType(position) == LOADING_ITEM) 1 else 2
                    }
                }
            }
            rcvPhotos.adapter = adapter.withLoadStateFooter(LoadStateFooterAdapter())
            rcvPhotoHistory.adapter = historyAdapter
            refreshLayout.setOnRefreshListener {
                adapter.refresh()
            }
            viewBtnGroup.onSelectChanged = {
                refreshLayout.isEnabled = it == 0
                when (it) {
                    0 -> {
                        rcvPhotos.visible()
                        rcvPhotoHistory.gone()
                    }
                    else -> {
                        viewModel.getHistory()
                        rcvPhotoHistory.visible()
                        rcvPhotos.gone()
                    }
                }
            }

            lifecycleScope.launch {
                adapter.loadStateFlow.map { it.refresh }
                    .distinctUntilChanged()
                    .collect {
                        if (it is LoadState.NotLoading) {
                            refreshLayout.isRefreshing = false
                            showOrigin()
                            (binding.rcvPhotos.layoutManager as? GridLayoutManager)?.scrollToPositionWithOffset(
                                0,
                                0
                            )
                        }
                    }
            }
        }
        with(viewModel) {
            getHistory()
            observe(photoListLD) {
                it?.let {
                    adapter.submitData(lifecycle, it)
                }
            }

            observe(photoHistoryLD) {
                it?.let {
                    historyAdapter.submitData(lifecycle, PagingData.from(it))
                }
            }
        }

        adapter.onItemClickListener = {
            viewModel.saveToHistory(it)
            transitFragment(
                PhotoDetailFragment(),
                args = Bundle().apply {
                    putParcelable(BundleKey.KEY_ARG_PHOTO, it)
                }
            )
        }
        historyAdapter.onItemClickListener = {
            transitFragment(
                PhotoDetailFragment(),
                args = Bundle().apply {
                    putParcelable(BundleKey.KEY_ARG_PHOTO, it)
                }
            )
        }

        historyAdapter.onRemoveItemClickListener = {
            viewModel.removePhotoFromHistory(it)
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPhotoListBinding {
        return FragmentPhotoListBinding.inflate(inflater, container, false)
    }

    private fun initSkeleton() {
        viewSkeleton = binding?.rcvPhotos?.applySkeleton(R.layout.item_cell_photo, 6)
        viewSkeleton?.showSkeleton()
    }

    private fun showOrigin() {
        viewSkeleton?.showOriginal()
    }
}