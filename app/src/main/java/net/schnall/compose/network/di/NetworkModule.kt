package net.schnall.compose.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.schnall.compose.BuildConfig
import net.schnall.compose.network.GameApi
import net.schnall.compose.network.GameApiImpl
import net.schnall.compose.network.GameService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://s.yimg.com/"

fun networkModule() = module {
    single<GameApi> { GameApiImpl(get()) }

    single<GameService> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
            .create(GameService::class.java)
    }

    single<Gson> {
        GsonBuilder().create()
    }

    single<OkHttpClient> {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        builder.build()
    }
}
