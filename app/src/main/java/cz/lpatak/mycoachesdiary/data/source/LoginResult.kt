package cz.lpatak.mycoachesdiary.data.source

sealed class LoginResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : LoginResult<T>()
    data class Error(val exception: Exception) : LoginResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}