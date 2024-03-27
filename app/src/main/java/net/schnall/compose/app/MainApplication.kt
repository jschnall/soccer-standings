package net.schnall.compose.app

import android.app.Application
import net.schnall.compose.app.di.appModule
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule())
        }
    }
}