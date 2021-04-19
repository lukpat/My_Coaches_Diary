package cz.lpatak.mycoachesdiary.data.repositories

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObjects
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_CATEGORY
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_NAME
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_NAME_UPPERCASE
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_OWNER
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.ExercisesCOLLECTION
import cz.lpatak.mycoachesdiary.data.model.Exercise
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.source.FirestoreSource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ExerciseRepositoryImpl(private val firestoreSource: FirestoreSource) : ExerciseRepository {

    private val exercisesPath = firestoreSource.firestore.collection(ExercisesCOLLECTION)
    private val exercisesImgPath = firestoreSource.storage.reference.child(ExercisesCOLLECTION)

    override suspend fun getExercises(): Result<List<Exercise>> =
        suspendCoroutine { cont ->
            exercisesPath.orderBy(COLUMN_NAME)
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

    override suspend fun getExercisesFilter(
        exerciseOwner: Boolean,
        category: String
    ): Result<List<Exercise>> =
        suspendCoroutine { cont ->
            val user = FirebaseAuth.getInstance().currentUser!!.uid
            if (exerciseOwner) {
                exercisesPath.whereEqualTo(COLUMN_OWNER, user)
                    .whereEqualTo(COLUMN_CATEGORY, category).orderBy(COLUMN_NAME)
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
            } else {
                exercisesPath.whereEqualTo(COLUMN_CATEGORY, category).orderBy(COLUMN_NAME)
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
        }

    override suspend fun searchData(query: String): Result<List<Exercise>> =
        suspendCoroutine { cont ->
            exercisesPath.whereGreaterThanOrEqualTo(COLUMN_NAME_UPPERCASE, query)
                .whereLessThanOrEqualTo(COLUMN_NAME_UPPERCASE, query + '\uf8ff')
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

    override fun deleteExercise(exerciseId: String) {
        exercisesPath.document(exerciseId).delete()
    }

    override fun isExerciseOwner(owner: String): Boolean {
        return owner == FirebaseAuth.getInstance().currentUser!!.uid
    }


    override suspend fun addExercise(exercise: Exercise): Result<Boolean> =
        suspendCoroutine { cont ->
            exercisesPath
                .add(
                    Exercise(
                        exercise.id,
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        exercise.name,
                        exercise.nameUpperCase,
                        exercise.category,
                        exercise.description,
                        exercise.imageUrl
                    )
                )
                .addOnSuccessListener {
                    cont.resume(Result.Success(true))
                }.addOnFailureListener {
                    cont.resume(Result.Error(it))
                }
        }

    override fun addExerciseWithoutIMG(exercise: Exercise) {
        exercisesPath
            .add(
                Exercise(
                    exercise.id,
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    exercise.name,
                    exercise.nameUpperCase,
                    exercise.category,
                    exercise.description,
                    exercise.imageUrl
                )
            )
    }

    override fun uploadImageWithUri(
        uri: Uri,
        block: ((Result<Uri>, Int) -> Unit)?
    ) {
        val photoRef =
            exercisesImgPath.child(uri.lastPathSegment!!)

        photoRef.putFile(uri).continueWithTask { task ->
            // Forward any exceptions
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            // Request the public download URL
            photoRef.downloadUrl
        }
            .addOnSuccessListener { block?.invoke(Result.Success(it), 100) }
            .addOnFailureListener { block?.invoke(Result.Error(it), 0) }
    }

}