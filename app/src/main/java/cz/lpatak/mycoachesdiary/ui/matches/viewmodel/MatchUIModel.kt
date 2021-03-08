package cz.lpatak.mycoachesdiary.ui.matches.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.lpatak.mycoachesdiary.ui.base.ObservableViewModel

class MatchUIModel : ObservableViewModel() {

    val areInputsReady = MediatorLiveData<Boolean>()

    val opponent = MutableLiveData("")
    val dateString = MutableLiveData("")
    val playingTime = MutableLiveData(0)
    val note = MutableLiveData("")

    init {
        areInputsReady.addSource(opponent) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(dateString) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(playingTime) { areInputsReady.value = checkInputs() }
    }


    private fun checkInputs(): Boolean {
        return !(
                opponent.value.isNullOrEmpty() ||
                        dateString.value.isNullOrEmpty() ||
                        checkPlayingTime()
                )
    }

    private fun checkPlayingTime(): Boolean {
        return playingTime.value !in 0..100
    }
}