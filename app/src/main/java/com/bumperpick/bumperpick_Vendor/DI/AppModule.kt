package com.bumperpick.bumperpick_Vendor.DI

// AppModule.kt
import DataStoreManager
import com.bumperpick.bumperickUser.Repository.SupportRepository
import com.bumperpick.bumperickUser.Repository.SupportRepositoryImpl
import com.bumperpick.bumperickUser.Screens.Faq.FaqViewmodel
import com.bumperpick.bumperickUser.Screens.Support.SupportViewModel
import com.bumperpick.bumperpick_Vendor.API.FinalModel.Feature
import com.bumperpick.bumperpick_Vendor.API.FinalModel.FeatureDeserializer
import com.bumperpick.bumperpick_Vendor.API.Provider.ApiService
import com.bumperpick.bumperpick_Vendor.Repository.AdsRepository
import com.bumperpick.bumperpick_Vendor.Repository.AdsRepositoryImpl
import com.bumperpick.bumperpick_Vendor.Repository.AuthRepository
import com.bumperpick.bumperpick_Vendor.Repository.AuthRepositoryImpl
import com.bumperpick.bumperpick_Vendor.Repository.Event2Repository
import com.bumperpick.bumperpick_Vendor.Repository.EventRepository
import com.bumperpick.bumperpick_Vendor.Repository.EventRepository2Impl
import com.bumperpick.bumperpick_Vendor.Repository.EventRepositoryImpl
import com.bumperpick.bumperpick_Vendor.Repository.GoogleSignInRepository
import com.bumperpick.bumperpick_Vendor.Repository.OfferRepositoryImpl
import com.bumperpick.bumperpick_Vendor.Repository.VendorRepository
import com.bumperpick.bumperpick_Vendor.Repository.VendorRepositoryImpl
import com.bumperpick.bumperpick_Vendor.Repository.offerRepository

import com.bumperpick.bumperpick_Vendor.Screens.Account.AccountViewmodel
import com.bumperpick.bumperpick_Vendor.Screens.Ads.AdsViewModel
import com.bumperpick.bumperpick_Vendor.Screens.CreateOfferScreen.CreateOfferViewmodel
import com.bumperpick.bumperpick_Vendor.Screens.CreateOfferScreen.EditOfferViewmodel
import com.bumperpick.bumperpick_Vendor.Screens.EditAccountScreen.EditAccountViewModel
import com.bumperpick.bumperpick_Vendor.Screens.Event2.Events2Viewmodel
import com.bumperpick.bumperpick_Vendor.Screens.Campaign.EventsViewmodel
import com.bumperpick.bumperpick_Vendor.Screens.Home.HomePageviewmodel
import com.bumperpick.bumperpick_Vendor.Screens.Login.GoogleSignInViewModel
import com.bumperpick.bumperpick_Vendor.Screens.Login.LoginViewmodel
import com.bumperpick.bumperpick_Vendor.Screens.NotificationScreen.NotificationViewmodel
import com.bumperpick.bumperpick_Vendor.Screens.OTP.OtpViewModel
import com.bumperpick.bumperpick_Vendor.Screens.OfferPage.OfferViewmodel
import com.bumperpick.bumperpick_Vendor.Screens.QrScreen.QrScreenViewmodel
import com.bumperpick.bumperpick_Vendor.Screens.Splash.SplashViewmodel
import com.bumperpick.bumperpick_Vendor.Screens.Subscription.SubscriptionViewModel
import com.bumperpick.bumperpick_Vendor.Screens.VendorDetailPage.VendorDetailViewmodel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

}

val appModule = module {
    single {

        val gson = GsonBuilder()
            .registerTypeAdapter(Feature::class.java, FeatureDeserializer())
            .create()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl("http://13.200.242.189/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


    single { get<Retrofit>().create(ApiService::class.java) }
    // DataStoreManager Singleton
    single { DataStoreManager(get()) }
    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get(),get()) }
    single <VendorRepository>{ VendorRepositoryImpl(get (),get()) }
    single { GoogleSignInRepository(get(),get (),get ()) }
    single <offerRepository>{ OfferRepositoryImpl(get(),get(),get()) }
    single <EventRepository>{EventRepositoryImpl(get(),get(),get())}
    single <Event2Repository>{ EventRepository2Impl(get(),get(),get()) }
    single <AdsRepository>{ AdsRepositoryImpl(get(),get()) }
    single <SupportRepository>{ SupportRepositoryImpl(get(),get(),get()) }
    // ViewModel
    viewModel { SplashViewmodel(get()) }
    viewModel { LoginViewmodel(get(),get()) }
    viewModel { OtpViewModel(get()) }
    viewModel { VendorDetailViewmodel(get()) }
    viewModel { GoogleSignInViewModel(get()) }
    viewModel { SubscriptionViewModel(get()) }
    viewModel { CreateOfferViewmodel(get()) }
    viewModel { OfferViewmodel(get()) }
    viewModel { EditOfferViewmodel(get(),get(),get()) }
    viewModel { AccountViewmodel(get(),get (),get()) }
    viewModel { QrScreenViewmodel(get(),get ()) }
    viewModel { EditAccountViewModel(get()) }
    viewModel { EventsViewmodel(get()) }
    viewModel { Events2Viewmodel(get())}
    viewModel { AdsViewModel(get(),get()) }
    viewModel { FaqViewmodel(get()) }
    viewModel { SupportViewModel(get()) }
    viewModel { HomePageviewmodel() }
    viewModel { NotificationViewmodel(get ()) }


}
