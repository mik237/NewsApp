package com.loc.newsapp.domain.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Source(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
) : Parcelable