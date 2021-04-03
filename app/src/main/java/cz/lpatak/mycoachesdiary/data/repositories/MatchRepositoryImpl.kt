package cz.lpatak.mycoachesdiary.data.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObjects
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_DATE
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_NOTE
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_OPPONENT
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_PLAYING_TIME
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_POWER_PLAYS_OPPONENT
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_POWER_PLAYS_OPPONENT_SUCESS
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_POWER_PLAYS_TEAM
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_POWER_PLAYS_TEAM_SUCESS
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_SCORE_OPPONENT
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_SCORE_TEAM
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_SHOTS_OPPONENT
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_SHOTS_OUTSIDE
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_SHOTS_TEAM
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_SHOTS_TO_BLOCK
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_TEAM
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_TYPE
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.MatchesCOLLECTION
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TEAM_ID_KEY
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.source.FirestoreSource
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MatchRepositoryImpl(
    private val firestoreSource: FirestoreSource,
    private val preferenceManager: PreferenceManger
) : MatchRepository {

    private val matchesPath: CollectionReference
        get() {
            return firestoreSource.firestore.collection(MatchesCOLLECTION)
        }

    override suspend fun getMatches(): Result<List<Match>> =
        suspendCoroutine { cont ->
            matchesPath.whereEqualTo(COLUMN_TEAM, preferenceManager.getStringValue(TEAM_ID_KEY))
                .orderBy(COLUMN_DATE)
                .get()
                .addOnSuccessListener {
                    try {
                        cont.resume(Result.Success(it.toObjects()))
                    } catch (e: Exception) {
                        cont.resume(Result.Error(e))
                    }
                }.addOnFailureListener {
                    cont.resume(Result.Error(it))
                }
        }

    override suspend fun getMatchesFilter(
        matchCategory: String,
        all: Boolean,
        dateFrom: Timestamp,
        dateTo: Timestamp
    ): Result<List<Match>> =
        suspendCoroutine { cont ->
            var query = matchesPath
                .whereEqualTo(COLUMN_TEAM, preferenceManager.getStringValue(TEAM_ID_KEY))
                .whereGreaterThanOrEqualTo(COLUMN_DATE, dateFrom)
                .whereLessThanOrEqualTo(COLUMN_DATE, dateTo)
                .orderBy(COLUMN_DATE)
            if (!all) {
                query = matchesPath
                    .whereEqualTo(COLUMN_TEAM, preferenceManager.getStringValue(TEAM_ID_KEY))
                    .whereEqualTo(COLUMN_TYPE, matchCategory)
                    .whereGreaterThanOrEqualTo(COLUMN_DATE, dateFrom)
                    .whereLessThanOrEqualTo(COLUMN_DATE, dateTo)
                    .orderBy(COLUMN_DATE)
            }

            query.get()
                .addOnSuccessListener {
                    try {
                        cont.resume(Result.Success(it.toObjects()))
                    } catch (e: Exception) {
                        cont.resume(Result.Error(e))
                    }
                }.addOnFailureListener {
                    cont.resume(Result.Error(it))
                }
        }

    override fun addMatch(match: Match) {

        val data = hashMapOf(
            COLUMN_TEAM to preferenceManager.getStringValue(TEAM_ID_KEY),
            COLUMN_OPPONENT to match.opponent,
            COLUMN_DATE to match.date,
            COLUMN_TYPE to match.type,
            COLUMN_PLAYING_TIME to match.playingTime,
            COLUMN_NOTE to match.note
        )

        matchesPath
            .add(data)
            .addOnSuccessListener {
                Result.Success(true)
            }.addOnFailureListener {
                Result.Error(it)
            }

    }

    override fun updateMatch(match: Match) {
        val data = hashMapOf(
            COLUMN_TEAM to match.team,
            COLUMN_OPPONENT to match.opponent,
            COLUMN_DATE to match.date,
            COLUMN_TYPE to match.type,
            COLUMN_PLAYING_TIME to match.playingTime,
            COLUMN_NOTE to match.note,
            COLUMN_SCORE_TEAM to match.scoreTeam,
            COLUMN_SCORE_OPPONENT to match.scoreOpponent,
            COLUMN_POWER_PLAYS_TEAM to match.powerPlaysTeam,
            COLUMN_POWER_PLAYS_OPPONENT to match.powerPlaysOpponent,
            COLUMN_POWER_PLAYS_TEAM_SUCESS to match.powerPlaysTeamSuccess,
            COLUMN_POWER_PLAYS_OPPONENT_SUCESS to match.powerPlaysOpponentSuccess,
            COLUMN_SHOTS_TEAM to match.shotsTeam,
            COLUMN_SHOTS_OPPONENT to match.shotsOpponent,
            COLUMN_SHOTS_TO_BLOCK to match.shotsToBlock,
            COLUMN_SHOTS_OUTSIDE to match.shotsOutside
        )

        matchesPath.document(match.id!!)
            .set(data)
            .addOnSuccessListener {
                Result.Success(true)
            }.addOnFailureListener {
                Result.Error(it)
            }
    }

    override fun deleteMatch(matchId: String) {
        matchesPath.document(matchId)
            .delete()
            .addOnSuccessListener {
                Result.Success(true)
            }.addOnFailureListener {
                Result.Error(it)
            }
    }

}
