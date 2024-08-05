package com.loc.newsapp.practice_navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.loc.newsapp.practice_navigation.destinations.NewsDetail
import com.loc.newsapp.practice_navigation.destinations.NewsHome
import com.loc.newsapp.practice_navigation.destinations.NewsHomeEvents
import com.loc.newsapp.practice_navigation.destinations.NewsObj
import com.loc.newsapp.practice_navigation.destinations.NewsSearch

@Composable
fun NewsAppGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NewsAppRoute.NewsHome.route) {
        composable(route = NewsAppRoute.NewsHome.route) {
            NewsHome { event ->
                when (event) {
                    NewsHomeEvents.ToDetail -> navController.navigate(NewsAppRoute.NewsDetail.route)
                    NewsHomeEvents.ToSearch -> navController.navigate(NewsAppRoute.NewsSearch.route)
                    is NewsHomeEvents.WithSimpleArgsToDetail -> {
                        //pass simple arguments
                        val routeWithSimpleArgs =
                            "${NewsAppRoute.NewsDetail.route}/${event.id}/${event.title}"
                        navController.navigate(routeWithSimpleArgs)
                    }

                    is NewsHomeEvents.WithObjArgsToDetail -> {
                        //pass complete object as arguments.
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "newsObj",
                            event.newsObj
                        )
                        val routeWithSimpleArgs =
                            "${NewsAppRoute.NewsDetail.route}/${event.newsObj.id}/${event.newsObj.title}"
                        navController.navigate(routeWithSimpleArgs)
                    }
                }
            }
        }

        composable(route = NewsAppRoute.NewsSearch.route) {
            NewsSearch() { navController.navigateUp() }
        }


        composable(
            route = "${NewsAppRoute.NewsDetail.route}/{id}/{title}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                nullable = false
                defaultValue = -1
            },
                navArgument("title") {
                    type = NavType.StringType
                    nullable = false
                    defaultValue = "--"
                }
            )
        ) { backstackEntry ->
            var newsObj =
                navController.previousBackStackEntry?.savedStateHandle?.get<NewsObj>("newsObj")
            if (newsObj == null) {
                val id = backstackEntry.arguments?.getInt("id") ?: -1
                val title = backstackEntry.arguments?.getString("title") ?: "--"
                newsObj = NewsObj(id = id, title = title)
            }

            NewsDetail(newsObj = newsObj) {
                navController.navigateUp()
            }
        }
    }
}