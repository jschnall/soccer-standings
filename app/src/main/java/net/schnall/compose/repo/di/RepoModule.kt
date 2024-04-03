package net.schnall.compose.repo.di

import androidx.room.Room
import kotlinx.datetime.Clock
import net.schnall.compose.data.AppDatabase
import net.schnall.compose.data.dao.GameDao
import net.schnall.compose.network.di.networkModule
import net.schnall.compose.repo.GameRepo
import net.schnall.compose.repo.GameRepoImpl
import net.schnall.compose.repo.cache.ApiCacheHelper
import net.schnall.compose.repo.cache.ApiDataStore
import net.schnall.compose.repo.cache.ApiDatastoreImpl
import org.koin.dsl.module

val repoModule = module {
    includes(networkModule())

    single<ApiCacheHelper> { ApiCacheHelper(get(), get()) }
    single<ApiDataStore> { ApiDatastoreImpl(get()) }

    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "GameDatabase"
        ).build()
    }
    single<GameDao> { get<AppDatabase>().gameDao() }
    single<GameRepo> { GameRepoImpl(get(), get(), get()) }
    single<Clock> { Clock.System }
}