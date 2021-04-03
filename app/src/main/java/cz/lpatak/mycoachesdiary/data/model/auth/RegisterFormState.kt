package cz.lpatak.mycoachesdiary.data.model.auth


data class RegisterFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val passwordError2: Int? = null,
    val passwordMatchError: Int? = null,
    val isDataValid: Boolean = false
)
