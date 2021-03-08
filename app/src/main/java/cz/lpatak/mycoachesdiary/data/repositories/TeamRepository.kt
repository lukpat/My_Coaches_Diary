package cz.lpatak.mycoachesdiary.data.repositories

import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.model.Team

interface TeamRepository {
    suspend fun getTeams(): Result<List<Team>>
    fun updateTeam(team: Team)
    fun addTeam(team: Team)
    fun deleteTeam(teamId: String)
}