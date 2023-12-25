package com.example.englishplaylocal.utils

import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView

fun View.showPopUp(menu: Int, action: (Int) -> Unit) {
    setOnClickListener {
        PopupMenu(context, this).apply {
            inflate(menu)
            setOnMenuItemClickListener { item ->
                (it as? TextView)?.let { textView ->
                    textView.text = item.title
                    action.invoke(item.itemId)
                    return@setOnMenuItemClickListener true
                }
                (it as? EditText)?.let { editText ->
                    editText.setText(item.title)
                    action.invoke(item.itemId)
                    return@setOnMenuItemClickListener true
                }
                action.invoke(item.itemId)
                true
            }
            show()
        }
    }
}