package cz.lpatak.mycoachesdiary.data.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import cz.lpatak.mycoachesdiary.data.model.DBConstants
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_DATE
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_TEAM
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.COLUMN_TYPE
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.ExercisesCOLLECTION
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.MatchesCOLLECTION
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TEAM_ID_KEY
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TrainingsCOLLECTION
import cz.lpatak.mycoachesdiary.data.source.FirestoreSource
import cz.lpatak.mycoachesdiary.util.PreferenceManger


class StatsRepositoryImpl(
    private val firestoreSource: FirestoreSource,
    private val preferenceManager: PreferenceManger
) : StatsRepository {

    private val matchesPath: CollectionReference
        get() {
            return firestoreSource.firestore.collection(MatchesCOLLECTION)
        }

    private val trainingsPath: CollectionReference
        get() {
            return firestoreSource.firestore.collection(DBConstants.TeamsCOLLECTION)
                .document(preferenceManager.getStringValue(TEAM_ID_KEY).toString())
                .collection(TrainingsCOLLECTION)
        }

    override fun getMatchesFilter(
        matchCategory: String,
        all: Boolean,
        dateFrom: Timestamp,
        dateTo: Timestamp
    ): Task<QuerySnapshot> {
        var query = matchesPath
            .whereEqualTo(COLUMN_TEAM, preferenceManager.getStringValue(TEAM_ID_KEY))
            .whereGreaterThanOrEqualTo(COLUMN_DATE, dateFrom)
            .whereLessThanOrEqualTo(COLUMN_DATE, dateTo)

        if (!all) {
            query = matchesPath
                .whereEqualTo(COLUMN_TEAM, preferenceManager.getStringValue(TEAM_ID_KEY))
                .whereEqualTo(COLUMN_TYPE, matchCategory)
                .whereGreaterThanOrEqualTo(COLUMN_DATE, dateFrom)
                .whereLessThanOrEqualTo(COLUMN_DATE, dateTo)
        }

        return query.get()
    }

    override fun getTrainings(dateFrom: Timestamp, dateTo: Timestamp): Task<QuerySnapshot> {
        return trainingsPath
            .whereGreaterThanOrEqualTo(COLUMN_DATE, dateFrom)
            .whereLessThanOrEqualTo(COLUMN_DATE, dateTo)
            .get()
    }


    override fun getExercises(trainingID: String): Task<QuerySnapshot> {
        return trainingsPath.document(trainingID).collection(ExercisesCOLLECTION).get()
    }

}
