package net.schnall.compose.repo.di

import androidx.room.Room
import net.schnall.compose.data.AppDatabase
import net.schnall.compose.data.dao.GameDao
import net.schnall.compose.network.di.networkModule
import net.schnall.compose.repo.GameRepo
import net.schnall.compose.repo.GameRepoImpl
import org.koin.dsl.module

val repoModule = module {
    includes(networkModule())

    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "GameDatabase"
        ).build()
    }
    single<GameDao> { get<AppDatabase>().gameDao() }
    single<GameRepo> { GameRepoImpl(get(), get()) }
}