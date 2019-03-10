package ru.obolonnyy.friendhelper.utils.data

data class SendEmailResponseDto(
    val accessToken: String?,
    val refreshToken: String?,
    val repeatCount: Int?,
    val repeatBeforeDate: Long?
)