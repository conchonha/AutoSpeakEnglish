package com.example.englishplaylocal.base

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel(application: Application) : AndroidViewModel(application){
    protected val TAG by lazy { this::class.java.name }
    protected val evenSender = Channel<AppEvent>()

    val eventReceiver = evenSender.receiveAsFlow().conflate()
    var checkReLoad: Boolean = false

    open fun onClickClose() {
        viewModelScope.launch {
            evenSender.send(AppEvent.OnCloseApp)
        }
    }

    open fun onBackStack() {
        viewModelScope.launch {
            evenSender.send(AppEvent.OnBackScreen)
        }
    }

    open fun showDialogConfirm(
        title: String? = "",
        message: String? = "",
        lblOke: String? = "Ok",
        lblCancel: String? = "Cancel"
    ) {
        viewModelScope.launch {
            evenSender.send(AppEvent.OnOpenAlertDialog(title, message, lblOke, lblCancel))
        }
    }


    open fun navigateToDestination(action: Int, bundle: Bundle? = null) = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnNavigation(action, bundle)
        )
    }

    open fun backScreen() = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnBackScreen
        )
    }

    open fun closeApp() = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnCloseApp
        )
    }

    open fun showToast(content: String) = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnShowToast(content)
        )
    }

    open fun <T>sendResponse(key: String, data: T) = viewModelScope.launch{
        evenSender.send(
            AppEvent.OnResponse(key,data)
        )
    }
}

sealed class AppEvent {
    class OnNavigation(val destination: Int, val bundle: Bundle? = null) : AppEvent()
    object OnCloseApp : AppEvent()
    object OnBackScreen : AppEvent()
    class OnShowToast(val content: String, val type: Long = 2000) : AppEvent()
    class OnOpenAlertDialog(
        val title: String? = "",
        val content: String? = "",
        val labelOke: String? = "",
        val lblCancel: String? = ""
    ) : AppEvent()

    data class OnResponse<T>(val key : String, val data: T) : AppEvent()
}