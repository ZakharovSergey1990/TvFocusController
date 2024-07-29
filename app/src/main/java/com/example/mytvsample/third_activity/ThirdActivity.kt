package com.example.mytvsample.third_activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.rememberDrawerState
import coil.compose.AsyncImage
import com.example.mytvsample.main_activity.MainViewModel
import com.example.mytvsample.main_activity.Screens
import com.example.mytvsample.second_activity.MediaItem
import com.example.mytvsample.ui.theme.MyTvSampleTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map


class ThirdActivity : ComponentActivity() {

    val vm = MainViewModel()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val controller = rememberNavController()

            val menu = listOf(
                "Профиль" to Icons.Filled.AccountCircle,
                "Настройки" to Icons.Filled.Settings, "Избранное" to Icons.Filled.Favorite
            )

            val state by vm.state.collectAsState()

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
                        BaseFocusableContent("main_column") {
                            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                            val focusContainer = LocalFocusContainer.current
                            val showMenu by focusContainer.currentFocus.map { it == "side_menu" }
                                .collectAsState(false)

                            LaunchedEffect(showMenu) {
                                Log.i("BaseFocusableContent", "show menu :$showMenu")
                                if (showMenu) drawerState.setValue(DrawerValue.Open)
                                else drawerState.setValue(DrawerValue.Closed)
                            }

                            ModalNavigationDrawer(
                                drawerState = drawerState,
                                drawerContent = { drawerValue ->
                                    TvColumn(
                                        key = "side_menu",
                                        graphDirection = GraphDirection(right = "main_column"),
                                        modifier = Modifier
                                            .background(Color.Gray)
                                            .fillMaxHeight()
                                            .padding(4.dp),
                                    ) {
                                        menu.forEachIndexed() { index, item ->
                                            FocusableItem(id = item.first) { focused ->
                                                NavigationDrawerItem(
                                                    modifier = Modifier,
                                                    selected = focused,
                                                    onClick = {},
                                                    leadingContent = {
                                                        Icon(
                                                            imageVector = item.second,
                                                            contentDescription = null,
                                                        )
                                                    }
                                                ) {
                                                    Text(item.first)
                                                }
                                            }
                                        }
                                    }
                                }
                            ) {
                                FocusableLazyColumn(
                                    modifier = Modifier.padding(start = 72.dp),
                                    key = "main_column",
                                    graphDirection = GraphDirection(left = "side_menu")
                                ) {
                                    item("new") {
                                        Column(
                                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally) {
                                            TwistedTvRow(key = "new") {
                                                state.new.forEach { new ->
                                                    FocusableItem(id = new.id) { hasFocus ->
                                                        AsyncImage(
                                                            model = new.posterUrl,
                                                            modifier = Modifier
                                                                .size(
                                                                    if (!hasFocus) DpSize(
                                                                        200.dp,
                                                                        100.dp
                                                                    )
                                                                    else DpSize(230.dp, 115.dp)
                                                                )
                                                                .clickable { }
                                                            ,
                                                            contentDescription = null
                                                        )
                                                    }
                                                }
                                            }
                                            TwistedTvRow(modifier = Modifier.padding(top = 16.dp), key = "new") {
                                                state.new.forEach { new ->
                                                    FocusableItem(id = new.id) { hasFocus ->
                                                        Surface(
                                                            modifier = Modifier.size(16.dp).padding(4.dp),
                                                            shape = CircleShape,
                                                            colors = SurfaceDefaults.colors(containerColor = if(hasFocus) Color.DarkGray else Color.LightGray)
                                                        ) {}
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    item("movie") {
                                        Column {
                                            Text("Фильмы", modifier = Modifier.padding(start = 16.dp))
                                            FocusableLazyRow(key = "movie") {
                                                state.movies.forEach {
                                                    item(it.id) { hasFocus ->
                                                        TvItem(
                                                            item = it,
                                                            hasFocus = hasFocus,
                                                            modifier = Modifier
                                                        ) {
                                                            controller.navigate(Screens.Film.name)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    item("serials") {
                                        Column {
                                            Text(text = "Сериалы", modifier = Modifier.padding(start = 16.dp))
                                            FocusableLazyRow(key = "serials") {
                                                state.serials.forEach {
                                                    item(it.id) { hasFocus ->
                                                        TvItem(
                                                            item = it,
                                                            hasFocus = hasFocus,
                                                            modifier = Modifier
                                                        ) {
                                                            controller.navigate(Screens.Film.name)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    item("channels") {
                                        Column {
                                            Text("Каналы", modifier = Modifier.padding(start = 16.dp))
                                            FocusableLazyRow(key = "channels") {
                                                state.channels.forEach {
                                                    item(it.id) { hasFocus ->
                                                        TvItem(
                                                            item = it,
                                                            hasFocus = hasFocus,
                                                            modifier = Modifier
                                                        ) {
                                                            controller.navigate(Screens.Film.name)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TvItem(item: MediaItem, hasFocus: Boolean, modifier: Modifier, onClick: (MediaItem) -> Unit) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .size(200.dp, 100.dp)
            .background(if (hasFocus) Color.Red else Color.LightGray)
            .clickable { onClick(item) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = item.title, color = Color.White)
    }
}