package com.example.mytvsample

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MediaViewModel : ViewModel(), FocusedViewModel {

    private val newMedia = listOf(
        NewMedia("25", "The Fall Boy", "https://i.ytimg.com/vi/-rEo9ytSKY8/maxresdefault.jpg", true),
        NewMedia("26", "The Acolite", "https://i.ytimg.com/vi/RE4ANpRBZbI/maxresdefault.jpg"),
        NewMedia("27", "Deadpool 3", "https://avatars.dzeninfra.ru/get-zen_doc/271828/pub_656c37bae8284b6ff62dcf53_656d874720ab034c336c8ac7/scale_1200")
    )

    private val movies = listOf(
        Movie("1", "The Matrix", false),
        Movie("2", "Inception", false),
        Movie("3", "Interstellar", false),
        Movie("4", "The Green Mile", false),
        Movie("5", "Fight Club", false),
        Movie("6", "Leon", false),
        Movie("7", "Pulp Fiction", false),
        Movie("8", "Gladiator", false),
    )

    private val serials = listOf(
        Serial("9", "Game of Thrones", false),
        Serial("10", "Breaking Bad", false),
        Serial("11", "Sopranos", false),
        Serial("12", "Friends", false),
        Serial("13", "The Office", false),
        Serial("14", "Ted Lasso", false),
        Serial("15", "Sherlock", false),
        Serial("16", "House", false),
    )

    private val channels = listOf(
        Channel("17", "СТС", false),
        Channel("18", "Пятница", false),
        Channel("19", "ТНТ", false),
        Channel("20", "Звезда", false),
        Channel("21", "Матч", false),
        Channel("22", "Мир", false),
        Channel("23", "Культура", false),
        Channel("24", "Россия 24", false),
    )

    val sideMenu = listOf(
        FocusableText("Смотреть позже", "Смотреть позже", true),
        FocusableText("Настройки", "Настройки"),
        FocusableText("Подписки", "Подписки"),
        )

    private val mainGraph = Column("mainColumn")

    private val menuGraph = Column("menuColumn")

    private val _focusedId = MutableStateFlow<String?>(null)
    override val focusedId: StateFlow<String?> = _focusedId.asStateFlow()

    private val _mainData = MutableStateFlow<MainPageState>(MainPageState.Main(MainData(newMedia, movies, serials, channels)))
    val mainData: StateFlow<MainPageState> = _mainData.asStateFlow()

    init{
        sideMenu.forEach {
            menuGraph.addRow(
                Row (it.id).apply {  addNode(FocusNode(it.id))}
            )
        }
        mainGraph.addRow(CircleRow("newMediaRow").apply {
            newMedia.forEach { addNode( FocusNode(it.id) ) }
        })
        mainGraph.addRow(Row("movieRow").apply {
            movies.forEach { addNode( FocusNode(it.id) ) }
        })
        mainGraph.addRow(Row("serialRow").apply {
            serials.forEach { addNode( FocusNode(it.id) ) }
        })
        mainGraph.addRow(Row("channelRow").apply {
            channels.forEach { addNode( FocusNode(it.id) ) }
        })
    }

    override fun handleDirectionInput(direction: Direction) {

        when (_mainData.value) {
            is MainPageState.Main -> {
                val focusedId = mainGraph.moveFocus(direction, onLeft = {
                    _mainData.value = MainPageState.ShowMenu(data = _mainData.value.data, menu = sideMenu)
                    _focusedId.value = sideMenu.firstOrNull()?.id
                    Log.i("MediaViewModel", "ShowMenu focusedId = ${_focusedId.value}")
                }
                )
                Log.i("MediaViewModel", "focusedId = ${focusedId?.id}, ${_focusedId.value}")
                if(focusedId?.id != null)
                _focusedId.value = focusedId?.id
            }

            is MainPageState.ShowMenu -> {
                val focusedId = menuGraph.moveFocus(direction, onRight = {
                    _mainData.value = MainPageState.Main(data = _mainData.value.data)
                    _focusedId.value = mainGraph.focusedId()
                    Log.i("MediaViewModel", "onRight _focusedId.value = ${_focusedId.value}")
                })
                Log.i("MediaViewModel", "ShowMenu focusedId = ${focusedId?.id}, ${_focusedId.value}")
                if(focusedId?.id != null)
                _focusedId.value = focusedId?.id
            }
        }
    }
}

interface MediaItem {
    val id: String
    val title: String
    val hasFocus: Boolean
}

data class Movie(
    override val id: String,
    override val title: String,
    override val hasFocus: Boolean,
) : MediaItem

data class Channel(
    override val id: String,
    override val title: String,
    override val hasFocus: Boolean,
) : MediaItem

data class Serial(
    override val id: String,
    override val title: String,
    override val hasFocus: Boolean,
) : MediaItem

data class MainData(
    val newMedia: List<NewMedia>,
    val movies: List<Movie>,
    val serials: List<Serial>,
    val channels: List<Channel>,
)

data class NewMedia (
    val id: String,
    val title: String,
    val posterUrl: String,
    val hasFocus: Boolean = false,
)

sealed class MainPageState(open val data: MainData){
    data class Main(override val data: MainData): MainPageState(data)
    data class ShowMenu(override val data: MainData, val menu: List<FocusableText>): MainPageState(data)
}

data class FocusableText(
    val text: String,
    val id: String,
    val hasFocus: Boolean = false
)

interface FocusedViewModel{
    val focusedId: StateFlow<String?>
    fun handleDirectionInput(direction: Direction)
}