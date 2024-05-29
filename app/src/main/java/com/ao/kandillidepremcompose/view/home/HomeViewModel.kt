package com.ao.kandillidepremcompose.view.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ao.kandillidepremcompose.data.repository.KandilliRepository
import com.ao.kandillidepremcompose.model.KandilliModel
import com.ao.kandillidepremcompose.model.State
import com.ao.kandillidepremcompose.utils.enums.ShortTypes
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var isSearchState by mutableStateOf(false)
        private set

    var searchText by mutableStateOf("")
        private set

    var shortType by mutableStateOf<ShortTypes>(ShortTypes.OnceYeni)
        private set


    private val _earthquakeData = MutableLiveData<State<List<KandilliModel>>>()
    val earthquakeData: LiveData<State<List<KandilliModel>>> get() = _earthquakeData
    val searchResult = MutableLiveData<State<List<KandilliModel>>>()
    private val earthquakeRepository = KandilliRepository()

    init {
        getEarthquake()
    }

    private fun getEarthquake() {
        if ((earthquakeData.value as? State.Success)?.data?.isNotEmpty() == true) {
            return
        } else {
            viewModelScope.launch {
                earthquakeRepository.getEarthquakes().collect { state ->
                    _earthquakeData.postValue(state)
                }
            }
        }
    }

    fun searchEarthquake(search: String) {
        searchText = search
        (earthquakeData.value as? State.Success)?.data?.let { data ->
            val result = data.filter {
                it.yer?.contains(search, ignoreCase = true) == true
            }
            searchResult.postValue(State.Success(result))
        }
    }

    fun setShort(newShortType: ShortTypes) {
        shortType = newShortType
        shortEarthquake(newShortType)
    }

    private fun shortEarthquake(shortT: ShortTypes) {
        when (shortT) {
            ShortTypes.BuyuktenKucuge -> {
                val sortedList =
                    (earthquakeData.value as? State.Success)?.data?.sortedByDescending {
                        it.buyukluk?.toDoubleOrNull() ?: 0.0
                    } ?: emptyList()
                _earthquakeData.postValue(State.Success(sortedList))
            }

            ShortTypes.KucuktenBuyuge -> {
                val sortedList = (earthquakeData.value as? State.Success)?.data?.sortedBy {
                    it.buyukluk?.toDoubleOrNull() ?: 0.0
                } ?: emptyList()
                _earthquakeData.postValue(State.Success(sortedList))
            }

            ShortTypes.OnceYeni -> {
                val sortedList =
                    (earthquakeData.value as? State.Success)?.data?.sortedWith { a, b ->
                        val date1 = a.tarih
                        val date2 = b.tarih
                        val time1 = a.saat
                        val time2 = b.saat
                        val result = date2?.compareTo(date1 ?: "") ?: 0
                        if (result == 0) {
                            time2?.compareTo(time1 ?: "") ?: 0
                        } else {
                            result
                        }
                    } ?: emptyList()
                _earthquakeData.postValue(State.Success(sortedList))
            }

            ShortTypes.OnceEski -> {
                val sortedList =
                    (earthquakeData.value as? State.Success)?.data?.sortedWith { a, b ->
                        val date1 = a.tarih
                        val date2 = b.tarih
                        val time1 = a.saat
                        val time2 = b.saat
                        val result = date1?.compareTo(date2 ?: "") ?: 0
                        if (result == 0) {
                            time1?.compareTo(time2 ?: "") ?: 0
                        } else {
                            result
                        }
                    } ?: emptyList()
                _earthquakeData.postValue(State.Success(sortedList))
            }
        }
    }

    fun setSearch(newSearch: Boolean) {
        isSearchState = newSearch
        if (newSearch) {
            searchResult.postValue(earthquakeData.value)
        }
    }
}