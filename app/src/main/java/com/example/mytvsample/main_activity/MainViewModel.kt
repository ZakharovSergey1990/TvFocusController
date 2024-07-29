package com.example.mytvsample.main_activity

import com.example.mytvsample.second_activity.Channel
import com.example.mytvsample.second_activity.Movie
import com.example.mytvsample.second_activity.Serial
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel() {

    private val newMedia = listOf(
        New("25", "The Fall Boy", "https://i.ytimg.com/vi/-rEo9ytSKY8/maxresdefault.jpg"),
        New("26", "The Acolite", "https://i.ytimg.com/vi/RE4ANpRBZbI/maxresdefault.jpg"),
        New("27", "Deadpool 3", "https://avatars.dzeninfra.ru/get-zen_doc/271828/pub_656c37bae8284b6ff62dcf53_656d874720ab034c336c8ac7/scale_1200")
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

    val state = MutableStateFlow(
        MainState(
        newMedia, movies, serials, channels
    )
    )

}


data class MainState(
    val new: List<New>,
    val movies: List<Movie>,
    val serials: List<Serial>,
    val channels: List<Channel>,
)


data class New (
    val id: String,
    val title: String,
    val posterUrl: String,
)