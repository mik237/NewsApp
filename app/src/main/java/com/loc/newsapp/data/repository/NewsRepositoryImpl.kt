package com.loc.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.loc.newsapp.data.local.NewsDao
import com.loc.newsapp.data.remote.pagingSource.NewsPagingSource
import com.loc.newsapp.data.remote.api.NewsApi
import com.loc.newsapp.data.remote.pagingSource.SearchNewsPagingSource
import com.loc.newsapp.domain.models.Article
import com.loc.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
) : NewsRepository {

    override fun getNews(sources: List<String>): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                NewsPagingSource(
                    newsApi = newsApi,
                    sources = sources.joinToString(separator = ",")
                )
            }
        ).flow
    }

    override fun searchNews(searchQuery: String, sources: List<String>): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                SearchNewsPagingSource(
                    searchQuery = searchQuery,
                    sources = sources.joinToString(separator = ","),
                    newsApi = newsApi
                )
            }
        ).flow
    }

    override fun selectArticle(url: String): Article? {
        return newsDao.selectArticle(url)
    }

    override suspend fun delete(article: Article) {
        newsDao.delete(article)
    }

    override suspend fun upsert(article: Article) {
        newsDao.upsert(article)
    }

    override fun getArticles(): Flow<List<Article>> {
        return newsDao.getArticles()
    }
}