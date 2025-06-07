package com.bumperpick.bumperpickvendor.DI

// AppModule.kt
import DataStoreManager
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import com.bumperpick.bumperpickvendor.Repository.AuthRepository
import com.bumperpick.bumperpickvendor.Repository.AuthRepositoryImpl
import com.bumperpick.bumperpickvendor.Repository.GoogleSignInRepository
import com.bumperpick.bumperpickvendor.Repository.VendorRepository
import com.bumperpick.bumperpickvendor.Repository.VendorRepositoryImpl
import com.bumperpick.bumperpickvendor.Repository.offerRepository
import com.bumperpick.bumperpickvendor.Repository.offerRepositoryImpl
import com.bumperpick.bumperpickvendor.Screens.Account.AccountViewmodel
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.CreateOfferViewmodel
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.EditOfferViewmodel
import com.bumperpick.bumperpickvendor.Screens.Login.GoogleSignInViewModel
import com.bumperpick.bumperpickvendor.Screens.Login.LoginViewmodel
import com.bumperpick.bumperpickvendor.Screens.OTP.OtpViewModel
import com.bumperpick.bumperpickvendor.Screens.OfferPage.OfferViewmodel
import com.bumperpick.bumperpickvendor.Screens.Splash.SplashViewmodel
import com.bumperpick.bumperpickvendor.Screens.Subscription.SubscriptionViewModel
import com.bumperpick.bumperpickvendor.Screens.VendorDetailPage.VendorDetailViewmodel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.math.sin

val networkModule = module {

}

val appModule = module {
    single {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl("http://13.50.109.14/")
            .client(okHttpClient) // attach custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    single { get<Retrofit>().create(ApiService::class.java) }
    // DataStoreManager Singleton
    single { DataStoreManager(get()) }
    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get(),get()) }
    single <VendorRepository>{ VendorRepositoryImpl(get (),get()) }
    single { GoogleSignInRepository(get(),get (),get ()) }
    single <offerRepository>{ offerRepositoryImpl(get(),get(),get()) }

    // ViewModel
    viewModel { SplashViewmodel(get()) }
    viewModel { LoginViewmodel(get(),get()) }
    viewModel { OtpViewModel(get()) }
    viewModel { VendorDetailViewmodel(get()) }
    viewModel { GoogleSignInViewModel(get()) }
    viewModel { SubscriptionViewModel(get()) }
    viewModel { CreateOfferViewmodel(get()) }
    viewModel { OfferViewmodel(get()) }
    viewModel { EditOfferViewmodel(get()) }
    viewModel { AccountViewmodel(get(),get ()) }



}
