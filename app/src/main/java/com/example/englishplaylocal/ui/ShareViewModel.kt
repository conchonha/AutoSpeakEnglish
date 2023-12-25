package com.example.englishplaylocal.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.englishplaylocal.base.BaseViewModel
import com.example.englishplaylocal.db.dao.EnglishDao
import com.example.englishplaylocal.model.EnglishTable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    application: Application,
    val englishDao: EnglishDao
) : BaseViewModel(application) {

    val listData = MutableLiveData<List<EnglishTable>>()

    init {
        getEnglishDao()
    }

    fun getEnglishDao(type: Int = 5) {
        englishDao.getListItem(type).onEach {
            listData.postValue(it)
        }.launchIn(viewModelScope)
    }

    /**
     * title: từ vựng
     * content: dịch ngĩa
     * sub: phiên âm
     */
    fun register(name: String,content: String, sub: String) {
        viewModelScope.launch {
            englishDao.insert(
                EnglishTable(
                    name = name,
                    subName = sub,
                    content = content
                )
            )
        }
    }

    fun delete(it: EnglishTable) {
        viewModelScope.launch {
            englishDao.delete(it)
        }
    }
}