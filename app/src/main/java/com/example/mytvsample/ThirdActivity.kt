package com.example.mytvsample

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent.ACTION_DOWN
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mytvsample.ui.theme.MyTvSampleTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class ThirdActivity : ComponentActivity() {

    val vm = MainViewModel()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val focusState = MutableStateFlow<String>("first")

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

                        BaseFocusableContent(focusState) {
                            Row() {
                                FocusableColumn(key = "first", graphDirection = GraphDirection(right = "second")) {
                                    FocusableItem(id = "1") { hasFocus ->
                                        Button(
                                            shape = if (hasFocus) RoundedCornerShape(2.dp) else ButtonDefaults.shape,
                                            onClick = {
                                                Toast.makeText(
                                                    this@ThirdActivity,
                                                    "button 1",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }) {
                                            Text("1")
                                        }
                                    }
                                    FocusableItem(id = "2") { hasFocus ->
                                        Button(
                                            shape = if (hasFocus) RoundedCornerShape(2.dp) else ButtonDefaults.shape,
                                            onClick = {
                                                Toast.makeText(
                                                    this@ThirdActivity,
                                                    "button 2",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }) {
                                            Text("2")
                                        }
                                    }
                                    FocusableItem(id = "3") { hasFocus ->
                                        Button(
                                            shape = if (hasFocus) RoundedCornerShape(2.dp) else ButtonDefaults.shape,
                                            onClick = {
                                                Toast.makeText(
                                                    this@ThirdActivity,
                                                    "button 3",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }) {
                                            Text("3")
                                        }
                                    }
                                    FocusableItem(id = "4") { hasFocus ->

                                        Button(
                                            shape = if (hasFocus) RoundedCornerShape(2.dp) else ButtonDefaults.shape,
                                            onClick = {
                                                Toast.makeText(
                                                    this@ThirdActivity,
                                                    "button 4",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }) {
                                            Text("4")
                                        }
                                    }
                                }


                                FocusableColumn(key = "second", graphDirection = GraphDirection(left = "first")) {
                                    FocusableItem(id = "5") { hasFocus ->
                                        Button(
                                            shape = if (hasFocus) RoundedCornerShape(2.dp) else ButtonDefaults.shape,
                                            onClick = {
                                                Toast.makeText(
                                                    this@ThirdActivity,
                                                    "button 5",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }) {
                                            Text("5")
                                        }
                                    }
                                    FocusableItem(id = "6") { hasFocus ->
                                        Button(
                                            shape = if (hasFocus) RoundedCornerShape(2.dp) else ButtonDefaults.shape,
                                            onClick = {
                                                Toast.makeText(
                                                    this@ThirdActivity,
                                                    "button 6",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }) {
                                            Text("6")
                                        }
                                    }
                                    FocusableItem(id = "7") { hasFocus ->
                                        Button(
                                            shape = if (hasFocus) RoundedCornerShape(2.dp) else ButtonDefaults.shape,
                                            onClick = {
                                                Toast.makeText(
                                                    this@ThirdActivity,
                                                    "button 7",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }) {
                                            Text("7")
                                        }
                                    }
                                    FocusableItem(id = "8") { hasFocus ->
                                        Button(
                                            shape = if (hasFocus) RoundedCornerShape(2.dp) else ButtonDefaults.shape,
                                            onClick = {
                                                Toast.makeText(
                                                    this@ThirdActivity,
                                                    "button 8",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }) {
                                            Text("8")
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
fun BaseFocusableContent(
    focus: StateFlow<String>,
    modifier: Modifier = Modifier,
    content : @Composable () -> Unit
) {
    CompositionLocalProvider(LocalStateFlow provides focus) {
        Box(modifier = modifier
            .fillMaxWidth()
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FocusableColumn(
    modifier: Modifier = Modifier,
    key: String,
    graphDirection: GraphDirection = GraphDirection(),
    content: @Composable FocusableColumnScope.() -> Unit
) {
    val items = remember { mutableStateListOf<FocusableComponent>() }
    val currentIndex = remember { mutableStateOf(0) }
    val currentFocused = LocalStateFlow.current
    val hasFocus by currentFocused.map{  it == key }.collectAsState(false)
    val requester = remember{ FocusRequester() }
    Column(modifier = modifier
        .focusRequester(requester)
        .focusable()
        .focusRestorer { requester }
        .onPreviewKeyEvent {
            Log.i("FocusableColumn", " onPreviewKeyEvent: ${items.map { it.focusedId }}")
            when {
                Key.DirectionRight == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    graphDirection.right?.let { key ->
                        (currentFocused as MutableStateFlow<String>).value = key
                    }
                    true
                }
                Key.DirectionLeft == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    graphDirection.left?.let { key ->
                        (currentFocused as MutableStateFlow<String>).value = key
                    }
                    true
                }
                Key.DirectionDown == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    Log.i(
                        "FocusableColumn",
                        " onPreviewKeyEvent: DirectionDown ${currentIndex.value}, ${items.lastIndex}"
                    )
                    if (currentIndex.value < items.lastIndex) {
                        currentIndex.value += 1
                        (currentFocused as MutableStateFlow<String>).value =
                            items[currentIndex.value].focusedId
                    } else {
                        graphDirection.down?.let { key ->
                            Log.i("FocusableColumn", " onPreviewKeyEvent: graphDirection.down $key")
                            (currentFocused as MutableStateFlow<String>).value = key
                        }
                    }
                    true
                }
                Key.DirectionUp == it.key && it.nativeKeyEvent.action == ACTION_DOWN -> {
                    if (currentIndex.value > 0) {
                        currentIndex.value -= 1
                        (currentFocused as MutableStateFlow<String>).value =
                            items[currentIndex.value].focusedId
                    } else {
                        graphDirection.up?.let { key ->
                            Log.i("FocusableColumn", " onPreviewKeyEvent: graphDirection.up $key")
                            (currentFocused as MutableStateFlow<String>).value = key
                        }
                    }
                    true
                }
                else -> false
            }
        }
    ) {
        FocusableColumnScope(items).apply {
            content()
        }
    }
    LaunchedEffect(hasFocus) {
        Log.i("FocusableColumn", " LaunchedEffect: $key, hasFocus $hasFocus ")
        if(!hasFocus) return@LaunchedEffect
        (currentFocused as MutableStateFlow<String>).value = items[currentIndex.value].focusedId ?: key
    }
}

class FocusableColumnScope(private val items: MutableList<FocusableComponent>) {
    @Composable
    fun FocusableItem(id: String, content: @Composable (Boolean) -> Unit) {
        val currentFocused = LocalStateFlow.current
        val hasFocus by currentFocused.map{  it == id }.collectAsState(false)
        val requester = remember { FocusRequester() }
        val focusableComponent = remember {
            object : FocusableComponent {
                override val focusedId = id
            }
        }
        LaunchedEffect(id) {
            items.add(focusableComponent)
        }

        Box(modifier = Modifier
            .focusGroup()
            .focusRequester(requester)
            .onFocusChanged {
                Log.i("FocusableColumnScope", "onFocusChanged $id, $it")
            }
            .focusable()
        ) {
            content(hasFocus)
        }
        LaunchedEffect(hasFocus) {
            Log.i("FocusableColumnScope", "LaunchedEffect ${hasFocus}, $id")
            if (!hasFocus) return@LaunchedEffect
            requester.requestFocus()
        }
    }
}

interface FocusableComponent {
    val focusedId: String
}

val LocalStateFlow = compositionLocalOf<StateFlow<String>> { error("No StateFlow provided") }

data class GraphDirection(
    val left: String? = null,
    val right: String? = null,
    val up: String? = null,
    val down: String? = null,
)