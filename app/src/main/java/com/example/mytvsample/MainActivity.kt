package com.example.mytvsample

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent.ACTION_UP
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.Cancel
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import androidx.tv.material3.DrawerState
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Surface
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.rememberDrawerState
import coil.compose.AsyncImage
import com.example.mytvsample.ui.theme.MyTvSampleTheme

class MainActivity : ComponentActivity() {

    val vm = MainViewModel()


    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val list = mutableListOf<Film>()
            for (i in 0..20) {
                list.add(Film(i, "title $i"))
            }
            val controller = rememberNavController()
            MyTvSampleTheme {
                NavHost(navController = controller, startDestination = Screens.Films.name) {
                    composable(Screens.Film.name) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Film Description")
                        }
                    }
                    composable(Screens.Films.name) {
                        val mainRequester = remember { FocusRequester() }
                        val selectedMenu by remember { mutableStateOf(0) }
                        val menu = listOf("Смотреть позже", "Настройки", "Подписки")
                        val state by vm.state.collectAsState()
                        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = { drawerValue ->
                                if (drawerValue == DrawerValue.Closed) return@ModalNavigationDrawer
                                Column(
                                    Modifier
                                        .background(Color.Gray)
                                        .fillMaxHeight()
                                        .padding(12.dp)
                                        .focusProperties { right = mainRequester },
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    menu.forEachIndexed() { index, item ->
                                        NavigationDrawerItem(
                                            modifier = Modifier,
                                            selected = selectedMenu == index,
                                            onClick = {},
                                            leadingContent = {
                                                Icon(
                                                    imageVector = Icons.Filled.AccountCircle,
                                                    contentDescription = null,
                                                )
                                            }
                                        ) {
                                            Text(item)
                                        }
                                    }
                                }
                            }
                        ) {
                            TvLazyColumn(
                                modifier = Modifier
                                    .focusRequester(mainRequester)
                                    .focusRestorer { mainRequester },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                item {
                                    NewRow(data = state.new)
                                }
                                item {
                                    Text(
                                        text = "Фильмы",
                                        modifier = Modifier.padding(
                                            vertical = 4.dp,
                                            horizontal = 32.dp
                                        )
                                    )
                                }
                                item {
                                    MainMediaRow(state.movies,
                                        onClick = {
                                            mainRequester.saveFocusedChild()
                                            controller.navigate(Screens.Film.name)
                                        },
                                        onLeft = {
                                            drawerState.setValue(DrawerValue.Open)
                                        }
                                    )
                                }
                                item {
                                    Text(
                                        text = "Сериалы",
                                        modifier = Modifier.padding(
                                            vertical = 4.dp,
                                            horizontal = 32.dp
                                        )
                                    )
                                }
                                item {
                                    MainMediaRow(state.serials,
                                        onClick = {
                                            mainRequester.saveFocusedChild()
                                            controller.navigate(Screens.Film.name)
                                        },
                                        onLeft = {
                                            drawerState.setValue(DrawerValue.Open)
                                        }
                                    )
                                }
                                item {
                                    Text(
                                        text = "Каналы",
                                        modifier = Modifier.padding(
                                            vertical = 4.dp,
                                            horizontal = 32.dp
                                        )
                                    )
                                }
                                item {
                                    MainMediaRow(state.channels,
                                        onClick = {
                                            controller.navigate(Screens.Film.name)
                                            mainRequester.saveFocusedChild()
                                        },
                                        onLeft = {
                                            drawerState.setValue(DrawerValue.Open)
                                        }
                                    )
                                }
                            }
                            LaunchedEffect(Unit) {
                                Log.i("MainActivity", "TvLazyColumn restoreFocusedChild")
                                mainRequester.restoreFocusedChild()
                            }
                        }
                    }
                }
            }
        }
    }
}


data class Film(
    val id: Int,
    val title: String,
)

enum class Screens {
    Films, Film
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewRow(modifier: Modifier = Modifier, data: List<New>) {
    val (firstItemRequester, lastItemRequester, columnRequester) = remember { FocusRequester.createRefs() }
    var focusIndex by remember { mutableStateOf<Int?>(null) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            data.forEachIndexed() { index, new ->
                AsyncImage(
                    model = new.posterUrl,
                    modifier = Modifier
                        .size(
                            if (focusIndex != index) DpSize(200.dp, 100.dp)
                            else DpSize(230.dp, 115.dp)
                        ),
                    contentDescription = null
                )
            }
        }
        Row(
            modifier = Modifier
                .focusRequester(columnRequester)
        ) {
            val firstModifier = Modifier
                .focusRequester(focusRequester = firstItemRequester)
                .focusProperties {
                    left = lastItemRequester
                }

            val lastModifier = Modifier
                .focusRequester(focusRequester = lastItemRequester)
                .focusProperties {
                    right = firstItemRequester
                }

            data.forEachIndexed() { index, new ->
                val buttonModifier = when (index) {
                    0 -> firstModifier
                    data.lastIndex -> lastModifier
                    else -> Modifier
                }
                IconButton(
                    onClick = { /*TODO*/ }, modifier = buttonModifier
                        .onFocusChanged { if (it.hasFocus) focusIndex = index }
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle, contentDescription = null,
                        tint = if (focusIndex == index) Color.DarkGray else Color.LightGray
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainMediaRow(
    items: List<MediaItem>, modifier: Modifier = Modifier,
    onClick: (MediaItem) -> Unit,
    onLeft: () -> Unit,
) {
    Log.i("MainActivity", "items $items")
    val firstItemRequester = remember { FocusRequester() }
    val rowRequester = remember { FocusRequester() }
    TvLazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .focusRequester(rowRequester)
            .focusRestorer { rowRequester }
            .onFocusChanged {
                if (it.isFocused && it.hasFocus && !it.isCaptured)
                    rowRequester.restoreFocusedChild()
            }
    ) {
        itemsIndexed(items) { index, item ->
            val itemModifier = if (index == 0) {
                Modifier
                    .focusRequester(firstItemRequester)
                    .onPreviewKeyEvent {
                        when {
                            Key.DirectionLeft == it.key -> {
                                onLeft()
                                true
                            }

                            else -> false
                        }
                    }
            } else {
                Modifier
            }
            MainMediaItem(item, itemModifier, onClick = {
                rowRequester.saveFocusedChild()
                onClick(it)
            })
        }
    }
}

@Composable
fun MainMediaItem(item: MediaItem, modifier: Modifier, onClick: (MediaItem) -> Unit) {
    var hasFocus by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .padding(8.dp)
            .size(200.dp, 100.dp)
            .background(if (hasFocus) Color.Red else Color.LightGray)
            .onFocusChanged {
                hasFocus = it.hasFocus
            }
            .clickable { onClick(item) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = item.title, color = Color.White)
    }
}