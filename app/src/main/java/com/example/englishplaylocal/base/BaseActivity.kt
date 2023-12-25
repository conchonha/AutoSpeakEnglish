package com.example.englishplaylocal.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

abstract class BaseActivity<VB : ViewDataBinding>() :
    AppCompatActivity() {
    @get:LayoutRes
    protected abstract val layoutId: Int

    protected val TAG = this.javaClass.simpleName
    private val asyncCallbackInternetChange = java.util.HashSet<Callback>()
    private val netWorkChange by lazy {
        NetworkChangeReceiver {
            asyncCallbackInternetChange.forEach {
                it?.invoke()
            }
        }
    }
    var onBackPressCheck: Callback = null

    open val fragmentContainerView: Int? = null

    lateinit var binding: VB
        private set
    lateinit var navHostFragment: NavHostFragment
        private set
    var navController: NavController? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        binding = DataBindingUtil.setContentView(this, layoutId)

        if (fragmentContainerView != null) {
            navHostFragment =
                supportFragmentManager.findFragmentById(fragmentContainerView!!) as NavHostFragment
            navController = navHostFragment.navController
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart: ")
        IntentFilter().apply {
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            registerReceiver(netWorkChange, this)
        }
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        unregisterReceiver(netWorkChange)
        Log.d(TAG, "onStop: ")
        super.onStop()
    }

    // unfocus && close keyboard edittext when select outside
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun addInternetChangeListener(callback: Callback) {
        asyncCallbackInternetChange.add(callback)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: ")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        asyncCallbackInternetChange.clear()
        super.onDestroy()
    }
}

typealias Callback = (() -> Unit)?

class NetworkChangeReceiver(val onChange: Callback = null) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        onChange?.invoke()
    }
}