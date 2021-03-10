package cz.lpatak.mycoachesdiary.data.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObjects
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_DATE
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_END_TIME
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_GOALKEEPERS
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_PLACE
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_PLAYERS
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_RATING
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_START_TIME
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TEAM_ID_KEY
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TeamsCOLLECTION
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TrainingsCOLLECTION
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.data.source.FirestoreSource
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TrainingRepositoryImpl(
    private val firestoreSource: FirestoreSource,
    private val preferenceManager: PreferenceManger
) : TrainingRepository {

    private val trainingsPath: CollectionReference
        get() {
            return firestoreSource.firestore.collection(TeamsCOLLECTION)
                .document(preferenceManager.getStringValue(TEAM_ID_KEY).toString())
                .collection(TrainingsCOLLECTION)
        }

    override suspend fun getTrainings(): Result<List<Training>> =
        suspendCoroutine { cont ->
            trainingsPath
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

    override suspend fun getTrainingsFilter(
        dateFrom: Timestamp,
        dateTo: Timestamp
    ): Result<List<Training>> =
        suspendCoroutine { cont ->
            trainingsPath.whereGreaterThanOrEqualTo(COLUMN_DATE, dateFrom)
                .whereLessThanOrEqualTo(COLUMN_DATE, dateTo)
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

    override fun addTraining(training: Training) {
        val data = hashMapOf(
            COLUMN_DATE to training.date,
            COLUMN_END_TIME to training.endTime,
            COLUMN_START_TIME to training.startTime,
            COLUMN_RATING to training.rating,
            COLUMN_PLACE to training.place,
            COLUMN_PLAYERS to training.players,
            COLUMN_GOALKEEPERS to training.goalkeepers
        )

        trainingsPath
            .add(data)
            .addOnSuccessListener {
                Result.Success(true)
            }.addOnFailureListener {
                Result.Error(it)
            }

    }

    override fun updateTraining(training: Training) {
        val data = hashMapOf(
            COLUMN_DATE to training.date,
            COLUMN_END_TIME to training.endTime,
            COLUMN_START_TIME to training.startTime,
            COLUMN_RATING to training.rating,
            COLUMN_PLACE to training.place,
            COLUMN_PLAYERS to training.players,
            COLUMN_GOALKEEPERS to training.goalkeepers
        )

        trainingsPath.document(training.id!!)
            .set(data)
            .addOnSuccessListener {
                Result.Success(true)
            }.addOnFailureListener {
                Result.Error(it)
            }
    }

    override fun deleteTraining(trainingId: String) {
        trainingsPath.document(trainingId)
            .delete()
            .addOnSuccessListener {
                Result.Success(true)
            }.addOnFailureListener {
                Result.Error(it)
            }
    }
}