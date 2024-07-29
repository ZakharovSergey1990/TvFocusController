package com.example.mytvsample.third_activity

import android.util.Log
import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@Composable
fun TvRow(
    modifier: Modifier = Modifier,
    key: String,
    graphDirection: GraphDirection = GraphDirection(),
    content: @Composable FocusableTvScope.() -> Unit
) {
    val items = remember { mutableStateListOf<FocusableComponent>() }
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val focusContainer = LocalFocusContainer.current
    val hasFocus by focusContainer.currentFocus.map{ it == key }.collectAsState(false)
    val requester = remember{ FocusRequester() }
    Row(
        modifier = modifier
        .focusRequester(requester)
        .focusable()
        .onKeyEvent {
            Log.i("FocusableColumn", " onPreviewKeyEvent: ${items.map { it.focusedId }}")
            when {
                Key.DirectionRight == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (items.isEmpty()) return@onKeyEvent false
                    if (currentIndex < items.lastIndex) {
                        currentIndex += 1
                        focusContainer.updateFocus(items[currentIndex].focusedId)
                        true
                    } else {
                        if (graphDirection.right == null) false
                        else {
                            focusContainer.updateFocus(graphDirection.right)
                            true
                        }
                    }
                }
                Key.DirectionLeft == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (items.isEmpty()) return@onKeyEvent false
                    if (currentIndex > 0) {
                        currentIndex -= 1
                        focusContainer.updateFocus(items[currentIndex].focusedId)
                        true
                    } else {
                        if (graphDirection.left == null) false
                        else {
                            focusContainer.updateFocus(graphDirection.left)
                            true
                        }
                    }
                }
                Key.DirectionDown == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (graphDirection.down == null) false
                    else {
                        focusContainer.updateFocus(graphDirection.down)
                        true
                    }
                }
                Key.DirectionUp == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (graphDirection.up == null) false
                    else {
                        focusContainer.updateFocus(graphDirection.up)
                        true
                    }
                }
                else -> false
            }
        }
    ) {
        FocusableTvScope(items).apply {
            content()
        }
    }
    LaunchedEffect(hasFocus) {
        Log.i("FocusableColumn", " LaunchedEffect: $key, hasFocus $hasFocus , ${currentIndex}")
        if(hasFocus && items.isNotEmpty()) focusContainer.updateFocus(items[currentIndex].focusedId)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TwistedTvRow(
    modifier: Modifier = Modifier,
    key: String,
    graphDirection: GraphDirection = GraphDirection(),
    content: @Composable FocusableTvScope.() -> Unit
) {
    val items = remember { mutableStateListOf<FocusableComponent>() }
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val focusContainer = LocalFocusContainer.current
    val hasFocus by focusContainer.currentFocus.map{ it == key }.collectAsState(false)
    val requester = remember{ FocusRequester() }
    Row(
        modifier = modifier
            .focusRequester(requester)
            .focusable()
            .onKeyEvent {
                Log.i("TwistedTvRow", " onPreviewKeyEvent: ${items.map { it.focusedId }} , currentIndex = $currentIndex")
                when {
                    Key.DirectionRight == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                        if (items.isEmpty()) return@onKeyEvent false
                        if (currentIndex < items.lastIndex) {
                            currentIndex += 1
                            focusContainer.updateFocus(items[currentIndex].focusedId)
                            true
                        } else {
                            currentIndex = 0
                            focusContainer.updateFocus(items[currentIndex].focusedId)
                            true
                        }
                    }
                    Key.DirectionLeft == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                        if (items.isEmpty()) return@onKeyEvent false
                        if (currentIndex > 0) {
                            currentIndex -= 1
                            focusContainer.updateFocus(items[currentIndex].focusedId)
                            true
                        } else {
                            currentIndex = items.lastIndex
                            focusContainer.updateFocus(items[currentIndex].focusedId)
                            true
                        }
                    }
                    Key.DirectionDown == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                        if (graphDirection.down == null) false
                        else {
                            focusContainer.updateFocus(graphDirection.down)
                            true
                        }
                    }
                    Key.DirectionUp == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                        if (graphDirection.up == null) false
                        else {
                            focusContainer.updateFocus(graphDirection.up)
                            true
                        }
                    }
                    else -> false
                }
            }
    ) {
        FocusableTvScope(items).apply {
            content()
        }
    }
    LaunchedEffect(hasFocus) {
        Log.i("TwistedTvRow", " LaunchedEffect: $key, hasFocus $hasFocus , ${currentIndex}")
        if(hasFocus && items.isNotEmpty()) focusContainer.updateFocus(items[currentIndex].focusedId)
    }
}