package cz.lpatak.mycoachesdiary.ui.exercises.viewmodel

import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import cz.lpatak.mycoachesdiary.ui.base.ObservableViewModel

class ExerciseUIModel : ObservableViewModel() {

    val areInputsReady = MediatorLiveData<Boolean>()

    val name = MutableLiveData("")
    val description = MutableLiveData("")
    val category = MutableLiveData("")
    val fileUri = MutableLiveData<Uri>(null)
    val imageReady = Transformations.map(fileUri) { it != null }

    init {
        areInputsReady.addSource(name) { areInputsReady.value = checkInputs() }
        areInputsReady.addSource(description) { areInputsReady.value = checkInputs() }
    }

    fun checkInputs(): Boolean {
        return !(
                name.value.isNullOrEmpty() || description.value.isNullOrEmpty()
                )
    }
}