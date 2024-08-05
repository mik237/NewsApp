package com.loc.newsapp.practice_navigation.destinations

import android.os.Parcelable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsHome(
    modifier: Modifier = Modifier,
    navigate: (NewsHomeEvents) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Home Screen") })
        },
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Home Screen Route")
            Button(onClick = { navigate(NewsHomeEvents.ToDetail) }) {
                Text(text = "Move to Detail")
            }

            Button(onClick = { navigate(NewsHomeEvents.ToSearch) }) {
                Text(text = "Move to Search")
            }

            Button(onClick = {
                navigate(
                    NewsHomeEvents.WithSimpleArgsToDetail(
                        1,
                        "Simple Title as Args"
                    )
                )
            }) {
                Text(text = "Move to Detail With Simple args")
            }

            Button(onClick = {
                navigate(
                    NewsHomeEvents.WithObjArgsToDetail(
                        NewsObj(
                            id = 2,
                            title = "Object Title as args",
                        )
                    )
                )
            }) {
                Text(text = "Move to Detail with Obj args")
            }
        }
    }
}


sealed class NewsHomeEvents {
    object ToDetail : NewsHomeEvents()
    object ToSearch : NewsHomeEvents()
    data class WithSimpleArgsToDetail(val id: Int, val title: String) : NewsHomeEvents()
    data class WithObjArgsToDetail(val newsObj: NewsObj) : NewsHomeEvents()
}

@Parcelize
data class NewsObj(
    val id: Int,
    val title: String,
    val source: String = "abc-news",
    val timestamp: Long = 123456
) : Parcelable