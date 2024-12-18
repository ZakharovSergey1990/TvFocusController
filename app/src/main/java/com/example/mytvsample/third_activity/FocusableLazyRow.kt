package com.example.mytvsample.third_activity

import android.util.Log
import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.foundation.focusable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.itemsIndexed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FocusableLazyRow(
    modifier: Modifier = Modifier,
    key: String,
    graphDirection: GraphDirection = GraphDirection(),
    content: @Composable FocusableLazyScope.() -> Unit
) {
    val currentIndex = rememberSaveable{ mutableStateOf(0) }
    val focusedContainer = LocalFocusContainer.current
    val hasFocus by focusedContainer.currentFocus.map{  it == key }.collectAsState(false)
    val requester = remember{ FocusRequester() }
    val scope = remember { FocusableLazyScope() }
    scope.content()
    LazyRow(modifier = modifier
        .focusRequester(requester)
        .focusable()
        .onKeyEvent {
            Log.i("FocusableLazyRow", " onPreviewKeyEvent: ${scope.items.map { it.id }}")
            when {
                Key.DirectionRight == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (currentIndex.value < scope.items.lastIndex) {
                        currentIndex.value += 1
                        focusedContainer.updateFocus(
                            scope.items[currentIndex.value].id )
                        true
                    } else {
                        if (graphDirection.right == null) false
                        else {
                            focusedContainer.updateFocus( key)
                            true
                        }
                    }
                }

                Key.DirectionLeft == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (currentIndex.value > 0) {
                        currentIndex.value -= 1
                        focusedContainer.updateFocus(
                            scope.items[currentIndex.value].id )
                        true
                    } else {
                        if (graphDirection.left == null) false
                        else {
                            focusedContainer.updateFocus( key)
                            true
                        }
                    }
                }

                Key.DirectionDown == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (graphDirection.down == null) false
                    else {
                        focusedContainer.updateFocus( key)
                        true
                    }
                }

                Key.DirectionUp == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (graphDirection.up == null) false
                    else {
                        focusedContainer.updateFocus( key)
                        true
                    }
                }

                else -> false
            }
        }
    ) {
        itemsIndexed(scope.items) { index, item ->
            FocusableItem(id = item.id) { focused ->
                item.content(focused)
            }
        }
    }
    LaunchedEffect(hasFocus) {
        Log.i("FocusableLazyRow", " LaunchedEffect: $key, hasFocus $hasFocus ")
        if(hasFocus) focusedContainer.updateFocus( scope.items[currentIndex.value].id ?: key)
    }
}
