package pt.hventura.mycoktails.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import pt.hventura.mycoktails.BuildConfig
import pt.hventura.mycoktails.authentication.AuthenticationViewModel
import pt.hventura.mycoktails.cocktails.coctailsMap.CocktailMapViewModel
import pt.hventura.mycoktails.cocktails.listcocktails.CocktailListViewModel
import pt.hventura.mycoktails.cocktails.randomcocktail.RandomCocktailViewModel
import pt.hventura.mycoktails.data.CocktailsRepositoryImpl
import pt.hventura.mycoktails.data.database.CocktailsDatabase
import pt.hventura.mycoktails.data.remote.CocktailsDataService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

object AppModule {

    fun load() = loadKoinModules(
        listOf(
            defaultModules(),
            databaseModule(),
            networkModule()
        )
    )

    private fun defaultModules(): Module {
        return module {
            viewModel {
                AuthenticationViewModel(get())
            }
            viewModel {
                CocktailListViewModel(get(), get() as CocktailsRepositoryImpl)
            }
            viewModel {
                RandomCocktailViewModel(get(), get() as CocktailsRepositoryImpl)
            }
            viewModel {
                CocktailMapViewModel(get(), get() as CocktailsRepositoryImpl)
            }
            single {
                CocktailsRepositoryImpl(get(), get())
            }
        }
    }

    private fun databaseModule(): Module {
        return module {
            single {
                CocktailsDatabase.getInstance(androidContext()).cocktailsDao
            }
        }
    }

    private fun networkModule(): Module {
        return module {
            single { createService<CocktailsDataService>(get(), get()) }
            single { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
            single {
                val interceptor = HttpLoggingInterceptor {
                    Timber.e(it)
                }
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            }
        }
    }

    private inline fun <reified T> createService(
        factory: Moshi,
        client: OkHttpClient
    ): T {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(factory))
            .client(client)
            .build()
            .create(T::class.java)
    }

}