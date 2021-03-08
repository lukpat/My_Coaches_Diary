package cz.lpatak.mycoachesdiary.data.repositories

import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.model.Training

interface TrainingRepository {
    suspend fun getTrainings(): Result<List<Training>>
    fun updateTraining(training: Training)
    fun deleteTraining(trainingId: String)
    fun addTraining(training: Training)
}