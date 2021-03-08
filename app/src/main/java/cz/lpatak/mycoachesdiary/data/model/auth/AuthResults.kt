package cz.lpatak.mycoachesdiary.data.model.auth


data class LoginResult(
        val success: LoggedInUserView? = null,
        val error: Int? = null
)

data class RegisterResult(
        val success: LoggedInUserView? = null,
        val error: Int? = null
)