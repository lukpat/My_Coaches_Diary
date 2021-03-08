package cz.lpatak.mycoachesdiary.ui.myteams.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.lpatak.mycoachesdiary.ui.base.ObservableViewModel

class TeamUIModel : ObservableViewModel() {

    val areInputsReady = MediatorLiveData<Boolean>()

    val name = MutableLiveData("")
    val season = MutableLiveData("")

    init {
        areInputsReady.addSource(name) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(season) { areInputsReady.value = checkInputs() }
    }

    private fun checkInputs(): Boolean {
        return !(
                name.value.isNullOrEmpty() ||
                        season.value.isNullOrEmpty()
                )
    }
}