package cz.lpatak.mycoachesdiary.ui.trainings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.DBConstants
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.data.repositories.ExerciseInTrainingRepositoryImpl
import cz.lpatak.mycoachesdiary.data.repositories.TrainingRepositoryImpl
import cz.lpatak.mycoachesdiary.ui.trainings.AddExerciseToTrainingFragment
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import cz.lpatak.mycoachesdiary.util.convertLongToDate
import cz.lpatak.mycoachesdiary.util.showToast
import kotlinx.coroutines.Dispatchers


class TrainingsViewModel(
        private val trainingsRepository: TrainingRepositoryImpl,
        private val exerciseInTrainingRepository: ExerciseInTrainingRepositoryImpl,
        private val preferenceManager: PreferenceManger
) : ViewModel() {
    val isTeamSelected = !preferenceManager.getStringValue(DBConstants.TEAM_ID_KEY).isNullOrEmpty()

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

    fun updateExerciseInTraining(exercise: ExerciseInTraining) {
        exerciseInTrainingRepository.updateExerciseInTraining(exercise)
    }

    fun deleteExercise(exerciseId: String) {
        exerciseInTrainingRepository.deleteExercise(exerciseId)
    }

    fun convertDateToString(date: Timestamp): String {
        return convertLongToDate(date.seconds)
    }

    fun addExerciseToTraining(exercise: ExerciseInTraining, fragment: AddExerciseToTrainingFragment) {
        exerciseInTrainingRepository.getDocument(exercise.id.toString()).addOnSuccessListener {
            if (!it.exists()) {
                updateExerciseInTraining(exercise)
                fragment.showToast("Cvičení " + exercise.name + " bylo přidáno do tréninku")
                fragment.findNavController().navigateUp()
            } else {
                fragment.showToast(R.string.exercise_already_in_training)
            }
        }
    }
}