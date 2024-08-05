package com.loc.newsapp.presentation.details

import com.loc.newsapp.domain.models.Article

sealed class DetailEvents {
    data class UpsertDeleteArticle(val article: Article) : DetailEvents()

    object DeleteSideEffect : DetailEvents()
}