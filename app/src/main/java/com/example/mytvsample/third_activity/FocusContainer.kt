package com.example.mytvsample.third_activity

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface FocusContainer {
    val currentFocus: StateFlow<String>
    fun updateFocus(id: String)
}

class FocusContainerImpl(
    initFocusId: String,
): FocusContainer{

    private val _currentFocus = MutableStateFlow(initFocusId)

    override val currentFocus: StateFlow<String> = _currentFocus

    override fun updateFocus(id: String) {
        _currentFocus.value = id
    }
}

val LocalFocusContainer = compositionLocalOf<FocusContainer> { error("No StateFlow provided") }
