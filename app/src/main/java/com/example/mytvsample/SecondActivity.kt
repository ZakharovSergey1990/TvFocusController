package com.example.mytvsample

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent.ACTION_DOWN
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Surface
import coil.compose.AsyncImage
import com.example.mytvsample.ui.theme.MyTvSampleTheme
import kotlinx.coroutines.flow.StateFlow

class SecondActivity : ComponentActivity() {

    val vm = MediaViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTvSampleTheme {
                val controller = rememberNavController()
                NavHost(navController = controller, startDestination = Screens.Films.name) {
                    composable(Screens.Film.name) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Description")
                        }
                    }
                    composable(Screens.Films.name) {
                        FocusableContent(vm = vm) {
                            MediaPage(vm) {
                                Toast.makeText(
                                    this@SecondActivity,
                                    "on ${it.title} clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                                controller.navigate(Screens.Film.name)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MediaPage(viewModel: MediaViewModel, onClick: (MediaItem) -> Unit) {
    val state by viewModel.mainData.collectAsState()
    Log.i("MainPage", "movies = ${state.data.movies}")
    Log.i("MainPage", "serials = ${state.data.serials}")
    Log.i("MainPage", "channels = ${state.data.channels}")
    ModalNavigationDrawer(
        drawerContent = {
            Column(
                Modifier
                    .background(Color.Gray)
                    .fillMaxHeight()
                    .padding(12.dp)
                    .selectableGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                (state as? MainPageState.ShowMenu)?.menu?.forEachIndexed() { index, item ->
                    val (text, icon) = item
                    Focusable(item.id) { hasFocus ->
                        NavigationDrawerItem(
                            modifier = Modifier,
                            selected = hasFocus,
                            onClick = {

                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = null,
                                )
                            }
                        ) {
                            Text(text)
                        }
                    }
                }
            }
        }
    ) {
        TvLazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                NewMediaRow(data = state.data.newMedia)
            }
            item {
                Column {
                    Text(
                        text = "Фильмы",
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 32.dp)
                    )
                    MediaRow(state.data.movies, onClick = onClick)
                }
            }
            item {
                Column {
                    Text(
                        text = "Сериалы",
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 32.dp)
                    )
                    MediaRow(state.data.serials, onClick = onClick)
                }
            }
            item {
                Column {
                    Text(
                        text = "Каналы",
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 32.dp)
                    )
                    MediaRow(state.data.channels, onClick = onClick)
                }
            }
        }
    }
}

@Composable
fun MediaRow(items: List<MediaItem>, onClick: (MediaItem) -> Unit) {
    Log.i("MediaRow", "items $items")
    TvLazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(items) { item ->
            MediaItemView(item, onClick = onClick)
        }
    }
}

@Composable
fun MediaItemView(item: MediaItem, onClick: (MediaItem) -> Unit) {
    Focusable(item.id) { hasFocus ->
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(200.dp, 100.dp)
                .background(if (hasFocus) Color.Red else Color.LightGray)
                .clickable { onClick(item) },
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.title, color = Color.White)
        }
    }
}

@Composable
fun NewMediaRow(modifier: Modifier = Modifier, data: List<NewMedia>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            data.forEach { new ->
                Focusable(new.id) { hasFocus ->
                    AsyncImage(
                        model = new.posterUrl,
                        modifier = Modifier.size(
                            if (!hasFocus) DpSize(200.dp, 100.dp)
                            else DpSize(230.dp, 115.dp)
                        ),
                        contentDescription = null
                    )
                }
            }
        }
        Row {
            data.forEach { new ->
                Focusable(new.id) { hasFocus ->
                    IconButton(
                        onClick = { /*TODO*/ }, modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle, contentDescription = null,
                            tint = if (hasFocus) Color.DarkGray else Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

@Composable
fun Focusable(focusId: String, modifier: Modifier = Modifier, content: @Composable (Boolean) -> Unit) {
    val currentFocusedId by FocusedId.current.collectAsState()
    val requester = remember { FocusRequester() }
    Box(
        modifier
            .focusRequester(requester)
            .focusable()
    ) {
        content(currentFocusedId == focusId)
    }
    LaunchedEffect(currentFocusedId) {
        if (currentFocusedId == focusId) requester.requestFocus()
    }
}

@Composable
fun FocusableContent(
    modifier: Modifier = Modifier,
    vm: FocusedViewModel,
    content : @Composable () -> Unit
) {
    CompositionLocalProvider(FocusedId provides vm.focusedId) {
        Box(modifier = modifier
            .fillMaxWidth()
            .onPreviewKeyEvent {
                Log.i("SecondActivity", " onPreviewKeyEvent: $it")
                when {
                    Key.DirectionRight == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                        vm.handleDirectionInput(Direction.RIGHT)
                        true
                    }

                    Key.DirectionLeft == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                        vm.handleDirectionInput(Direction.LEFT)
                        true
                    }

                    Key.DirectionDown == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                        vm.handleDirectionInput(Direction.DOWN)
                        true
                    }

                    Key.DirectionUp == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                        vm.handleDirectionInput(Direction.UP)
                        true
                    }

                    else -> false
                }
            }
        ) {
            content()
        }
    }
}

val FocusedId = compositionLocalOf<StateFlow<String?>> { error("No active focused found!") }
