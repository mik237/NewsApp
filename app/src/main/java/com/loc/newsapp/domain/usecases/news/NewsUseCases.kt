package com.loc.newsapp.domain.usecases.news

data class NewsUseCases(
    val getNews: GetNewsUseCase,
    val searchNews: SearchNewsUseCase,
    val upsertArticle: UpsertArticleUseCase,
    val deleteArticle: DeleteArticleUseCase,
    val selectArticles: SelectArticlesUseCase,
    val selectArticle: SelectArticleUseCase
)