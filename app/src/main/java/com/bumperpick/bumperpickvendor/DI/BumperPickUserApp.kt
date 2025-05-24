package com.bumperpick.bumperpickvendor.DI

import android.app.Application
import com.bumperpick.bumperpickvendor.DI.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class BumperPickVendorApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BumperPickVendorApp)
            modules(appModule, networkModule)
        }
    }
}