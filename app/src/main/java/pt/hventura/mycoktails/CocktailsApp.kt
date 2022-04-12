package pt.hventura.mycoktails

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import pt.hventura.mycoktails.di.AppModule
import pt.hventura.mycoktails.utils.PreferencesManager
import timber.log.Timber

class CocktailsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        configureTimber()
        startPreferencesManager()
        // Start koin
        configureKoin()
        AppModule.load()
    }

    private fun configureTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun startPreferencesManager() {
        PreferencesManager.with(this)
    }

    private fun configureKoin() = startKoin {
        androidContext(this@CocktailsApp)
        if (BuildConfig.DEBUG)
            androidLogger()
    }

}