package cz.lpatak.mycoachesdiary.ui.matches.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.lpatak.mycoachesdiary.ui.base.ObservableViewModel

class StatsUIModel : ObservableViewModel() {

    val areInputsReady = MediatorLiveData<Boolean>()

    val scoreTeam = MutableLiveData("")
    val scoreOpponent = MutableLiveData("")
    val powerPlaysTeam = MutableLiveData("")
    val powerPlaysOpponent = MutableLiveData("")
    val powerPlaysTeamSuccess = MutableLiveData("")
    val powerPlaysOpponentSuccess = MutableLiveData("")
    val shotsTeam = MutableLiveData("")
    val shotsOpponent = MutableLiveData("")
    val shotsToBlock = MutableLiveData("")
    val shotsOutside = MutableLiveData("")

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

    fun checkInputs(): Boolean {
        return !(
                scoreTeam.value.isNullOrEmpty() || scoreOpponent.value.isNullOrEmpty() ||
                        powerPlaysTeam.value.isNullOrEmpty() || powerPlaysOpponent.value.isNullOrEmpty() ||
                        powerPlaysTeamSuccess.value.isNullOrEmpty() || powerPlaysOpponentSuccess.value.isNullOrEmpty() ||
                        shotsTeam.value.isNullOrEmpty() || shotsOpponent.value.isNullOrEmpty() ||
                        shotsToBlock.value.isNullOrEmpty() || shotsOutside.value.isNullOrEmpty() ||
                        !checkTeamPowerPlaysSuccess() ||
                        !checkOpponentPowerPlaysSuccess() ||
                        !checkScore() ||
                        !checkShots() ||
                        !checkShotsToBlock() ||
                        !checkShotsOutside() ||
                        !checkPowerPlays()
                )
    }

    private fun checkTeamPowerPlaysSuccess(): Boolean {
        return powerPlaysTeam.value!!.toInt() >= powerPlaysTeamSuccess.value!!.toInt()
    }

    private fun checkOpponentPowerPlaysSuccess(): Boolean {
        return powerPlaysOpponent.value!!.toInt() >= powerPlaysOpponentSuccess.value!!.toInt()
    }

    private fun checkScore(): Boolean {
        return scoreOpponent.value!!.toInt() in 0..50 && scoreTeam.value!!.toInt() in 0..50
    }

    private fun checkShots(): Boolean {
        return shotsTeam.value!!.toInt() in 0..200 && shotsOpponent.value!!.toInt() in 0..200
    }

    private fun checkShotsToBlock(): Boolean {
        return shotsToBlock.value!!.toInt() in 0..200
    }

    private fun checkShotsOutside(): Boolean {
        return shotsOutside.value!!.toInt() in 0..200
    }

    private fun checkPowerPlays(): Boolean {
        return powerPlaysTeam.value!!.toInt() in 0..20 && powerPlaysOpponent.value!!.toInt() in 0..20
    }

}