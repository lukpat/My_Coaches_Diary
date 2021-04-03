package cz.lpatak.mycoachesdiary.ui.exercises.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cz.lpatak.mycoachesdiary.data.model.Exercise
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.repositories.ExerciseRepositoryImpl
import kotlinx.coroutines.Dispatchers
import java.util.*

class ExercisesViewModel(private val exerciseRepository: ExerciseRepositoryImpl) : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun loadExercises(): LiveData<Result<List<Exercise>>> = liveData(coroutineContext) {
        emit(Result.Loading)

        val result = exerciseRepository.getExercises()

        if (result is Result.Success) {
            emit(result)
        }
    }

    fun loadExercisesFilter(
        exerciseOwner: Boolean,
        category: String
    ): LiveData<Result<List<Exercise>>> = liveData(coroutineContext) {
        emit(Result.Loading)

        val result = exerciseRepository.getExercisesFilter(exerciseOwner, category)

        if (result is Result.Success) {
            emit(result)
        }
    }

    fun addExercise(exercise: Exercise) {
        exerciseRepository.addExerciseWithoutIMG(exercise)
    }

    fun searchData(query: String): LiveData<Result<List<Exercise>>> = liveData(coroutineContext) {
        emit(Result.Loading)

        val queryUpper = query.toUpperCase(Locale.ROOT)
        val result = exerciseRepository.searchData(queryUpper)

        if (result is Result.Success) {
            emit(result)
        }
    }
}

