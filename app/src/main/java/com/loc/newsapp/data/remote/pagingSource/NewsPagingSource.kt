package com.loc.newsapp.data.remote.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.loc.newsapp.data.remote.api.NewsApi
import com.loc.newsapp.domain.models.Article

class NewsPagingSource(private val newsApi: NewsApi, private val sources: String) :
    PagingSource<Int, Article>() {

    private var totalNewsCount = 0
    val articles = mutableListOf<Article>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val newsResponse = newsApi.getNews(page = page, sources = sources)
            totalNewsCount += newsResponse.articles.size
            val distinctArticles = newsResponse.articles.distinctBy { it.title }
            articles.addAll(distinctArticles)
            LoadResult.Page(
                data = articles,
                prevKey = null,
                nextKey = if (articles.isEmpty() || totalNewsCount >= newsResponse.totalResults) null else page + 1
            )
        } catch (e: Exception) {
            if (articles.isNotEmpty()) {
                LoadResult.Page(
                    data = articles,
                    prevKey = null,
                    nextKey = null
                )
            } else {
                LoadResult.Error(throwable = e)
            }
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}