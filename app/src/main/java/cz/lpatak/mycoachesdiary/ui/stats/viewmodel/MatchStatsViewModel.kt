package cz.lpatak.mycoachesdiary.ui.stats.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.data.model.DBConstants
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.data.repositories.StatsRepositoryImpl
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import java.math.BigDecimal
import java.math.RoundingMode

class MatchStatsViewModel(private val preferenceManager: PreferenceManger, private val statsRepository: StatsRepositoryImpl) : ViewModel() {
    private val matchList: MutableList<Match?> = mutableListOf()

    var matches = 0
    private var playingTime = 0
    var win = 0
    var lost = 0
    var draw = 0
    var goalsScored = 0
    var goalsConceded = 0
    var winPercentage = 0.0

    var powerPlaysTeam = 0
    var powerPlaysOpponent = 0
    private var powerPlaysTeamSuccess = 0
    private var powerPlaysOpponentSuccess = 0
    var powerPlaysSuccessRate = 0.0
    var penaltyKillPercentage = 0.0

    var shotsTeam = 0
    var shotsOpponent = 0
    var shotsToBlock = 0
    var shotsToBlockPercentage = 0.0
    var shotsOutside = 0
    var shotsOutsidePercentage = 0.0
    var shotsBalance = 0
    var shotsPerGame = 0.0
    var shotsPerMinute = 0.0
    var goalkeeperSavesPercentage = 0.0
    var goalShotPercentage = 0.0
    var shotsOnGoalPercentage = 0.0


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
            matches = matchList.size
        }.continueWith {
            if (matches != 0) getMatchesStats()
        }.continueWith {
            if (matches != 0) calculateMatchStats()
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
        calculateWinPercentage()
        calculatePowerPlaysStats()
        calculateShotsStats()
    }

    private fun getScoreInfo(match: Match) {
        goalsScored += match.scoreTeam
        goalsConceded += match.scoreOpponent
        playingTime += match.playingTime
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

    private fun calculateWinPercentage() {
        winPercentage = ((win.toDouble() / matches) * 100)
        winPercentage = BigDecimal(winPercentage).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }

    private fun calculatePowerPlaysStats() {
        powerPlaysSuccessRate = (powerPlaysTeamSuccess.toDouble() / powerPlaysTeam) * 100
        powerPlaysSuccessRate = BigDecimal(powerPlaysSuccessRate).setScale(2, RoundingMode.HALF_EVEN).toDouble()

        penaltyKillPercentage = (powerPlaysOpponentSuccess.toDouble() / powerPlaysOpponent) * 100
        penaltyKillPercentage = BigDecimal(penaltyKillPercentage).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }

    private fun calculateShotsStats() {
        val shotsTeamDouble = shotsTeam.toDouble()
        val totalShots = shotsToBlock + shotsOutside + shotsTeam

        shotsPerGame = (shotsTeamDouble / matches)
        shotsPerGame = BigDecimal(shotsPerGame).setScale(2, RoundingMode.HALF_EVEN).toDouble()

        shotsPerMinute = (shotsTeamDouble / playingTime)
        shotsPerMinute = BigDecimal(shotsPerMinute).setScale(2, RoundingMode.HALF_EVEN).toDouble()

        shotsBalance = shotsTeam - shotsOpponent

        goalkeeperSavesPercentage = 100 - ((goalsConceded.toDouble() / shotsOpponent) * 100)
        goalkeeperSavesPercentage = BigDecimal(goalkeeperSavesPercentage).setScale(2, RoundingMode.HALF_EVEN).toDouble()

        goalShotPercentage = (goalsScored / shotsTeamDouble) * 100
        goalShotPercentage = BigDecimal(goalShotPercentage).setScale(2, RoundingMode.HALF_EVEN).toDouble()

        shotsOnGoalPercentage = (shotsTeamDouble / totalShots) * 100
        shotsOnGoalPercentage = BigDecimal(shotsOnGoalPercentage).setScale(2, RoundingMode.HALF_EVEN).toDouble()


        shotsOutsidePercentage = (shotsOutside.toDouble() / totalShots) * 100
        shotsOutsidePercentage = BigDecimal(shotsOutsidePercentage).setScale(2, RoundingMode.HALF_EVEN).toDouble()


        shotsToBlockPercentage = (shotsToBlock.toDouble() / totalShots) * 100
        shotsToBlockPercentage = BigDecimal(shotsToBlockPercentage).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }

    fun isTeamSelected(): Boolean {
        return !preferenceManager.getStringValue(DBConstants.TEAM_ID_KEY).isNullOrEmpty()
    }
}