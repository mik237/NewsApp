package com.loc.newsapp.domain.usecases.news

import com.loc.newsapp.domain.models.Article
import com.loc.newsapp.domain.repository.NewsRepository

class SelectArticleUseCase(private val newsRepository: NewsRepository) {
    operator fun invoke(url: String): Article? {
        return newsRepository.selectArticle(url)
    }
}