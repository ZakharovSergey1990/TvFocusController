package com.example.mytvsample.third_activity

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf

class FocusableLazyScope {
    val items = mutableStateListOf<TvFocusableComponent>()

    fun item(id: String, content: @Composable (Boolean) -> Unit) {
        Log.i("FocusableLazyColumnScope", "item: $id")
        val newItem = object : TvFocusableComponent {
            override val id = id
            override val content = content
        }
        if (!items.any { it.id == id }) {
            Log.i("FocusableLazyColumnScope", "add: $newItem")
            items.add(newItem)
        }
    }
}