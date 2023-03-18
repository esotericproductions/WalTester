package com.test.waltester.di

import android.content.Context
import androidx.room.Room
import com.test.waltester.data.WTCountriesRepo
import com.test.waltester.data.database.WTCountriesDb
import com.test.waltester.data.database.model.WTCountryDao
import com.test.waltester.data.network.model.WTCountryInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.http.GET
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CountriesModule {

    @Singleton
    @Provides
    fun provideWTCountryDao(countriesDb: WTCountriesDb): WTCountryDao {
        return countriesDb.wtCountryDao
    }

    @Singleton
    @Provides
    fun provideCountriesDatabase(@ApplicationContext appContext: Context): WTCountriesDb {
        return Room.databaseBuilder(
            appContext,
            WTCountriesDb::class.java,
            "wtcountriesdb"
        ).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideWTCountriesRepo(network: WTNetworker?,
                               dao: WTCountryDao): WTCountriesRepo{
        return WTCountriesRepo(
            network = network,
            wtCountryDao = dao
        )
    }
}

/**
 * network interface to fetch a new countries list
 */
interface WTNetworker {
    @GET("countries.json")
    suspend fun fetchNewCountries(): WTCountryInfo?
}