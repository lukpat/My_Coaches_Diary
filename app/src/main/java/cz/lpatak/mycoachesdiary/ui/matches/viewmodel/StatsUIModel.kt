package cz.lpatak.mycoachesdiary.ui.matches.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.lpatak.mycoachesdiary.ui.base.ObservableViewModel

class StatsUIModel : ObservableViewModel() {

    val areInputsReady = MediatorLiveData<Boolean>()

    val scoreTeam = MutableLiveData(0)
    val scoreOpponent = MutableLiveData(0)
    val powerPlaysTeam = MutableLiveData(0)
    val powerPlaysOpponent = MutableLiveData(0)
    val powerPlaysTeamSuccess = MutableLiveData(0)
    val powerPlaysOpponentSuccess = MutableLiveData(0)
    val shotsTeam = MutableLiveData(0)
    val shotsOpponent = MutableLiveData(0)
    val shotsToBlock = MutableLiveData(0)
    val shotsOutside = MutableLiveData(0)

    init {
        areInputsReady.addSource(scoreTeam) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(scoreOpponent) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(powerPlaysTeam) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(powerPlaysOpponent) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(powerPlaysTeamSuccess) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(powerPlaysOpponentSuccess) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(shotsTeam) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(shotsOpponent) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(shotsToBlock) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(shotsOutside) { areInputsReady.value = checkInputs() }
    }

    private fun checkInputs(): Boolean {
        return !(
                !checkTeamPowerPlaysSuccess() ||
                        !checkOpponentPowerPlaysSuccess() ||
                        !checkScore() ||
                        !checkShots() ||
                        !checkShotsToBlock() ||
                        !checkShotsOutside()
                )
    }

    private fun checkTeamPowerPlaysSuccess(): Boolean {
        return powerPlaysTeam.value!! >= powerPlaysTeamSuccess.value!!
    }

    private fun checkOpponentPowerPlaysSuccess(): Boolean {
        return powerPlaysOpponent.value!! >= powerPlaysOpponentSuccess.value!!
    }

    private fun checkScore(): Boolean {
        return scoreOpponent.value in 0..50 && scoreTeam.value in 0..50
    }

    private fun checkShots(): Boolean {
        return shotsTeam.value in 0..200 && shotsOpponent.value in 0..200
    }

    private fun checkShotsToBlock(): Boolean {
        return shotsToBlock.value in 0..200
    }

    private fun checkShotsOutside(): Boolean {
        return shotsOutside.value in 0..200
    }
}