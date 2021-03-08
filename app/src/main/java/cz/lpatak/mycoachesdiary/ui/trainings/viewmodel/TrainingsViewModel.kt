package cz.lpatak.mycoachesdiary.ui.trainings.viewmodel

import android.util.Log
import androidx.lifecycle.*
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_CATEGORY
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_DESCRIPTION
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_IMAGE_URL
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_NAME
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_OWNER
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TEAM_ID_KEY
import cz.lpatak.mycoachesdiary.data.model.Exercise
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.data.repositories.ExerciseInTrainingRepositoryImpl
import cz.lpatak.mycoachesdiary.data.repositories.ExerciseRepositoryImpl
import cz.lpatak.mycoachesdiary.data.repositories.TrainingRepositoryImpl
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import kotlinx.coroutines.Dispatchers


class TrainingsViewModel(
        private val trainingsRepository: TrainingRepositoryImpl,
        private val exerciseInTrainingRepository: ExerciseInTrainingRepositoryImpl,
        private val exerciseRepository: ExerciseRepositoryImpl,
        private val preferenceManager: PreferenceManger
) : ViewModel() {

    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun loadTeams(): LiveData<Result<List<Training>>> = liveData(coroutineContext) {
        emit(Result.Loading)

        val result = trainingsRepository.getTrainings()

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
        exerciseInTrainingRepository.updateExercise(
                ExerciseInTraining(exercise.id, exercise.name, exercise.category, 0)
        )
    }

    fun setExerciseTime(exerciseInTraining: ExerciseInTraining) {
        exerciseInTrainingRepository.updateExercise(exerciseInTraining)
    }

    fun deleteExercise(exerciseId: String) {
        exerciseInTrainingRepository.deleteExercise(exerciseId)
    }

    fun loadExercises(): LiveData<Result<List<Exercise>>> = liveData(coroutineContext) {
        emit(Result.Loading)

        val result = exerciseRepository.getExercises()

        if (result is Result.Success) {
            emit(result)
        }
    }

    fun getExercise(exerciseId: String): Exercise {
        var exercise = Exercise()

        exerciseRepository.getExercise(exerciseId).addOnSuccessListener {
            val exercise2 = Exercise(
                    "",
                    it.get(COLUMN_OWNER).toString(),
                    it.get(COLUMN_NAME).toString(),
                    it.get(COLUMN_CATEGORY).toString(),
                    it.get(COLUMN_DESCRIPTION).toString(),
                    it.get(COLUMN_IMAGE_URL).toString()
            )
            exercise = exercise2
            Log.println(Log.ERROR, "TrainingsVM2", exercise.toString())
        }.addOnCompleteListener {
            return@addOnCompleteListener
        }
        Log.println(Log.ERROR, "TrainingsVM3", exercise.toString())
        return exercise
    }

}