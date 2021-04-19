package cz.lpatak.mycoachesdiary.data.repositories

import com.google.firebase.auth.FirebaseAuth
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


    override suspend fun getTeams(): Result<List<Team>> =
        suspendCoroutine { cont ->
            val currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid
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
            COLUMN_OWNER to FirebaseAuth.getInstance().currentUser!!.uid,
            COLUMN_SEASON to team.season
        )
        teamsPath.add(data)
    }

    override fun updateTeam(team: Team) {
        val data = hashMapOf(
            COLUMN_NAME to team.name,
            COLUMN_OWNER to team.owner,
            COLUMN_SEASON to team.season
        )
        teamsPath.document(team.id.toString()).set(data)
    }

    override fun deleteTeam(teamId: String) {
        teamsPath.document(teamId).delete()
    }

}
