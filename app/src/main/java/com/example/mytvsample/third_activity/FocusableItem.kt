package com.example.mytvsample.third_activity

import android.util.Log
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import kotlinx.coroutines.flow.map

@Composable
fun FocusableItem(id: String, content: @Composable (Boolean) -> Unit) {
    val requester = remember{ FocusRequester() }
    val currentFocused = LocalFocusContainer.current
    val hasFocus by currentFocused.currentFocus.map{  it == id }.collectAsState(false)
    var focused by remember{ mutableStateOf(false) }
    Box(modifier = Modifier
        .focusGroup()
        .focusRequester(requester)
        .onFocusChanged {
            focused = it.isFocused
            Log.i("FocusableItem", "onFocusChanged $id, $it")
        }
        .focusable()
    ) {
        content(focused)
    }
    LaunchedEffect(hasFocus) {
        Log.i("FocusableItem", "LaunchedEffect ${hasFocus}, $id")
        if (hasFocus) requester.requestFocus()
    }
}
