package cz.lpatak.mycoachesdiary.data.source

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import cz.lpatak.mycoachesdiary.data.model.auth.LoggedInUser
import cz.lpatak.mycoachesdiary.util.NoConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException


class AuthDataSource {

    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    suspend fun login(username: String, password: String): LoginResult<LoggedInUser> =
        withContext(Dispatchers.IO) {
            var result: LoginResult<LoggedInUser> =
                LoginResult.Error(IOException("Error logging in"))

            try {
                val task = auth.signInWithEmailAndPassword(username, password).await()
                task?.let {
                    if (it.user != null) {
                        Log.d("TAG", "signInWithEmail:success")
                        val user = auth.currentUser
                        user?.let {
                            result =
                                LoginResult.Success(
                                    LoggedInUser(
                                        user.email,
                                        user.uid
                                    )
                                )
                        }
                    }
                }
            } catch (e: FirebaseException) {
                Log.w("TAG", "signInWithEmail:failure", e)
                result = if (e is FirebaseNetworkException) {
                    LoginResult.Error(NoConnectivityException())
                } else {
                    LoginResult.Error(IOException("Error logging in", e))
                }
            }
            return@withContext result
        }


    suspend fun register(username: String, password: String): LoginResult<LoggedInUser> =
        withContext(Dispatchers.IO) {
            var result: LoginResult<LoggedInUser> =
                LoginResult.Error(IOException("Error logging in"))

            try {
                val task = auth.createUserWithEmailAndPassword(username, password).await()
                task?.let {
                    if (it.user != null) {
                        Log.d("TAG", "register:success")
                        val user = auth.currentUser
                        user?.let {
                            result =
                                LoginResult.Success(
                                    LoggedInUser(
                                        user.email,
                                        user.uid
                                    )
                                )
                        }
                    }
                }
            } catch (e: FirebaseException) {
                Log.w("TAG", "register:failure", e)
                result = if (e is FirebaseNetworkException) {
                    LoginResult.Error(NoConnectivityException())
                } else {
                    LoginResult.Error(IOException("Error registration in", e))
                }
            }
            return@withContext result
        }

    /**
     * Logout the user session
     */
    fun logout() {
        auth.signOut()
    }
}

