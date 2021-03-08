package cz.lpatak.mycoachesdiary.ui.auth.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.auth.*
import cz.lpatak.mycoachesdiary.data.repositories.AuthRepository
import cz.lpatak.mycoachesdiary.data.source.LoginResult.Error
import cz.lpatak.mycoachesdiary.data.source.LoginResult.Success
import cz.lpatak.mycoachesdiary.util.NoConnectivityException
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import kotlinx.coroutines.launch

class AuthViewModel(
        private val context: Context,
        private val authRepository: AuthRepository,
        private val preferenceManager: PreferenceManger
) :
        ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult


    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm
    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult


    fun login(username: String, password: String) = viewModelScope.launch {
        val result = authRepository.login(username, password)

        if (result is Success) {
            _loginResult.value =
                    LoginResult(
                            success = LoggedInUserView(
                                    email = result.data.email
                                            ?: context.getString(R.string.unknown),
                                    uid = result.data.uid ?: "UID"
                            )
                    )
        } else {
            val exception = (result as Error).exception
            if (exception is NoConnectivityException) {
                _loginResult.value =
                        LoginResult(error = R.string.message_internet_unavailable)
            } else {
                _loginResult.value =
                        LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun register(username: String, password: String) = viewModelScope.launch {
        val result = authRepository.register(username, password)

        if (result is Success) {
            _registerResult.value =
                    RegisterResult(
                            success = LoggedInUserView(
                                    email = result.data.email
                                            ?: context.getString(R.string.unknown),
                                    uid = result.data.uid ?: "UID"
                            )
                    )
        } else {
            val exception = (result as Error).exception
            if (exception is NoConnectivityException) {
                _registerResult.value =
                        RegisterResult(error = R.string.message_internet_unavailable)
            } else {
                _registerResult.value =
                        RegisterResult(error = R.string.login_failed)
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value =
                    LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value =
                    LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value =
                    LoginFormState(isDataValid = true)
        }
    }

    fun registerDataChanged(username: String, password: String, password2: String) {
        if (!isUserNameValid(username)) {
            _registerForm.value =
                    RegisterFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value =
                    RegisterFormState(passwordError = R.string.invalid_password)
        } else if (!isPasswordValid(password2)) {
            _registerForm.value =
                    RegisterFormState(passwordError2 = R.string.invalid_password)
        } else if (passwordMatch(password, password2)) {
            _registerForm.value =
                    RegisterFormState(passwordError = R.string.password_match_error)
        } else {
            _registerForm.value =
                    RegisterFormState(isDataValid = true)
        }
    }


    private fun isUserNameValid(username: String): Boolean {
        return if (username.isNotBlank() && username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            false
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun passwordMatch(password: String, password2: String): Boolean {
        return password != password2
    }

    fun getCurrentLoggedInUser(): LiveData<LoggedInUserView>? {
        val userdata = MutableLiveData<LoggedInUserView>()
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val email = it.email ?: context.getString(R.string.unknown)
            val uid = it.uid

            userdata.value =
                    LoggedInUserView(
                            email,
                            uid
                    )
            return userdata
        }

        return null
    }


    fun logOut() {
        FirebaseAuth.getInstance().signOut()
        preferenceManager.clearAllPreferences()
    }

    fun getCurrentUserEmail(): String {
        return authRepository.getUserEmail()
    }
}
