package cz.lpatak.mycoachesdiary.data.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObjects
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_CATEGORY
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_NAME
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_TIME
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.ExercisesCOLLECTION
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TEAM_ID_KEY
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TRAINING_ID_KEY
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TeamsCOLLECTION
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TrainingsCOLLECTION
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.source.FirestoreSource
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ExerciseInTrainingRepositoryImpl(
        private val firestoreSource: FirestoreSource,
        private val preferenceManager: PreferenceManger
) : ExerciseInTrainingRepository {

    private val exercisesPath: CollectionReference
        get() {
            return firestoreSource.firestore.collection(TeamsCOLLECTION)
                    .document(preferenceManager.getStringValue(TEAM_ID_KEY).toString())
                    .collection(TrainingsCOLLECTION)
                    .document(preferenceManager.getStringValue(TRAINING_ID_KEY).toString())
                    .collection(ExercisesCOLLECTION)
        }

    override suspend fun getExercises(): Result<List<ExerciseInTraining>> =
            suspendCoroutine { cont ->
                exercisesPath
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


    override fun updateExercise(exercise: ExerciseInTraining) {
        val data = hashMapOf(
                COLUMN_NAME to exercise.name,
                COLUMN_CATEGORY to exercise.category,
                COLUMN_TIME to exercise.time,
        )

        exercisesPath.document(exercise.id!!)
                .set(data)
                .addOnSuccessListener {
                    Result.Success(true)
                }.addOnFailureListener {
                    Result.Error(it)
                }
    }

    override fun deleteExercise(exerciseId: String) {
        exercisesPath.document(exerciseId)
                .delete()
                .addOnSuccessListener {
                    Result.Success(true)
                }.addOnFailureListener {
                    Result.Error(it)
                }
    }
}