package cz.lpatak.mycoachesdiary.data.repositories

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import cz.lpatak.mycoachesdiary.data.model.Exercise
import cz.lpatak.mycoachesdiary.data.model.Result

interface ExerciseRepository {
    suspend fun getExercises(): Result<List<Exercise>>
    suspend fun getExercisesFilter(exerciseOwner: Boolean, category: String): Result<List<Exercise>>
    fun getExercise(exerciseId: String): Task<DocumentSnapshot>
    suspend fun searchData(query: String): Result<List<Exercise>>
    suspend fun addExercise(exercise: Exercise): Result<Boolean>
    fun addExerciseWithoutIMG(exercise: Exercise)
    fun uploadImageWithUri(uri: Uri, block: ((Result<Uri>, Int) -> Unit)? = null)
}