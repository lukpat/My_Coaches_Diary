package cz.lpatak.mycoachesdiary.data.repositories

import com.google.firebase.firestore.ktx.toObjects
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_NAME
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_OWNER
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_SEASON
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TeamsCOLLECTION
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.model.Team
import cz.lpatak.mycoachesdiary.data.source.FirestoreSource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TeamRepositoryImpl(
    firestoreSource: FirestoreSource
) : TeamRepository {

    private val teamsPath = firestoreSource.firestore.collection(TeamsCOLLECTION)
    private val currentUserUID = firestoreSource.auth.currentUser?.uid

    override suspend fun getTeams(): Result<List<Team>> =
        suspendCoroutine { cont ->
            teamsPath.whereEqualTo(COLUMN_OWNER, currentUserUID)
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

    override fun addTeam(team: Team) {
        val data = hashMapOf(
            COLUMN_NAME to team.name,
            COLUMN_OWNER to currentUserUID,
            COLUMN_SEASON to team.season
        )

        teamsPath
            .add(data)
            .addOnSuccessListener {
                Result.Success(true)
            }.addOnFailureListener {
                Result.Error(it)
            }

    }

    override fun updateTeam(team: Team) {
        val data = hashMapOf(
            COLUMN_NAME to team.name,
            COLUMN_OWNER to team.owner,
            COLUMN_SEASON to team.season
        )

        teamsPath.document(team.id.toString())
            .set(data)
            .addOnSuccessListener {
                Result.Success(true)
            }.addOnFailureListener {
                Result.Error(it)
            }
    }

    override fun deleteTeam(teamId: String) {
        teamsPath.document(teamId)
            .delete()
            .addOnSuccessListener {
                Result.Success(true)
            }.addOnFailureListener {
                Result.Error(it)
            }
    }

}
