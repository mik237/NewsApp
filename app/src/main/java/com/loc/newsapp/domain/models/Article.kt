package com.loc.newsapp.domain.models


import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Article(
    @SerializedName("author") val author: String?,
    @SerializedName("content") val content: String,
    @SerializedName("description") val description: String,
    @SerializedName("publishedAt") val publishedAt: String,
//    @Embedded
    @SerializedName("source") val source: Source,
    @SerializedName("title") val title: String,
    @PrimaryKey
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String
) : Parcelable