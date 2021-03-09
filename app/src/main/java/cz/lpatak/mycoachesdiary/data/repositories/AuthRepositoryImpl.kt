package cz.lpatak.mycoachesdiary.data.repositories

import cz.lpatak.mycoachesdiary.data.model.auth.LoggedInUser
import cz.lpatak.mycoachesdiary.data.source.AuthDataSource
import cz.lpatak.mycoachesdiary.data.source.LoginResult


class AuthRepositoryImpl(private val dataSource: AuthDataSource) : AuthRepository {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set


    init {
        user = null
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }

    override suspend fun login(username: String, password: String): LoginResult<LoggedInUser> {

        val result = dataSource.login(username, password)

        if (result is LoginResult.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    override suspend fun register(username: String, password: String): LoginResult<LoggedInUser> {

        val result = dataSource.register(username, password)

        if (result is LoginResult.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    override fun resetPassword(username: String) {
        dataSource.resetPassword(username)
    }

    override fun logout() {
        user = null
        dataSource.logout()
    }
}
