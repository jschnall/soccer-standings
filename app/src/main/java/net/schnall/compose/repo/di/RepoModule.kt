package net.schnall.compose.repo.di

import net.schnall.compose.network.di.networkModule
import net.schnall.compose.repo.GameRepo
import net.schnall.compose.repo.GameRepoImpl
import org.koin.dsl.module

val repoModule = module {
    includes(networkModule())
    single<GameRepo> { GameRepoImpl(get()) }
}