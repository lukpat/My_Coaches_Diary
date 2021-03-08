package cz.lpatak.mycoachesdiary.data.repositories

import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.data.model.Result

interface ExerciseInTrainingRepository {
    suspend fun getExercises(): Result<List<ExerciseInTraining>>
    fun updateExercise(exercise: ExerciseInTraining)
    fun deleteExercise(exerciseId: String)
}