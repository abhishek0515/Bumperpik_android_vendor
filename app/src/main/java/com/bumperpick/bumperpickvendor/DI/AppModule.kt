package com.bumperpick.bumperpickvendor.DI

// AppModule.kt
import DataStoreManager
import com.bumperpick.bumperpickvendor.API.FinalModel.Feature
import com.bumperpick.bumperpickvendor.API.FinalModel.FeatureDeserializer
import com.bumperpick.bumperpickvendor.API.Provider.ApiService
import com.bumperpick.bumperpickvendor.Repository.AdsRepository
import com.bumperpick.bumperpickvendor.Repository.AdsRepositoryImpl
import com.bumperpick.bumperpickvendor.Repository.AuthRepository
import com.bumperpick.bumperpickvendor.Repository.AuthRepositoryImpl
import com.bumperpick.bumperpickvendor.Repository.Event2Repository
import com.bumperpick.bumperpickvendor.Repository.EventRepository
import com.bumperpick.bumperpickvendor.Repository.EventRepository2Impl
import com.bumperpick.bumperpickvendor.Repository.EventRepositoryImpl
import com.bumperpick.bumperpickvendor.Repository.GoogleSignInRepository
import com.bumperpick.bumperpickvendor.Repository.OfferRepositoryImpl
import com.bumperpick.bumperpickvendor.Repository.VendorRepository
import com.bumperpick.bumperpickvendor.Repository.VendorRepositoryImpl
import com.bumperpick.bumperpickvendor.Repository.offerRepository

import com.bumperpick.bumperpickvendor.Screens.Account.AccountViewmodel
import com.bumperpick.bumperpickvendor.Screens.Ads.AdsViewModel
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.CreateOfferViewmodel
import com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen.EditOfferViewmodel
import com.bumperpick.bumperpickvendor.Screens.EditAccountScreen.EditAccountViewModel
import com.bumperpick.bumperpickvendor.Screens.Event2.Events2Viewmodel
import com.bumperpick.bumperpickvendor.Screens.Campaign.EventsViewmodel
import com.bumperpick.bumperpickvendor.Screens.Login.GoogleSignInViewModel
import com.bumperpick.bumperpickvendor.Screens.Login.LoginViewmodel
import com.bumperpick.bumperpickvendor.Screens.OTP.OtpViewModel
import com.bumperpick.bumperpickvendor.Screens.OfferPage.OfferViewmodel
import com.bumperpick.bumperpickvendor.Screens.QrScreen.QrScreenViewmodel
import com.bumperpick.bumperpickvendor.Screens.Splash.SplashViewmodel
import com.bumperpick.bumperpickvendor.Screens.Subscription.SubscriptionViewModel
import com.bumperpick.bumperpickvendor.Screens.VendorDetailPage.VendorDetailViewmodel
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
            .baseUrl("http://13.50.109.14/")
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
    viewModel { AccountViewmodel(get(),get ()) }
    viewModel { QrScreenViewmodel(get(),get ()) }
    viewModel { EditAccountViewModel(get()) }
    viewModel { EventsViewmodel(get()) }
    viewModel { Events2Viewmodel(get())}
    viewModel { AdsViewModel(get()) }




}
