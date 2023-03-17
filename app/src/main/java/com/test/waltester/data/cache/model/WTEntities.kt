package com.test.waltester.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CountryInfoEntity constructor(
    @PrimaryKey
    val name: String,
    val region: String,
    val code: String,
    val capital: String
)