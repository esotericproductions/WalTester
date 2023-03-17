package com.test.waltester.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.waltester.data.cache.model.CountryInfoEntity
import com.test.waltester.data.database.model.WTCountryDao

/**
 * WTCountriesDb provides a dao reference to repositories
 */
@Database(entities = [CountryInfoEntity::class], version = 1, exportSchema = false)
abstract class WTCountriesDb : RoomDatabase() {
    abstract val wtCountryDao: WTCountryDao
}
