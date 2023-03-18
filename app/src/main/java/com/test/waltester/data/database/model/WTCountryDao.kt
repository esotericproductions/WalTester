package com.test.waltester.data.database.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.test.waltester.data.cache.model.CountryInfoEntity

@Dao
interface WTCountryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCountryInfo(countryInfo: CountryInfoEntity)

    @get:Query("SELECT * FROM CountryInfoEntity")
    val countryInfoLiveData: List<CountryInfoEntity>
}