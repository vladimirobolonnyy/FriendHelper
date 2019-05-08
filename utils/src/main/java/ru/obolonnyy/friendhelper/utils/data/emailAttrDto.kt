package ru.obolonnyy.friendhelper.utils.data

data class emailAttrDto(
    val email: String = "email@email",
    val instanceId: String = "111111",
    val device: Any = Device()
) {
    private data class Device(val os: String = "ANDROID", val version: String = "5.1.1")
}

