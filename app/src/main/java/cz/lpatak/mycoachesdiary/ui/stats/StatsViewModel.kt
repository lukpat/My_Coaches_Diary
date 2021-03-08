package cz.lpatak.mycoachesdiary.ui.stats

import androidx.lifecycle.ViewModel
import cz.lpatak.mycoachesdiary.data.model.DBConstants
import cz.lpatak.mycoachesdiary.util.PreferenceManger

class StatsViewModel(private val preferenceManager: PreferenceManger) : ViewModel() {

    fun isTeamSelected(): Boolean {
        return !preferenceManager.getStringValue(DBConstants.TEAM_ID_KEY).isNullOrEmpty()
    }
}