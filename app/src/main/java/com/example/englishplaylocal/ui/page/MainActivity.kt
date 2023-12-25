package com.example.englishplaylocal.ui.page

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.viewModels
import com.example.englishplaylocal.R
import com.example.englishplaylocal.base.BaseActivity
import com.example.englishplaylocal.databinding.ActivityMainBinding
import com.example.englishplaylocal.ui.ShareViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val fragmentContainerView = R.id.fragmentContainerView

    override val layoutId = R.layout.activity_main

}