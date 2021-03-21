package cz.lpatak.mycoachesdiary.ui.trainings.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.ui.base.ObservableViewModel
import java.util.*

class TrainingUIModel : ObservableViewModel() {
    val areInputsReady = MediatorLiveData<Boolean>()

    val place = MutableLiveData("")
    val rating = MutableLiveData("")
    val date = MutableLiveData("")
    val startTime = MutableLiveData("")
    val endTime = MutableLiveData("")
    val players = MutableLiveData("")
    val goalkeepers = MutableLiveData("")
    var timestamp = Timestamp(Date(0))

    init {
        areInputsReady.addSource(place) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(date) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(startTime) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(endTime) { areInputsReady.value = checkInputs() }
    }

    fun checkInputs(): Boolean {
        return !(
                place.value.isNullOrEmpty() ||
                        rating.value.isNullOrEmpty() || checkRating() ||
                        date.value.isNullOrEmpty() ||
                        startTime.value.isNullOrEmpty() ||
                        endTime.value.isNullOrEmpty() ||
                        players.value.isNullOrEmpty() || checkPlayers() ||
                        goalkeepers.value.isNullOrEmpty() || checkGoalkeepers()
                )
    }

    private fun checkRating(): Boolean {
        return rating.value!!.toInt() !in 0..10
    }

    private fun checkPlayers(): Boolean {
        return rating.value!!.toInt() !in 0..50
    }

    private fun checkGoalkeepers(): Boolean {
        return rating.value!!.toInt() !in 0..20
    }
}