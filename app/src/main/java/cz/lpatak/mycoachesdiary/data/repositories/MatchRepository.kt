package cz.lpatak.mycoachesdiary.data.repositories

import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.data.model.Result

interface MatchRepository {
    suspend fun getMatches(): Result<List<Match>>
    suspend fun getMatchesFilter(
            matchCategory: String,
            all: Boolean,
            dateFrom: Timestamp,
            dateTo: Timestamp
    ): Result<List<Match>>

    fun updateMatch(match: Match)
    fun addMatch(match: Match)
    fun deleteMatch(matchId: String)
}