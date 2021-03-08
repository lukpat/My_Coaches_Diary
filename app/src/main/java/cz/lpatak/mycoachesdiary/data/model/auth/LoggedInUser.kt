package cz.lpatak.mycoachesdiary.data.model.auth

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
        val email: String?,
        val uid: String?
)
