package cz.lpatak.mycoachesdiary.ui.trainings.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.lpatak.mycoachesdiary.ui.base.ObservableViewModel

class ExerciseInTrainingUIModel : ObservableViewModel() {
    val name = MutableLiveData("")
    val time = MutableLiveData("0")

    fun checkTime(): Boolean {
        if (time.value.isNullOrEmpty()) {
            time.value = "0"
        }

        return (time.value!!.toInt() in 1..100)
    }

}