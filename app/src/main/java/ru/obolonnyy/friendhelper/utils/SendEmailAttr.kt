package ru.obolonnyy.friendhelper.utils

data class SendEmailAttr(
    val email: String = "email@email",
    val instanceId: String = "111111",
    val device: Device = Device()
)

data class Device(val os: String = "ANDROID", val version: String = "5.1.1")

data class SendEmailResponseDto(
    val accessToken: String?,
    val refreshToken: String?,
    val repeatCount: Int?,
    val repeatBeforeDate: Long?
)

enum class AuthServerError {
    ERROR_ID_NOTFOUND;
}