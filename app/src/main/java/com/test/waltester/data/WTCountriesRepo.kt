package com.test.waltester.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.waltester.data.cache.model.CountryInfoEntity
import com.test.waltester.data.database.model.WTCountryDao
import com.test.waltester.di.WTNetworker
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


class WTCountriesRepo @Inject constructor(val network: WTNetworker,
                                          val wtCountryDao: WTCountryDao) {

    private val countriesMutableLiveData = MutableLiveData<List<CountryInfoEntity?>>()

    fun getCountriesInfo(): LiveData<List<CountryInfoEntity?>> {
        return countriesMutableLiveData
    }

    /**
     * Refresh the current countries list:
     * always show the items that are stored in offline cache.
     * 1. fetch the latest list of WTCountryInfo from the network service
     * 2. map from List<WTCountryInfo> -> List<CountryInfoEntity> for UI consumers
     * 3. postValue for List<CountryInfoEntity>
     * 4. insert into db for offline data
     */
    suspend fun refreshNewCountries() {
        try {
            // fetch new data
            val result = withTimeout(5_000) {
                Log.d(TAG, "refreshNewCountries():")
                network.fetchNewCountries()
            }
            // map from network data model to UI consumer model
            val newResults: List<CountryInfoEntity> = result.map { country ->
                    CountryInfoEntity (
                        name = country.name,
                        region = country.region,
                        code = country.code,
                        capital = country.capital
                    )
            }

            if(newResults.isNotEmpty()) {
                // post to vm
                countriesMutableLiveData.postValue(newResults)
                // insert to room db
                newResults.forEach {
                    wtCountryDao.insertCountryInfo(it)
                }
            }

        } catch (error: Throwable) {
            throw WTCountriesRefreshError("Unable to refresh CountryInfoEntity", error)
        }
    }

    companion object {
        const val TAG = "WTCountriesRepo"
    }
}

class WTCountriesRefreshError(message: String, cause: Throwable) : Throwable(message, cause)