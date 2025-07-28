package com.bumperpick.bumperpick_Vendor.DI

import android.app.Application
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