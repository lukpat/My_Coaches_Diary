package cz.lpatak.mycoachesdiary.ui.trainings.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TEAM_ID_KEY
import cz.lpatak.mycoachesdiary.data.model.Exercise
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.data.repositories.ExerciseInTrainingRepositoryImpl
import cz.lpatak.mycoachesdiary.data.repositories.ExerciseRepositoryImpl
import cz.lpatak.mycoachesdiary.data.repositories.TrainingRepositoryImpl
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import cz.lpatak.mycoachesdiary.util.convertLongToDate
import kotlinx.coroutines.Dispatchers


class TrainingsViewModel(
    private val trainingsRepository: TrainingRepositoryImpl,
    private val exerciseInTrainingRepository: ExerciseInTrainingRepositoryImpl,
    private val exerciseRepository: ExerciseRepositoryImpl,
    private val preferenceManager: PreferenceManger
) : ViewModel() {

    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun loadTrainings(): LiveData<Result<List<Training>>> = liveData(coroutineContext) {
        emit(Result.Loading)

        val result = trainingsRepository.getTrainings()

        if (result is Result.Success) {
            emit(result)
        }
    }

    fun loadTrainingsWithFilter(
        dateFrom: Timestamp,
        dateTo: Timestamp
    ): LiveData<Result<List<Training>>> = liveData(coroutineContext) {
        emit(Result.Loading)

        val result = trainingsRepository.getTrainingsFilter(dateFrom, dateTo)

        if (result is Result.Success) {
            emit(result)
        }
    }

    fun addTraining(training: Training) {
        trainingsRepository.addTraining(training)
    }

    fun updateTraining(training: Training) {
        trainingsRepository.updateTraining(training)
    }

    fun deleteTraining(trainingId: String) {
        trainingsRepository.deleteTraining(trainingId)
    }

    fun isTeamSelected(): Boolean {
        return !preferenceManager.getStringValue(TEAM_ID_KEY).isNullOrEmpty()
    }

    fun getPreferenceManager(): PreferenceManger {
        return preferenceManager
    }

    fun loadExercisesInTraining(): LiveData<Result<List<ExerciseInTraining>>> =
        liveData(coroutineContext) {
            emit(Result.Loading)

            val result = exerciseInTrainingRepository.getExercises()

            if (result is Result.Success) {
                emit(result)
            }
        }

    fun addExerciseToTraining(exercise: Exercise) {
        exerciseInTrainingRepository.updateExerciseInTraining(
            ExerciseInTraining(
                exercise.id,
                exercise.name,
                exercise.category,
                exercise.description,
                exercise.imageUrl,
                0
            )
        )
    }

    fun updateExerciseInTraining(exercise: ExerciseInTraining) {
        Log.println(Log.ERROR, "TrainingsVM", exercise.toString())
        exerciseInTrainingRepository.updateExerciseInTraining(exercise)
    }

    fun deleteExercise(exerciseId: String) {
        exerciseInTrainingRepository.deleteExercise(exerciseId)
    }

    fun convertDateToString(date: Timestamp): String {
        return convertLongToDate(date.seconds)
    }
}