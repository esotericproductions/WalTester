package com.test.waltester.data.database.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.test.waltester.data.cache.model.CountryInfoEntity

@Dao
interface WTCountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountryInfo(countryInfo: CountryInfoEntity)

    @get:Query("select distinct * from CountryInfoEntity")
    val countryInfoLiveData: LiveData<List<CountryInfoEntity?>>
}