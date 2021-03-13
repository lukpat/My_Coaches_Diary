package cz.lpatak.mycoachesdiary.ui.stats.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.data.model.DBConstants
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.data.repositories.StatsRepositoryImpl
import cz.lpatak.mycoachesdiary.util.PreferenceManger

class StatsViewModel(private val preferenceManager: PreferenceManger, private val statsRepository: StatsRepositoryImpl) : ViewModel() {
    private val matchList: MutableList<Match?> = mutableListOf()

    private var win = 0
    private var lost = 0
    private var draw = 0
    private var goalsScored = 0
    private var goalsConceded = 0
    private var powerPlaysTeam = 0
    private var powerPlaysOpponent = 0
    private var powerPlaysTeamSuccess = 0
    private var powerPlaysOpponentSuccess = 0
    private var shotsTeam = 0
    private var shotsOpponent = 0
    private var shotsToBlock = 0
    private var shotsOutside = 0

    fun loadMatchStats(
            matchCategory: String,
            all: Boolean,
            dateFrom: Timestamp,
            dateTo: Timestamp
    ) {
        statsRepository.getMatchesFilter(matchCategory, all, dateFrom, dateTo).addOnSuccessListener {
            if (!it.isEmpty) {
                val list = it.documents
                for (doc in list) {
                    val match = doc.toObject(Match::class.java)
                    matchList.add(match)
                }
            }
        }.continueWith {
            getMatchesStats()
        }.continueWith{
            calculateMatchStats()
        }
    }

    private fun getMatchesStats() {
        for (match in matchList) {
            if (match != null) {
                getScoreInfo(match)
                getPowerPlaysInfo(match)
                getShotsInfo(match)
            }
        }
    }

    private fun calculateMatchStats() {
        //TODO: odkazy na výpočty statistik
    }

    private fun getScoreInfo(match: Match) {
        goalsScored += match.scoreTeam
        goalsConceded += match.scoreOpponent
        when {
            match.scoreTeam == match.scoreOpponent -> {
                draw++
            }
            match.scoreTeam > match.scoreOpponent -> {
                win++
            }
            else -> {
                lost++
            }
        }
    }

    private fun getPowerPlaysInfo(match: Match) {
        powerPlaysTeam += match.powerPlaysTeam
        powerPlaysOpponent += match.powerPlaysOpponent
        powerPlaysTeamSuccess += match.powerPlaysTeamSuccess
        powerPlaysOpponentSuccess += match.powerPlaysOpponentSuccess
    }

    private fun getShotsInfo(match: Match) {
        shotsTeam += match.shotsTeam
        shotsOpponent += match.shotsOpponent
        shotsToBlock += match.shotsToBlock
        shotsOutside += match.shotsOutside
    }

    fun isTeamSelected(): Boolean {
        return !preferenceManager.getStringValue(DBConstants.TEAM_ID_KEY).isNullOrEmpty()
    }
}