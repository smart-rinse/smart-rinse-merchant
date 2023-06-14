package labs.nusantara.smartrinsebusiness.utils

data class SessionModel(
    val ownerId: String,
    val isLaundry: Boolean,
    val email: String,
    val name: String,
    val token: String,
    val isLogin: Boolean
)