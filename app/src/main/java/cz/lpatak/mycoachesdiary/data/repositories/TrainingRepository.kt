package cz.lpatak.mycoachesdiary.data.repositories

import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.model.Training

interface TrainingRepository {
    suspend fun getTrainings(): Result<List<Training>>
    suspend fun getTrainingsFilter(dateFrom: Timestamp, dateTo: Timestamp): Result<List<Training>>
    fun updateTraining(training: Training)
    fun deleteTraining(trainingId: String)
    fun addTraining(training: Training)
}