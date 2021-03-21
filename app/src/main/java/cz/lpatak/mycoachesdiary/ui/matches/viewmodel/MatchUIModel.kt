package cz.lpatak.mycoachesdiary.ui.matches.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.ui.base.ObservableViewModel
import java.util.*

class MatchUIModel : ObservableViewModel() {

    val areInputsReady = MediatorLiveData<Boolean>()

    val opponent = MutableLiveData("")
    val dateString = MutableLiveData("")
    val playingTime = MutableLiveData("")
    val note = MutableLiveData("")
    var timestamp = Timestamp(Date(0))

    init {
        areInputsReady.addSource(opponent) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(dateString) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(playingTime) { areInputsReady.value = checkInputs() }
    }


    fun checkInputs(): Boolean {
        return !(
                opponent.value.isNullOrEmpty() ||
                        dateString.value.isNullOrEmpty() ||
                        playingTime.value.isNullOrBlank() ||
                        checkPlayingTime()
                )
    }

    private fun checkPlayingTime(): Boolean {
        return playingTime.value!!.toInt() !in 0..100
    }
}