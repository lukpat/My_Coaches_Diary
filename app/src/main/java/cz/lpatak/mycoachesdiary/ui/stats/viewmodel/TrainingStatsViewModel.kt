package cz.lpatak.mycoachesdiary.ui.stats.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.data.model.DBConstants
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.data.repositories.StatsRepositoryImpl
import cz.lpatak.mycoachesdiary.util.PreferenceManger

class TrainingStatsViewModel(private val preferenceManager: PreferenceManger, private val statsRepository: StatsRepositoryImpl) : ViewModel() {
    private val exerciseList: MutableList<ExerciseInTraining?> = mutableListOf()
    private val trainingsList: MutableList<Training?> = mutableListOf()
    var category = HashMap<String, Int>()


    var trainingsCount = 0
    private var ratingSum = 0
    private var playersSum = 0
    private var goalkeepersSum = 0

    fun loadTrainingsStats(dateFrom: Timestamp, dateTo: Timestamp) {
        statsRepository.getTrainings(dateFrom, dateTo).addOnSuccessListener {
            if (!it.isEmpty) {
                val list = it.documents
                for (doc in list) {
                    val training = doc.toObject(Training::class.java)
                    trainingsList.add(training)
                }
            }
        }.continueWith {
            for (training in trainingsList) {
                if (training != null) {
                    calculateTrainingStats(training)
                    statsRepository.getExercises(training.id.toString()).addOnSuccessListener {
                        if (!it.isEmpty) {
                            val list = it.documents
                            for (doc in list) {
                                val exercise = doc.toObject(ExerciseInTraining::class.java)
                                exerciseList.add(exercise)
                            }
                        }
                    }
                }
            }
        }.continueWith {
            for (exercise in exerciseList) {
                if (exercise != null) {
                    calculateExerciseCategoryTime(exercise)
                }
            }

        }.continueWith {
            printMap()
        }.continueWith {
            trainingsCount = trainingsList.size
        }
    }

    fun isTeamSelected(): Boolean {
        return !preferenceManager.getStringValue(DBConstants.TEAM_ID_KEY).isNullOrEmpty()
    }

    private fun calculateTrainingStats(training: Training) {
        ratingSum += training.rating
        playersSum += training.players
        goalkeepersSum += training.goalkeepers
    }

    private fun calculateExerciseCategoryTime(exercise: ExerciseInTraining) {
        val index = exercise.category.toString()
        val exerciseTime = exercise.time
        var exerciseCount = category[index]?.plus(exerciseTime)

        if (exerciseCount == null) exerciseCount = exerciseTime

        category[index] = exerciseCount
    }

    private fun printMap() {
        for ((k, v) in category) {
            println("$k = $v")
        }
    }
}