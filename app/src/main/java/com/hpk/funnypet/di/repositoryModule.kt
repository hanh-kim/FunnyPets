package com.hpk.funnypet.di

import com.hpk.funnypet.repository.MainRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<MainRepository> { MainRepository.MainRepositoryImpl(get()) }
//
}