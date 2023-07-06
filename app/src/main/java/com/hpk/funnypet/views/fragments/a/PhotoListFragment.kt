package com.hpk.funnypet.views.fragments.a

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hpk.funnypet.databinding.FragmentPhotoListBinding
import com.hpk.funnypet.views.base.BaseFragment

class PhotoListFragment : BaseFragment<FragmentPhotoListBinding>() {
    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        binding: FragmentPhotoListBinding
    ) {

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPhotoListBinding {
        return FragmentPhotoListBinding.inflate(inflater, container, false)
    }
}