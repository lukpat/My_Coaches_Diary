package cz.lpatak.mycoachesdiary.data.repositories

import cz.lpatak.mycoachesdiary.data.model.auth.LoggedInUser
import cz.lpatak.mycoachesdiary.data.source.LoginResult

interface AuthRepository {
    suspend fun login(username: String, password: String): LoginResult<LoggedInUser>
    suspend fun register(username: String, password: String): LoginResult<LoggedInUser>
    suspend fun logout()
    fun getUserEmail(): String
}