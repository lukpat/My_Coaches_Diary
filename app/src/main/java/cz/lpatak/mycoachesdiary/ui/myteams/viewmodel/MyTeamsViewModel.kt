package cz.lpatak.mycoachesdiary.ui.myteams.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TEAM_ID_KEY
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TEAM_NAME_KEY
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TEAM_SEASON_KEY
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.model.Team
import cz.lpatak.mycoachesdiary.data.repositories.TeamRepositoryImpl
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import kotlinx.coroutines.Dispatchers

class MyTeamsViewModel(
        private val teamsRepository: TeamRepositoryImpl,
        private val preferenceManager: PreferenceManger
) : ViewModel() {

    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun loadTeams(): LiveData<Result<List<Team>>> = liveData(coroutineContext) {
        emit(Result.Loading)

        val result = teamsRepository.getTeams()

        if (result is Result.Success) {
            emit(result)
        }
    }

    fun addTeam(team: Team) {
        teamsRepository.addTeam(team)
    }

    fun updateTeam(team: Team) {
        teamsRepository.updateTeam(team)
    }

    fun deleteTeam(teamId: String) {
        teamsRepository.deleteTeam(teamId)
        if (isTeamCurrentTeam(teamId)) {
            preferenceManager.clearAllPreferences()
        }
    }

    fun setCurrentTeam(team: Team) {
        preferenceManager.run {
            putStringValue(TEAM_ID_KEY, team.id.toString())
            putStringValue(TEAM_NAME_KEY, team.name.toString())
            putStringValue(TEAM_SEASON_KEY, team.season.toString())
        }
    }

    fun getPreferenceManager(): PreferenceManger {
        return preferenceManager
    }

    fun isTeamSelected(): Boolean {
        return !preferenceManager.getStringValue(TEAM_ID_KEY).isNullOrEmpty()
    }

    fun isTeamCurrentTeam(teamId: String): Boolean {
        Log.println(Log.ERROR, "teamId", teamId)
        Log.println(Log.ERROR, "prefMan", preferenceManager.getStringValue(TEAM_ID_KEY).toString())
        return (teamId == preferenceManager.getStringValue(TEAM_ID_KEY))
    }
}