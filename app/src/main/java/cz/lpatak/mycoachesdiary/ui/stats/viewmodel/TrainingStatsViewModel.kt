package cz.lpatak.mycoachesdiary.ui.stats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.data.model.DBConstants
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.data.repositories.StatsRepositoryImpl
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import kotlinx.coroutines.Dispatchers
import java.util.*
import kotlin.collections.HashMap

class TrainingStatsViewModel(
    preferenceManager: PreferenceManger,
    private val statsRepository: StatsRepositoryImpl
) : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO
    val isTeamSelected = !preferenceManager.getStringValue(DBConstants.TEAM_ID_KEY).isNullOrEmpty()

    private val exerciseList: MutableList<ExerciseInTraining?> = mutableListOf()
    private val trainingsList: MutableList<Training?> = mutableListOf()

    var trainings: Int = 0

    var category = HashMap<String, Int>()

    fun loadTrainingsStats(dateFrom: Timestamp, dateTo: Timestamp) {
        statsRepository.getTrainings(dateFrom, dateTo).addOnSuccessListener {
            if (!it.isEmpty) {
                val list = it.documents
                for (doc in list) {
                    val training = doc.toObject(Training::class.java)
                    trainingsList.add(training)
                }
                trainings = trainingsList.size
            }
        }.continueWith {
            for (training in trainingsList) {
                if (training != null) {
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
        }
    }

    fun getEntries(): ArrayList<PieEntry> {
        val entries = ArrayList<PieEntry>()
        for (i in 0 until 9) {
            entries.add(PieEntry((Math.random() * 60 + 40).toFloat(), i.toString()))
        }
        return entries
    }

}
