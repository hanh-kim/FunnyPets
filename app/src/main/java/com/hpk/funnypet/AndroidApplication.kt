package com.hpk.funnypet

import android.app.Application
import com.hpk.funnypet.di.apiModule
import com.hpk.funnypet.di.repositoryModule
import com.hpk.funnypet.di.retrofitModule
import com.hpk.funnypet.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AndroidApplication : Application() {

    companion object {
        lateinit var mInstance : AndroidApplication
    }
    override fun onCreate() {
        super.onCreate()

        mInstance = this
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AndroidApplication)
            // moduleが増えたらlistに追加
            modules(
                listOf(retrofitModule,apiModule, viewModelModule, repositoryModule,)
            )
        }
    }
}