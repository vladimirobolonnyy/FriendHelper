package ru.obolonnyy.friendhelper.utils.data

data class SendEmailAttr(
    val email: String = "email@email",
    val instanceId: String = "111111",
    val device: Device = Device()
)

data class Device(val os: String = "ANDROID", val version: String = "5.1.1")

enum class AuthServerError {
    ERROR_ID_NOTFOUND;
}