package cz.lpatak.mycoachesdiary.data.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot

interface StatsRepository {
    fun getMatchesFilter(
            matchCategory: String,
            all: Boolean,
            dateFrom: Timestamp,
            dateTo: Timestamp
    ): Task<QuerySnapshot>

    fun getTrainings(dateFrom: Timestamp, dateTo: Timestamp): Task<QuerySnapshot>
    fun getExercises(trainingID: String): Task<QuerySnapshot>
}