package com.loc.newsapp.presentation.bookmark

import com.loc.newsapp.domain.models.Article
import kotlinx.coroutines.flow.Flow

data class BookmarkState(
    val articles: List<Article> = emptyList()
)