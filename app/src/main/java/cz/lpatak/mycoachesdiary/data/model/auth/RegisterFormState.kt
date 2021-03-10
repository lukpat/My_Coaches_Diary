package cz.lpatak.mycoachesdiary.data.model.auth

/**
 * Data validation state of the login form.
 */
data class RegisterFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val passwordError2: Int? = null,
    val passwordMatchError: Int? = null,
    val isDataValid: Boolean = false
)
