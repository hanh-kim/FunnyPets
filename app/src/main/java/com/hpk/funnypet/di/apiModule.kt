package com.hpk.funnypet.di

import com.hpk.funnypet.api.MainInterface
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single(createdAtStart = false) { get<Retrofit>().create(MainInterface::class.java) }
}