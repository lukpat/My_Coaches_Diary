package cz.lpatak.mycoachesdiary.ui.matches.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TEAM_ID_KEY
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.repositories.MatchRepositoryImpl
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import kotlinx.coroutines.Dispatchers

class MatchesViewModel(
    private val matchRepository: MatchRepositoryImpl,
    private val preferenceManager: PreferenceManger
) : ViewModel() {

    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun loadMatches(): LiveData<Result<List<Match>>> = liveData(coroutineContext) {
        emit(Result.Loading)

        val result = matchRepository.getMatches()

        if (result is Result.Success) {
            emit(result)
        }
    }

    fun loadMatchesFilter(matchCategory: String): LiveData<Result<List<Match>>> =
        liveData(coroutineContext) {
            emit(Result.Loading)

            val result = matchRepository.getMatchesFilter(matchCategory)

            if (result is Result.Success) {
                emit(result)
            }
        }

    fun addMatch(opponent: String, date: String, type: String, playingTime: Int) {
        matchRepository.addMatch(
            Match(
                "",
                preferenceManager.getStringValue(TEAM_ID_KEY),
                opponent,
                date,
                type,
                playingTime,
                "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            )
        )
    }

    fun updateMatch(match: Match) {
        Log.println(Log.ERROR, "vm", match.id.toString())
        matchRepository.updateMatch(match)
    }

    fun deleteMatch(matchId: String) {
        matchRepository.deleteMatch(matchId)
    }

    fun isTeamSelected(): Boolean {
        return !preferenceManager.getStringValue(TEAM_ID_KEY).isNullOrEmpty()
    }


}