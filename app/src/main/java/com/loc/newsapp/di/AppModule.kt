package com.loc.newsapp.di

import android.app.Application
import androidx.room.Room
import com.loc.newsapp.data.local.NewsDao
import com.loc.newsapp.data.local.NewsDatabase
import com.loc.newsapp.data.local.NewsTypeConverter
import com.loc.newsapp.data.managers.LocalUserManagerImpl
import com.loc.newsapp.data.remote.api.NewsApi
import com.loc.newsapp.data.repository.NewsRepositoryImpl
import com.loc.newsapp.domain.managers.LocalUserManager
import com.loc.newsapp.domain.repository.NewsRepository
import com.loc.newsapp.domain.usecases.appEntry.AppEntryUseCases
import com.loc.newsapp.domain.usecases.appEntry.ReadAppEntry
import com.loc.newsapp.domain.usecases.appEntry.SaveAppEntry
import com.loc.newsapp.domain.usecases.news.DeleteArticleUseCase
import com.loc.newsapp.domain.usecases.news.GetNewsUseCase
import com.loc.newsapp.domain.usecases.news.NewsUseCases
import com.loc.newsapp.domain.usecases.news.SearchNewsUseCase
import com.loc.newsapp.domain.usecases.news.SelectArticleUseCase
import com.loc.newsapp.domain.usecases.news.SelectArticlesUseCase
import com.loc.newsapp.domain.usecases.news.UpsertArticleUseCase
import com.loc.newsapp.utils.Constants.BASE_URL
import com.loc.newsapp.utils.Constants.NEWS_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManager(app: Application): LocalUserManager {
        return LocalUserManagerImpl(app)
    }


    @Provides
    @Singleton
    fun provideAppEntryUseCases(localUserManager: LocalUserManager): AppEntryUseCases {

        return AppEntryUseCases(
            saveAppEntry = SaveAppEntry(localUserManager),
            readAppEntry = ReadAppEntry(localUserManager)
        )

    }

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(newsApi: NewsApi, newsDao: NewsDao): NewsRepository {
        return NewsRepositoryImpl(newsApi = newsApi, newsDao = newsDao)
    }

    @Provides
    @Singleton
    fun provideNewsUseCases(newsRepository: NewsRepository): NewsUseCases {
        return NewsUseCases(
            getNews = GetNewsUseCase(newsRepository = newsRepository),
            searchNews = SearchNewsUseCase(newsRepository = newsRepository),
            upsertArticle = UpsertArticleUseCase(newsRepository = newsRepository),
            deleteArticle = DeleteArticleUseCase(newsRepository = newsRepository),
            selectArticles = SelectArticlesUseCase(newsRepository = newsRepository),
            selectArticle = SelectArticleUseCase(newsRepository = newsRepository)
        )
    }


    @Provides
    @Singleton
    fun provideNewsDatabase(
        app: Application
    ): NewsDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = NewsDatabase::class.java,
            name = NEWS_DATABASE
        )
            .addTypeConverter(NewsTypeConverter())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(newsDatabase: NewsDatabase): NewsDao = newsDatabase.newsDao

}