package com.loc.newsapp.data.remote.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.loc.newsapp.domain.models.Article
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsResponse(
    @SerializedName("articles") val articles: List<Article>,
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int
) : Parcelable