package cz.lpatak.mycoachesdiary.ui.trainings.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.databinding.CustomDialogBinding
import cz.lpatak.mycoachesdiary.ui.trainings.TrainingDetailFragmentExercises
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.ExerciseInTrainingUIModel
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import cz.lpatak.mycoachesdiary.util.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateExerciseTimeDialog(
    private val exercise: ExerciseInTraining,
    private val fragment: TrainingDetailFragmentExercises,
    private val add: Boolean
) : DialogFragment() {
    private lateinit var binding: CustomDialogBinding
    private val exerciseUIModel: ExerciseInTrainingUIModel by viewModel()
    private val trainingModel: TrainingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog, container, false)

        with(binding) {
            exerciseModel = exerciseUIModel
            btnAdd.setOnClickListener { chooseAction() }
            btnCancel.setOnClickListener { dismiss() }
        }

        setExercise()

        return binding.root
    }

    private fun setExercise() {
        binding.exerciseModel!!.name.value = exercise.name
        binding.exerciseModel!!.time.value = exercise.time.toString()
    }

    private fun chooseAction() {
        val exerciseInTraining = ExerciseInTraining(
            exercise.id,
            exercise.name,
            exercise.category,
            exercise.description,
            exercise.imageUrl,
            exerciseUIModel.time.value!!.toInt()
        )

        if (exerciseUIModel.checkTime()) {
            trainingModel.updateExerciseInTraining(exerciseInTraining)
            dismiss()
            if (add) {
                findNavController().navigateUp()
            }
            fragment.loadExercises()
        } else {
            showToast(getString(R.string.exercise_time_must_be_between))
        }
    }

}