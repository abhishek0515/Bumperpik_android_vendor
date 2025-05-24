package com.bumperpick.bumperpickvendor.DI

// AppModule.kt
import DataStoreManager
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import com.bumperpick.bumperpickvendor.Repository.AuthRepository
import com.bumperpick.bumperpickvendor.Repository.AuthRepositoryImpl
import com.bumperpick.bumperpickvendor.Repository.GoogleSignInRepository
import com.bumperpick.bumperpickvendor.Repository.VendorRepository
import com.bumperpick.bumperpickvendor.Repository.VendorRepositoryImpl
import com.bumperpick.bumperpickvendor.Screens.Login.GoogleSignInViewModel
import com.bumperpick.bumperpickvendor.Screens.Login.LoginViewmodel
import com.bumperpick.bumperpickvendor.Screens.OTP.OtpViewModel
import com.bumperpick.bumperpickvendor.Screens.Splash.SplashViewmodel
import com.bumperpick.bumperpickvendor.Screens.VendorDetailPage.VendorDetailViewmodel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

}

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("http://13.50.109.14/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(ApiService::class.java) }
    // DataStoreManager Singleton
    single { DataStoreManager(get()) }
    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get(),get()) }
    single <VendorRepository>{ VendorRepositoryImpl(get (),get()) }
    single { GoogleSignInRepository(get()) }
    // ViewModel
    viewModel { SplashViewmodel(get()) }
    viewModel { LoginViewmodel(get(),get()) }
    viewModel { OtpViewModel(get()) }
    viewModel { VendorDetailViewmodel(get()) }
    viewModel { GoogleSignInViewModel(get()) }
}
