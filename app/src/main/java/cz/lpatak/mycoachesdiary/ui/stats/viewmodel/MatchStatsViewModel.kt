package cz.lpatak.mycoachesdiary.ui.stats.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.repositories.MatchRepositoryImpl
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchStatsBinding
import kotlinx.coroutines.Dispatchers
import java.math.BigDecimal
import java.math.RoundingMode

class MatchStatsViewModel(private val matchRepository: MatchRepositoryImpl) : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO


    fun loadMatchesFilter(
            matchCategory: String,
            all: Boolean,
            dateFrom: Timestamp,
            dateTo: Timestamp
    ): LiveData<Result<List<Match>>> =
            liveData(coroutineContext) {
                emit(Result.Loading)

                val result = matchRepository.getMatchesFilter(matchCategory, all, dateFrom, dateTo)

                if (result is Result.Success) {
                    result.data
                    emit(result)
                }
            }

    fun setUI(matchList: MutableList<Match>, binding: FragmentMatchStatsBinding) {
        setMatchCategory(matchList, binding)
        setMatchStats(matchList, binding)
        setShotsStats(matchList, binding)
        setCompareStats(matchList, binding)
    }

    private fun setMatchCategory(
            matchList: MutableList<Match>,
            binding: FragmentMatchStatsBinding
    ) {
        var league = 0
        var tournament = 0
        var friendly = 0

        for (match in matchList) {
            when (match.type) {
                "Liga" -> {
                    league++
                }
                "Turnaj" -> {
                    tournament++
                }
                "Přátelák" -> {
                    friendly++
                }
            }
        }

        with(binding) {
            matchCategoryLayout.league.text = league.toString()
            matchCategoryLayout.tournament.text = tournament.toString()
            matchCategoryLayout.friendly.text = friendly.toString()
        }
    }

    private fun setMatchStats(matchList: MutableList<Match>, binding: FragmentMatchStatsBinding) {
        var w = 0
        var d = 0
        var l = 0

        for (match in matchList) {
            when {
                match.scoreTeam == match.scoreOpponent -> {
                    d++
                }
                match.scoreTeam > match.scoreOpponent -> {
                    w++
                }
                match.scoreTeam < match.scoreOpponent -> {
                    l++
                }
            }
        }

        val matchCountText = "Celkový počet zápasů: " + matchList.size.toString()
        val winPercentage =
                BigDecimal((w.toDouble() / matchList.size) * 100).setScale(0, RoundingMode.HALF_EVEN)
                        .toString() + " %"

        with(binding) {
            matchesCount.text = matchCountText
            matchResultsLayout.wins.text = w.toString()
            matchResultsLayout.draws.text = d.toString()
            matchResultsLayout.lost.text = l.toString()
            matchResultsLayout.winsPercentage.text = winPercentage
        }
    }

    private fun setShotsStats(matchList: MutableList<Match>, binding: FragmentMatchStatsBinding) {
        var shotsOn = 0
        var shotsOutside = 0
        var shotsToBlock = 0

        for (match in matchList) {
            shotsOn += match.shotsTeam
            shotsOutside += match.shotsOutside
            shotsToBlock += match.shotsToBlock
        }

        val shots = shotsOn + shotsOutside + shotsToBlock

        with(binding) {
            shotsLayout.shots.text = shots.toString()
            shotsLayout.shotsOnGoal.text = shotsOn.toString()
            shotsLayout.shotsOutside.text = shotsOutside.toString()
            shotsLayout.shotsToBlock.text = shotsToBlock.toString()
        }
    }

    private fun setCompareStats(matchList: MutableList<Match>, binding: FragmentMatchStatsBinding) {
        var scoreTeam = 0
        var scoreOpponent = 0
        var powerPlaysTeam = 0
        var powerPlaysOpponent = 0
        var powerPlaysTeamSuccess = 0
        var powerPlaysOpponentSuccess = 0
        var shotsTeam = 0
        var shotsOpponent = 0

        for (match in matchList) {
            scoreTeam += match.scoreTeam
            scoreOpponent += match.scoreOpponent
            powerPlaysTeam += match.powerPlaysTeam
            powerPlaysOpponent += match.powerPlaysOpponent
            powerPlaysTeamSuccess += match.powerPlaysTeamSuccess
            powerPlaysOpponentSuccess += match.powerPlaysOpponentSuccess
            shotsTeam += match.shotsTeam
            shotsOpponent += match.shotsOpponent
        }

        var powerPlaysTeamSuccessStr = "0"
        if (powerPlaysTeam != 0) {
            powerPlaysTeamSuccessStr =
                    BigDecimal((powerPlaysTeamSuccess.toDouble() / powerPlaysTeam) * 100).setScale(
                            0,
                            RoundingMode.HALF_EVEN
                    ).toString() + "%"
        }

        var powerPlaysOpponentSuccessStr = "0"
        if (powerPlaysOpponent != 0) {
            powerPlaysOpponentSuccessStr =
                    BigDecimal((powerPlaysOpponentSuccess.toDouble() / powerPlaysOpponent) * 100).setScale(
                            0,
                            RoundingMode.HALF_EVEN
                    ).toString() + "%"
        }

        var goalkeepersPercTeam = "0"
        if (shotsOpponent != 0) {
            goalkeepersPercTeam =
                    BigDecimal((scoreOpponent.toDouble() / shotsOpponent) * 100).setScale(
                            0,
                            RoundingMode.HALF_EVEN
                    ).toString() + "%"
        }

        var goalkeepersPercOpponent = "0"
        if (shotsTeam != 0) {
            goalkeepersPercOpponent = BigDecimal((scoreTeam.toDouble() / shotsTeam) * 100).setScale(
                    0,
                    RoundingMode.HALF_EVEN
            ).toString() + "%"
        }


        with(binding) {
            goalsLayout.statTeam.text = scoreTeam.toString()
            goalsLayout.statOpponent.text = scoreOpponent.toString()

            powerPlaysLayout.statTeam.text = powerPlaysTeam.toString()
            powerPlaysLayout.statOpponent.text = powerPlaysOpponent.toString()

            powerPlaysSuccesLayout.statTeam.text = powerPlaysTeamSuccessStr
            powerPlaysSuccesLayout.statOpponent.text = powerPlaysOpponentSuccessStr

            shotsLayout2.statTeam.text = shotsTeam.toString()
            shotsLayout2.statOpponent.text = shotsOpponent.toString()

            goalkeeperPercLayout.statTeam.text = goalkeepersPercTeam
            goalkeeperPercLayout.statOpponent.text = goalkeepersPercOpponent
        }

    }
}
