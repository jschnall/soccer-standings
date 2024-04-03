package net.schnall.compose.app.di

import net.schnall.compose.app.AppViewModel
import net.schnall.compose.app.TeamDetailViewModel
import net.schnall.compose.app.TeamListViewModel
import net.schnall.compose.repo.di.repoModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun appModule() = module {
    includes(repoModule)

    viewModel { AppViewModel() }
    viewModel { TeamListViewModel(get()) }
    viewModel { TeamDetailViewModel(get()) }
}