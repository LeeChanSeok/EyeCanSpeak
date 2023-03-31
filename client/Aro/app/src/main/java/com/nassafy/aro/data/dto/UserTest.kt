package com.nassafy.aro.data.dto

data class UserTest(
    val auroraPlaces: List<Int>,
    val auroraService: Boolean,
    val email: String,
    val meteorPlaces: List<Int>,
    val meteorService: Boolean,
    val nickname: String,
    val password: String?,
    val alarm: Boolean = true
)