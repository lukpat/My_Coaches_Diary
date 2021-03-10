package cz.lpatak.mycoachesdiary.data.model.auth


data class AuthResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)