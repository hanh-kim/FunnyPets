package com.hpk.funnypet.di

import com.hpk.funnypet.views.fragments.a.PhotoListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PhotoListViewModel() }
}