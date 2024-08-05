package com.loc.newsapp.data.remote.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.loc.newsapp.data.remote.api.NewsApi
import com.loc.newsapp.domain.models.Article

class SearchNewsPagingSource(
    private val searchQuery: String,
    private val sources: String,
    private val newsApi: NewsApi
) : PagingSource<Int, Article>() {

    private var totalNewsCount = 0


    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val articlesResponse = newsApi.searchNews(searchQuery, page = page, sources = sources)
            totalNewsCount += articlesResponse.articles.size
            val articles = articlesResponse.articles.distinctBy { it.title }
            LoadResult.Page(
                data = articles,
                prevKey = null,
                nextKey = if (totalNewsCount > articlesResponse.totalResults) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(throwable = e)
        }
    }
}