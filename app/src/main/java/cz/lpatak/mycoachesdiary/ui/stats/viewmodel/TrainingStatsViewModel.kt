package cz.lpatak.mycoachesdiary.ui.stats.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.data.model.DBConstants
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.data.repositories.ExerciseInTrainingRepositoryImpl
import cz.lpatak.mycoachesdiary.data.repositories.TrainingRepositoryImpl
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingStatsBinding
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import kotlinx.coroutines.Dispatchers
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.HashMap

class TrainingStatsViewModel(
        preferenceManager: PreferenceManger,
        private val trainingRepository: TrainingRepositoryImpl,
        private val exerciseInTrainingRepository: ExerciseInTrainingRepositoryImpl
) : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO
    val isTeamSelected = !preferenceManager.getStringValue(DBConstants.TEAM_ID_KEY).isNullOrEmpty()

    fun loadTrainings(dateFrom: Timestamp, dateTo: Timestamp): LiveData<Result<List<Training>>> = liveData(coroutineContext) {
        emit(Result.Loading)

        val result = trainingRepository.getTrainingsFilter(dateFrom, dateTo)

        if (result is Result.Success) {
            result.data
            emit(result)
        }
    }

    fun loadExercises(collection: String): LiveData<Result<List<ExerciseInTraining>>> = liveData(coroutineContext) {
        emit(Result.Loading)

        val result = exerciseInTrainingRepository.getExercises(collection)

        if (result is Result.Success) {
            result.data
            emit(result)
        }
    }

    fun setTrainingStats(trainingsList: MutableList<Training>, binding: FragmentTrainingStatsBinding) {
        with(binding) {
            val str = "Celkový počet tréninků:" + trainingsList.size
            trainingsCount.text = str

            var players = 0
            var goalkeepers = 0
            var rating = 0

            for (training in trainingsList) {
                players += training.players
                goalkeepers += training.goalkeepers
                rating += training.rating
            }

            var playersStr = ""
            if (trainingsList.size > 0) {
                playersStr = BigDecimal((players / trainingsList.size.toDouble())).setScale(1, RoundingMode.HALF_EVEN).toString()
            }

            var goalkeepersStr = ""
            if (trainingsList.size > 0) {
                goalkeepersStr = BigDecimal((goalkeepers / trainingsList.size.toDouble())).setScale(1, RoundingMode.HALF_EVEN).toString()
            }

            var ratingStr = ""
            if (trainingsList.size > 0) {
                ratingStr = BigDecimal((rating / trainingsList.size.toDouble())).setScale(1, RoundingMode.HALF_EVEN).toString()
            }

            with(binding) {
                trainingStatsLayout.players.text = playersStr
                trainingStatsLayout.goalkeepers.text = goalkeepersStr
                trainingStatsLayout.rating.text = ratingStr
            }

        }
    }

    val category = HashMap<String, Int>()
    fun getEntries(exerciseList: MutableList<ExerciseInTraining>): ArrayList<PieEntry> {

        for (exercise in exerciseList){
            calculateExerciseCategoryTime(exercise)
        }

        val entries = ArrayList<PieEntry>()
        for ((k, v) in category) {
            entries.add(PieEntry(v.toFloat(), k))
        }

        return entries
    }

    private fun calculateExerciseCategoryTime(exercise: ExerciseInTraining) {
        val index = exercise.category.toString()
        val exerciseTime = exercise.time
        var exerciseCount = category[index]?.plus(exerciseTime)

        if (exerciseCount == null) exerciseCount = exerciseTime

        category[index] = exerciseCount
    }

}
