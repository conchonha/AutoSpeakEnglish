package com.example.englishplaylocal.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.englishplaylocal.utils.Validations
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel> : Fragment() {
    protected val TAG by lazy { this::class.java.name }

    lateinit var binding: T
        private set

    protected var jopEventReceiver: Job? = null
    abstract val viewModel: VM

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onAttach(ctx: Context) {
        super.onAttach(ctx)
        Log.d(TAG, "onAttach: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: $TAG")
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: $savedInstanceState")

        jopEventReceiver = lifecycleScope.launch {
            viewModel.eventReceiver.collectLatest {
                when (it) {
                    is AppEvent.OnNavigation -> navigateToDestination(it.destination, it.bundle)
                    AppEvent.OnCloseApp -> closeApp()
                    AppEvent.OnBackScreen -> onBackFragment()
                    is AppEvent.OnShowToast -> showToast(it.content, it.type)
                    is AppEvent.OnOpenAlertDialog -> showDialogConfirm(
                        it.title, it.content, it.labelOke, it.lblCancel
                    )
                    is AppEvent.OnResponse<*> -> onResponse(it.key,it.data)
                }
            }
        }

        binding.root.setOnClickListener {
            Validations.hideKeyboard(it, requireContext())
        }
    }

    open fun onResponse(key: String, data: Any?) {
        Log.d(TAG, "onResponse: Called key: $key ---- data: $data")
    }


    open fun showDialogConfirm(
        title: String? = "Box",
        message: String? = "",
        lblOke: String? = "Oke",
        lblCancel: String? = "Cancel",
        onConfirm: (() -> Unit)? = null
    ) {
        Log.d(
            TAG,
            "showDialogConfirm: title:$title ---- message: $message --- lblOke: $lblOke --- lblCancel: $lblCancel"
        )

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.checkReLoad) return
        viewModel.checkReLoad = true
        onReload()
        Log.d(TAG, "onResume: ")
    }

    open fun onReload(){
        Log.d(TAG, "onReload: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    open fun showToast(content: String, duration: Long = 2000) {
        Log.d(TAG, "showToast: content: $content ---- type: $duration")
        Toast.makeText(requireContext(),content,Toast.LENGTH_SHORT).show()

    }

    open fun showToast(res: Int, duration: Long = 2000) {
        Log.d(TAG, "showToast: content: $res ---- type: $duration")

    }

    open fun onBackFragment() {
        Log.d(TAG, "onBackFragment: ")
        findNavController().popBackStack()
    }

    open fun navigateToDestination(destination: Int, bundle: Bundle? = null) {
        Log.d(TAG, "navigateToDestination: ")
        findNavController().apply {
            bundle?.let {
                navigate(destination, it)
            } ?: navigate(destination)
        }
    }

    open fun openAnotherApp(packageName: String, bundle: Bundle?) {
        val launch = context?.packageManager?.getLaunchIntentForPackage(packageName)
        launch?.let {
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(it, bundle)
        }
    }

    open fun closeApp() {
        activity?.finishAffinity()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: ")
        jopEventReceiver?.cancel()
        super.onDestroyView()
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach: ")
        super.onDetach()
    }

    fun onClearViewModelInScopeActivity() {
        activity?.viewModelStore?.clear()
    }
}