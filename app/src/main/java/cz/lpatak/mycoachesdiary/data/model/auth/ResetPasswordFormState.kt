package cz.lpatak.mycoachesdiary.data.model.auth

/**
 * Data validation state of the login form.
 */
data class ResetPasswordFormState(
    val usernameError: Int? = null,
    val isDataValid: Boolean = false
)
