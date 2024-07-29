package com.example.mytvsample.third_activity

import androidx.compose.runtime.Composable

interface TvFocusableComponent {
    val id: String
    val content: @Composable (Boolean) -> Unit
}