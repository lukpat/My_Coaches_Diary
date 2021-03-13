package cz.lpatak.mycoachesdiary.data.model.auth

/**
 * User details post authentication that is exposed to the UI
 */

data class LoggedInUserView(
        val email: String,
        val uid: String
)
