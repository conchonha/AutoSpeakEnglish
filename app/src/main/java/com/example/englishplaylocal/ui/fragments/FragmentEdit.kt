package com.example.englishplaylocal.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.englishplaylocal.R
import com.example.englishplaylocal.base.BaseFragment
import com.example.englishplaylocal.databinding.FragmentEditBinding
import com.example.englishplaylocal.ui.ShareViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentEdit : BaseFragment<FragmentEditBinding, ShareViewModel>() {
    override val viewModel: ShareViewModel by viewModels()
    override val layoutId: Int = R.layout.fragment_edit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnAdd.setOnClickListener {
                if (edtVocabulary.text.toString().trim()
                        .isNullOrBlank() || edtDefineContent.text.toString().trim().isNullOrBlank()
                ) {
                    showToast("Not Format English")
                    return@setOnClickListener
                }

                viewModel.register(
                    edtVocabulary.text.toString(),
                    edtDefineContent.text.toString(),
                    edtSub.text.toString()
                )

                showToast("Add Vocabulary Success")
            }

            ivBack.setOnClickListener {
                onBackFragment()
            }
        }
    }
}