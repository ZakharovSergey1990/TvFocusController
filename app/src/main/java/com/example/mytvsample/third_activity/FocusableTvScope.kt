package com.example.mytvsample.third_activity

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import kotlinx.coroutines.flow.map

class FocusableTvScope(private val items: MutableList<FocusableComponent>) {
    @Composable
    fun FocusableItem(id: String, content: @Composable (Boolean) -> Unit) {
        val currentFocused = LocalFocusContainer.current
        val hasFocus by currentFocused.currentFocus.map{  it == id }.collectAsState(false)
        val requester = remember { FocusRequester() }
        val focusableComponent = remember {
            object : FocusableComponent {
                override val focusedId = id
            }
        }
        LaunchedEffect(id) {
            items.add(focusableComponent)
        }
        Box(modifier = Modifier
            .focusGroup()
            .focusRequester(requester)
            .focusable()
        ) {
            content(hasFocus)
        }
        LaunchedEffect(hasFocus) {
            if (hasFocus) requester.requestFocus()
        }
    }
}