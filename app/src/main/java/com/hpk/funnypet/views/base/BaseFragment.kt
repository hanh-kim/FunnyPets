package com.hpk.funnypet.views.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.hpk.funnypet.R

abstract class BaseFragment<B : ViewDataBinding> :  Fragment(){
    private val TAG = this::class.java.name
    private var _binding: B? = null

    val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val viewBinding = getFragmentBinding(inflater, container)
        initView(inflater, container, viewBinding)
        this._binding = viewBinding
        return viewBinding.root
    }
    abstract fun initView(inflater: LayoutInflater, container: ViewGroup?, binding: B)

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    protected fun transitFragment(
        fragment: BaseFragment<*>,
        @IdRes id: Int,
        args: Bundle? = null,
        @AnimRes enterAnim: Int = R.anim.slide_in,
        @AnimRes exitAnim: Int = R.anim.fade_out,
        @AnimRes popEnter: Int = R.anim.fade_in,
        @AnimRes popExit: Int = R.anim.slide_out,
    ) {

        val fragmentManager = activity?.supportFragmentManager
        args?.let {
            fragment.arguments?.putAll(args) ?: kotlin.run {
                fragment.arguments = args
            }
        }
        fragmentManager?.beginTransaction()?.setCustomAnimations(
            enterAnim,  // enter
            exitAnim,  // exit
            popEnter,   // popEnter
            popExit  // popExit
        )?.add(id, fragment, fragment.javaClass.name)?.addToBackStack(fragment.TAG)?.commit()
    }

}