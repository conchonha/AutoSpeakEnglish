package com.example.englishplaylocal.ui.adapter

import com.example.englishplaylocal.R
import com.example.englishplaylocal.base.BaseRecyclerViewAdapter
import com.example.englishplaylocal.databinding.RvItEnglishBinding
import com.example.englishplaylocal.model.EnglishTable

class EnglishAdapter : BaseRecyclerViewAdapter<EnglishTable, RvItEnglishBinding>() {
    override val layoutId: Int = R.layout.rv_it_english
    var delete: ((EnglishTable) -> Unit)? = null
    var edit: ((EnglishTable) -> Unit)? = null
    var volume: ((EnglishTable) -> Unit)? = null

    override fun onBindViewHolder(holder: BaseViewHolder<RvItEnglishBinding>, position: Int) {
        val data = getListItems().getOrNull(position) ?: return
        with(holder.binding) {
            tvName.setText(data.name)
            tvSub.setText(" [${data.subName}] : ${data.content}")

            ivEdit.setOnClickListener {
                edit?.invoke(data)
            }

            ivDelete.setOnClickListener {
                delete?.invoke(data)
            }

            ivVolume.setOnClickListener {
                volume?.invoke(data)
            }
        }
    }
}