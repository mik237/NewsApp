package com.loc.newsapp.domain.repository

import androidx.paging.PagingData
import com.loc.newsapp.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(sources: List<String>): Flow<PagingData<Article>>
    fun searchNews(searchQuery: String, sources: List<String>): Flow<PagingData<Article>>
    fun selectArticle(url: String): Article?
    suspend fun delete(article: Article)
    suspend fun upsert(article: Article)
    fun getArticles(): Flow<List<Article>>
}