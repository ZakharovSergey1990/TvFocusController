package com.example.mytvsample.third_activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.StateFlow

@Composable
fun BaseFocusableContent(
    focus: String,
    modifier: Modifier = Modifier,
    content : @Composable () -> Unit
) {
    CompositionLocalProvider(LocalFocusContainer provides FocusContainerImpl(focus)) {
        Box(modifier = modifier
            .fillMaxWidth()
        ) {
            content()
        }
    }
}