package com.loc.newsapp.presentation.news_navigator.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.loc.newsapp.R
import com.loc.newsapp.domain.models.Article
import com.loc.newsapp.presentation.bookmark.BookmarkScreen
import com.loc.newsapp.presentation.bookmark.BookmarkViewModel
import com.loc.newsapp.presentation.details.DetailEvents
import com.loc.newsapp.presentation.details.DetailScreen
import com.loc.newsapp.presentation.details.DetailsViewModel
import com.loc.newsapp.presentation.home.HomeScreen
import com.loc.newsapp.presentation.home.HomeViewModel
import com.loc.newsapp.presentation.navgraph.Route
import com.loc.newsapp.presentation.search.SearchScreen
import com.loc.newsapp.presentation.search.SearchViewModel

@SuppressLint("RememberReturnType")
@Composable
fun NewsNavigator(modifier: Modifier = Modifier) {
    val bottomNavigationItems = listOf(
        BottomNavigationItem("Home", icon = R.drawable.ic_home),
        BottomNavigationItem("Search", icon = R.drawable.ic_search),
        BottomNavigationItem("Bookmark", icon = R.drawable.ic_bookmark),
    )


    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    selectedItem = when (backStackState?.destination?.route) {
        Route.HomeScreen.route -> 0
        Route.SearchScreen.route -> 1
        Route.BookmarksScreen.route -> 2
        else -> 0
    }

    val isBottomBarVisible = remember(key1 = backStackState) {
        backStackState?.destination?.route == Route.HomeScreen.route ||
                backStackState?.destination?.route == Route.SearchScreen.route ||
                backStackState?.destination?.route == Route.BookmarksScreen.route
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {
                NewsBottomNavigation(
                    items = bottomNavigationItems,
                    selected = selectedItem,
                    onItemClick = { index ->
                        when (index) {
                            0 -> navigateToTab(
                                navController = navController,
                                route = Route.HomeScreen.route
                            )

                            1 -> navigateToTab(
                                navController = navController,
                                route = Route.SearchScreen.route
                            )

                            2 -> navigateToTab(
                                navController = navController,
                                route = Route.BookmarksScreen.route
                            )
                        }
                    })
            }
        }
    ) { paddingValues ->
        val bottomPadding = paddingValues.calculateBottomPadding()
        NavHost(
            modifier = modifier.padding(bottom = bottomPadding),
            navController = navController,
            startDestination = Route.HomeScreen.route
        ) {
            composable(route = Route.HomeScreen.route) {
                val homeViewModel: HomeViewModel = hiltViewModel()
                val articles = homeViewModel.news.collectAsLazyPagingItems()
                HomeScreen(
                    articles = articles,
                    navigateToSearch = {
                        navigateToTab(
                            navController = navController,
                            route = Route.SearchScreen.route
                        )
                    },
                    navigateToDetail = { article ->
                        navigateToDetail(
                            navController = navController,
                            article = article
                        )
                    }
                )
            }

            composable(route = Route.SearchScreen.route) {
                val searchVM: SearchViewModel = hiltViewModel()
                SearchScreen(
                    state = searchVM.state.value,
                    event = searchVM::onEvent
                ) { article ->
                    navigateToDetail(navController = navController, article = article)
                }
            }

            composable(route = Route.BookmarksScreen.route) {
                val bookmarkVM: BookmarkViewModel = hiltViewModel()

                BookmarkScreen(state = bookmarkVM.state.value) { article ->
                    navigateToDetail(navController = navController, article = article)
                }
            }

            composable(route = Route.DetailsScreen.route) {
                val detailVM: DetailsViewModel = hiltViewModel()
                if(detailVM.sideEffect != null){
                    Toast.makeText(LocalContext.current, detailVM.sideEffect, Toast.LENGTH_SHORT).show()
                    detailVM.onEvent(DetailEvents.DeleteSideEffect)
                }
                navController.previousBackStackEntry?.savedStateHandle?.get<Article?>("article")
                    ?.let { article ->
                        DetailScreen(
                            article = article,
                            events = detailVM::onEvent,
                            navigateUp = { navController.navigateUp() }
                        )
                    }
            }
        }
    }
}


fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { homeScreen ->
            popUpTo(homeScreen) { saveState = true }
            restoreState = true
            launchSingleTop = true
        }
    }
}

fun navigateToDetail(navController: NavController, article: Article) {
    navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
    navController.navigate(route = Route.DetailsScreen.route)
}