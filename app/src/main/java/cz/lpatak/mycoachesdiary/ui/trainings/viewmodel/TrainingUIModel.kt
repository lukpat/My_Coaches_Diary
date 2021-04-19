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
        areInputsReady.addSource(place) { areInputsReady.value = checkInputsAdd() }
        areInputsReady.addSource(date) { areInputsReady.value = checkInputsAdd() }
        areInputsReady.addSource(startTime) { areInputsReady.value = checkInputsAdd() }
        areInputsReady.addSource(endTime) { areInputsReady.value = checkInputsAdd() }
    }

    private fun checkInputsAdd(): Boolean {
        return !(
                place.value.isNullOrEmpty() ||
                        date.value.isNullOrEmpty() ||
                        startTime.value.isNullOrEmpty() ||
                        endTime.value.isNullOrEmpty() ||
                        !checkTime()
                )
    }


    fun checkInputs(): Boolean {
        return !(
                place.value.isNullOrEmpty() ||
                        rating.value.isNullOrEmpty() || checkRating() ||
                        date.value.isNullOrEmpty() ||
                        startTime.value.isNullOrEmpty() ||
                        endTime.value.isNullOrEmpty() ||
                        players.value.isNullOrEmpty() || checkPlayers() ||
                        goalkeepers.value.isNullOrEmpty() || checkGoalkeepers() ||
                        !checkTime()
                )
    }

    private fun checkTime(): Boolean {
        if (!startTime.value.isNullOrEmpty() && !endTime.value.isNullOrEmpty()) {
            val hoursFrom = startTime.value!!.replace(":", "").toInt()
            val hoursTo = endTime.value!!.replace(":", "").toInt()
            if (hoursFrom < hoursTo) {
                return true
            }
        }
        return false
    }

    private fun checkRating(): Boolean {
        return rating.value!!.toInt() !in 0..10
    }

    private fun checkPlayers(): Boolean {
        return players.value!!.toInt() !in 0..50
    }

    private fun checkGoalkeepers(): Boolean {
        return goalkeepers.value!!.toInt() !in 0..20
    }
}