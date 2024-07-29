package com.example.mytvsample.third_activity

import android.util.Log
import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.foundation.focusable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.itemsIndexed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FocusableLazyColumn(
    modifier: Modifier = Modifier,
    key: String,
    graphDirection: GraphDirection = GraphDirection(),
    content: @Composable FocusableLazyScope.() -> Unit
) {
    val currentIndex = rememberSaveable { mutableIntStateOf(0) }
    val focusContainer = LocalFocusContainer.current
    val hasFocus by focusContainer.currentFocus.map{ it == key }.collectAsState(false)
    val requester = remember{ FocusRequester() }
    val scope = remember { FocusableLazyScope() }

    scope.content()

    TvLazyColumn(modifier = modifier
        .focusRequester(requester)
        .focusable()
        .onKeyEvent {
            Log.i(
                "FocusableLazyColumn",
                " onPreviewKeyEvent: ${it}, ${scope.items.map { it.id }}, $graphDirection"
            )
            when {
                Key.DirectionRight == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (graphDirection.right == null) false
                    else {
                        focusContainer.updateFocus(graphDirection.right)
                        true
                    }
                }

                Key.DirectionLeft == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (graphDirection.left == null) false
                    else {
                        focusContainer.updateFocus(graphDirection.left)
                        true
                    }
                }

                Key.DirectionDown == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (currentIndex.value < scope.items.lastIndex) {
                        currentIndex.value += 1
                        focusContainer.updateFocus(
                            scope.items[currentIndex.value].id
                        )
                        true
                    } else {
                        if (graphDirection.down == null) false
                        else {
                            focusContainer.updateFocus(graphDirection.down)
                            true
                        }
                    }
                }

                Key.DirectionUp == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (currentIndex.value > 0) {
                        currentIndex.value -= 1
                        focusContainer.updateFocus(
                            scope.items[currentIndex.value].id
                        )
                        true
                    } else {
                        if (graphDirection.up == null) false
                        else {
                            focusContainer.updateFocus(graphDirection.up)
                            true
                        }
                    }
                }

                else -> {
                    Log.i("FocusableLazyColumn", " onPreviewKeyEvent: else ")
                    false
                }
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
        Log.i("FocusableLazyColumn", " LaunchedEffect: $key, hasFocus $hasFocus , currentIndex ${currentIndex.value}")
        if(hasFocus) focusContainer.updateFocus( scope.items[currentIndex.value].id )
    }
}