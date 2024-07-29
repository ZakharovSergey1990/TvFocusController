package com.example.mytvsample

import com.example.mytvsample.second_activity.Channel
import com.example.mytvsample.second_activity.FocusableText
import com.example.mytvsample.second_activity.Movie
import com.example.mytvsample.second_activity.NewMedia
import com.example.mytvsample.second_activity.Serial

val newMedia = listOf(
    NewMedia("25", "The Fall Boy", "https://i.ytimg.com/vi/-rEo9ytSKY8/maxresdefault.jpg", true),
    NewMedia("26", "The Acolite", "https://i0.wp.com/showspy.ru/wp-content/uploads/2023/07/image-97.png?w=1920&ssl=1"),
    NewMedia("27", "Deadpool 3", "https://avatars.dzeninfra.ru/get-zen_doc/271828/pub_656c37bae8284b6ff62dcf53_656d874720ab034c336c8ac7/scale_1200")
)

 val movies = listOf(
    Movie("1", "The Matrix", false),
    Movie("2", "Inception", false),
    Movie("3", "Interstellar", false),
    Movie("4", "The Green Mile", false),
    Movie("5", "Fight Club", false),
    Movie("6", "Leon", false),
    Movie("7", "Pulp Fiction", false),
    Movie("8", "Gladiator", false),
)

 val serials = listOf(
    Serial("9", "Game of Thrones", false),
    Serial("10", "Breaking Bad", false),
    Serial("11", "Sopranos", false),
    Serial("12", "Friends", false),
    Serial("13", "The Office", false),
    Serial("14", "Ted Lasso", false),
    Serial("15", "Sherlock", false),
    Serial("16", "House", false),
)

 val channels = listOf(
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