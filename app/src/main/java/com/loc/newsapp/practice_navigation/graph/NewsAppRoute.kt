package com.loc.newsapp.practice_navigation.graph

sealed class NewsAppRoute(val route: String) {
    object NewsHome : NewsAppRoute("home")
    object NewsSearch : NewsAppRoute("search")
    object NewsDetail : NewsAppRoute("details")
}