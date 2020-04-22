package com.demo.ai.model

data class Movie(
    val page: Int = 0,
    val results: List<Result>? = null,
    val total_pages: Int = 0,
    val total_results: Int = 0
)