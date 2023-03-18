package com.test.waltester.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.waltester.data.WTCountriesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class WTCountriesViewModel
@Inject
constructor(private val repo: WTCountriesRepo) : ViewModel() {
    /**
     * countries is a LiveData<List<CountryInfoEntity>>
     */
    val countries = repo.getCountriesInfo()

    private val _spinner = MutableLiveData(false)

    /**
     * Show a loading spinner while loading
     */
    val spinner: LiveData<Boolean>
        get() = _spinner

    /**
     * Fetch latest data from gist/endpoint.
     * Eventually this can be put into a clickListener for a fab
     * or button to fetch latest data.
     *
     * Requirements do not call for a fab/button so just loading data once
     * for now, on first load.
     */
    fun refreshTitle() = launchLoadNewCountries {
        repo.refreshNewCountries()
    }

    fun fetchFromCache() = launchLoadNewCountries {
        repo.fetchCachedCountriesInfo()
    }


    private fun launchLoadNewCountries(completion: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _spinner.value = true
                completion()
            } catch (error: Error) {
                Log.e(TAG, "launchDataLoad(): uh oh! unable to fetch new countries!")
            } finally {
                _spinner.value = false
            }
        }
    }

    companion object {
        const val TAG = "MainWTCoViewModel"
    }
}