package com.loc.newsapp

import kotlin.collections.*
import kotlin.io.*
import kotlin.jvm.*
import com.google.gson.*
import java.net.URL


/*
 * Complete the 'getArticleTitles' function below.
 *
 * The function is expected to return a STRING_ARRAY.
 * The function accepts STRING author as parameter.
 * API URL: https://jsonmock.hackerrank.com/api/articles?author=<authorName>&page=<num>
 */

fun getArticleTitles(author: String): Array<String> {
    // Write your code here
    val titles = mutableListOf<String>()
    var pageNum = 1
    var totalPages = 1

    do {

        val articleResponse = fetchArticles(author, pageNum)

        articleResponse?.let { response ->
            // Extract titles and story titles
            response.data.forEach { article ->
                val title = article.title ?: article.story_title
                title?.let { titles.add(it) }
            }

            // Update pageNum and totalPages
            totalPages = response.total_pages
            pageNum++
        }

    } while (pageNum <= totalPages)



    return titles.toTypedArray()
}

private fun fetchArticles(author: String, pageNum: Int): ArticlesResponse? {
    return try {
        // Construct the URL with the given author name and page number
        val urlString = "https://jsonmock.hackerrank.com/api/articles?author=$author&page=$pageNum"
        val url = URL(urlString)
        val response = url.readText()

        // Parse the JSON response
        fromJson<ArticlesResponse>(response)
    } catch (e: Exception) {
        null
    }
}

/*
 * Helper function that converts an object of type T -> JSON string.
 */
private inline fun <T> toJson(model: T): String = Gson().toJson(model)

/*
* Helper function that converts a JSON string -> to an object of type T.
*/
private inline fun <reified T> fromJson(json: String): T = Gson().fromJson(json, T::class.java)

fun main(args: Array<String>) {
    val result = getArticleTitles("epaga")
    println(result.joinToString("\n"))
}

data class ArticlesResponse(
    val `data`: List<ArticleData>,
    val page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int
)

data class ArticleData(
    val author: String,
    val created_at: Int,
    val num_comments: Int,
    val parent_id: Long,
    val story_id: Long,
    val story_title: String?,
    val story_url: String?,
    val title: String?,
    val url: String?
)
