package com.example.englishplaylocal.ui.fragments

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.englishplaylocal.R
import com.example.englishplaylocal.base.BaseFragment
import com.example.englishplaylocal.databinding.FragmentHomeBinding
import com.example.englishplaylocal.ui.ShareViewModel
import com.example.englishplaylocal.ui.adapter.EnglishAdapter
import com.example.englishplaylocal.utils.showPopUp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, ShareViewModel>(),
    TextToSpeech.OnInitListener {
    override val viewModel: ShareViewModel by viewModels()
    override val layoutId: Int = R.layout.fragment_home

    private lateinit var textToSpeech: TextToSpeech
    private val adapterEnglish: EnglishAdapter by lazy { EnglishAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textToSpeech = TextToSpeech(requireContext(), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvEnglish.adapter = adapterEnglish

        binding.tvDrop.showPopUp(R.menu.menu_type) {
            viewModel.getEnglishDao(converType(it))
        }

        binding.ivAdd.setOnClickListener {
            navigateToDestination(R.id.fragmentEdit)
        }

        viewModel.listData.observe(viewLifecycleOwner) {
            binding.emptyView.root.isVisible = it.size == 0
            adapterEnglish.updateItems(it)
        }

        binding.ivMenu.setOnClickListener {
            lifecycleScope.launch {
                for (listItem in adapterEnglish.getListItems()) {
                    speak(listItem.name)
                    delay(1500)
                }
            }
        }

        with(adapterEnglish) {
            edit = {

            }

            volume = {
                speak(it.name)
            }

            delete = {
                viewModel.delete(it)
            }
        }
    }

    private fun speak(text: String) {
        // Kiểm tra xem TextToSpeech đã được khởi tạo thành công chưa
        val supportedLanguages = textToSpeech.availableLanguages
        Log.d("TextToSpeech", "Supported Languages: $supportedLanguages")

        if (textToSpeech.isLanguageAvailable(Locale.ENGLISH) == TextToSpeech.LANG_AVAILABLE) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Đặt ngôn ngữ cho TextToSpeech
            val result = textToSpeech.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                // Xử lý khi ngôn ngữ không hỗ trợ
            }
        } else {
            // Xử lý khi khởi tạo TextToSpeech không thành công
        }
    }

    override fun onDestroy() {
        // Giải phóng tài nguyên TextToSpeech khi không cần sử dụng nữa
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }

    private fun converType(idMenu: Int): Int {
        return when (idMenu) {
            R.id.it1 -> 1
            R.id.it2 -> 2
            R.id.it3 -> 3
            R.id.it4 -> 4
            else -> 5
        }
    }
}