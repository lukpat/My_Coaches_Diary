package cz.lpatak.mycoachesdiary.ui.trainings.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.ui.base.ObservableViewModel
import java.util.*

class TrainingUIModel : ObservableViewModel() {
    val areInputsReady = MediatorLiveData<Boolean>()
    val areInputsReady2 = MediatorLiveData<Boolean>()

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

        areInputsReady2.addSource(place) { areInputsReady2.value = checkInputs() }
        areInputsReady2.addSource(date) { areInputsReady2.value = checkInputs() }
        areInputsReady2.addSource(startTime) { areInputsReady2.value = checkInputs() }
        areInputsReady2.addSource(endTime) { areInputsReady2.value = checkInputs() }
        areInputsReady2.addSource(players) { areInputsReady2.value = checkInputs() }
        areInputsReady2.addSource(goalkeepers) { areInputsReady2.value = checkInputs() }
        areInputsReady2.addSource(rating) { areInputsReady2.value = checkInputs() }
    }

    private fun checkInputsAdd(): Boolean {
        return !(
                place.value.isNullOrEmpty() ||
                        date.value.isNullOrEmpty() ||
                        startTime.value.isNullOrEmpty() ||
                        endTime.value.isNullOrEmpty()
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