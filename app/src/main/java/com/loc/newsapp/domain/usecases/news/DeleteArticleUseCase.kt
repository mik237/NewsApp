package com.loc.newsapp.domain.usecases.news

import com.loc.newsapp.data.local.NewsDao
import com.loc.newsapp.domain.models.Article
import com.loc.newsapp.domain.repository.NewsRepository

class DeleteArticleUseCase(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(article: Article) {
        newsRepository.delete(article)
    }
}