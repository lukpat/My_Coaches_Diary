package cz.lpatak.mycoachesdiary.ui.exercises.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cz.lpatak.mycoachesdiary.data.model.Exercise
import cz.lpatak.mycoachesdiary.data.repositories.ExerciseRepositoryImpl
import cz.lpatak.mycoachesdiary.data.source.FirestoreSource

class NewExerciseWorker(
        context: Context,
        workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repository = ExerciseRepositoryImpl(FirestoreSource())

    override suspend fun doWork(): Result {
        val name = checkNotNull(inputData.getString(NAME))
        val category = checkNotNull(inputData.getString(CATEGORY))
        val description = checkNotNull((inputData.getString(DESCRIPTION)))
        val imageUri = checkNotNull(inputData.getString(ImageUploaderWorker.KEY_UPLOADED_URI))

        val exercise: Exercise
/*
        if (exerciseId.isNullOrEmpty()) {
            exercise = Exercise("", "", name, category, description, imageUri)
        } else {
            exercise = Exercise(exerciseId, "", name, category, description, imageUri)
        }
*/

        exercise = Exercise("", "", name, category, description, imageUri)

        when (repository.addExercise(exercise)) {
            is cz.lpatak.mycoachesdiary.data.model.Result.Success -> {
                return Result.success()
            }
            is cz.lpatak.mycoachesdiary.data.model.Result.Error -> {
                return Result.failure()
            }
        }

        return Result.failure()
    }


    companion object {
        const val NAME = "key-name-new-exercise"
        const val CATEGORY = "key-category-new-exercise"
        const val DESCRIPTION = "key-description-new-exercise"
    }
}