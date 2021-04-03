package cz.lpatak.mycoachesdiary.ui.trainings.util

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Exercise
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.databinding.ExerciseInTrainingItemBinding
import cz.lpatak.mycoachesdiary.ui.base.DataBoundListAdapter
import cz.lpatak.mycoachesdiary.ui.exercises.viewmodel.ExercisesViewModel
import cz.lpatak.mycoachesdiary.ui.trainings.TrainingDetailFragmentDirections
import cz.lpatak.mycoachesdiary.ui.trainings.TrainingDetailFragmentExercises
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel

class ExerciseInTrainingAdapter(
    private val onClick: ((ExerciseInTraining) -> Unit)? = null,
    private val fragment: TrainingDetailFragmentExercises
) : DataBoundListAdapter<ExerciseInTraining, ExerciseInTrainingItemBinding>(
    diffCallback = object : DiffUtil.ItemCallback<ExerciseInTraining>() {
        override fun areItemsTheSame(
            oldItem: ExerciseInTraining,
            newItem: ExerciseInTraining
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ExerciseInTraining,
            newItem: ExerciseInTraining
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    private lateinit var viewModel: TrainingsViewModel
    private lateinit var exerciseViewModel: ExercisesViewModel

    fun setViewModel(viewModel: TrainingsViewModel) {
        this.viewModel = viewModel
    }

    fun setViewModel(viewModel: ExercisesViewModel) {
        this.exerciseViewModel = viewModel
    }

    override fun createBinding(parent: ViewGroup): ExerciseInTrainingItemBinding {
        return DataBindingUtil.inflate<ExerciseInTrainingItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.exercise_in_training_item,
            parent, false
        ).apply {
            this.buttonDelete.setOnClickListener {
                this.exercise?.let { deleteExercise(this.root, it.id.toString()) }
            }
            this.root.setOnClickListener {
                this.exercise?.let { goToExerciseDetail(this.root, it) }
            }
            this.exerciseTime.setOnClickListener {
                this.exercise?.let { setExerciseTime(this.root, it) }
            }
        }
    }

    override fun bind(binding: ExerciseInTrainingItemBinding, item: ExerciseInTraining) {
        binding.exercise = item
    }

    private fun deleteExercise(view: View, exerciseId: String) {
        AlertDialog.Builder(view.context)
            .setMessage(R.string.delete_exercise_from_training_alert)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteExercise(exerciseId)
                view.findFragment<TrainingDetailFragmentExercises>().loadExercises()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun goToExerciseDetail(view: View, exerciseInTraining: ExerciseInTraining) {
        val exercise = Exercise(
            exerciseInTraining.id,
            "",
            exerciseInTraining.name,
            "",
            exerciseInTraining.category,
            exerciseInTraining.description,
            exerciseInTraining.imageUrl
        )
        val directions =
            TrainingDetailFragmentDirections.actionNavigationTrainingDetailToNavigationExerciseDetail(
                exercise
            )
        view.findNavController().navigate(directions)
    }

    private fun setExerciseTime(view: View, exercise: ExerciseInTraining) {
        val exercise2 = Exercise(
            exercise.id,
            "",
            exercise.name,
            "",
            exercise.category,
            exercise.description,
            exercise.imageUrl
        )
        view.findFragment<TrainingDetailFragmentExercises>()
            .updateExerciseTime(exercise2, exercise.time)
    }
}


