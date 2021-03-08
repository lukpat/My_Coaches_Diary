package cz.lpatak.mycoachesdiary.ui.trainings.util

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.databinding.ExerciseInTrainingItemBinding
import cz.lpatak.mycoachesdiary.ui.base.DataBoundListAdapter
import cz.lpatak.mycoachesdiary.ui.trainings.TrainingDetailFragmentDirections
import cz.lpatak.mycoachesdiary.ui.trainings.TrainingDetailFragmentExercisesDirections
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel

class ExerciseInTrainingAdapter(
    private val onClick: ((ExerciseInTraining) -> Unit)? = null
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

    fun setViewModel(viewModel: TrainingsViewModel) {
        this.viewModel = viewModel
    }

    override fun createBinding(parent: ViewGroup): ExerciseInTrainingItemBinding {
        return DataBindingUtil.inflate<ExerciseInTrainingItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.exercise_in_training_item,
            parent, false
        ).apply {
            this.root.setOnClickListener {
                goToExerciseDetail(it, this.exercise!!.id.toString())
            }
            /* this.buttonDelete.setOnClickListener {
                  this.exercise?.let { deleteExercise(this.root, it) }
              }*/
        }
    }

    override fun bind(binding: ExerciseInTrainingItemBinding, item: ExerciseInTraining) {
        binding.exercise = item
    }

    private fun deleteExercise(view: View, exercise: ExerciseInTraining) {
        val directions =
            TrainingDetailFragmentExercisesDirections.actionNavigationTrainingDetailExercisesSelf()

        AlertDialog.Builder(view.context)
            .setTitle(R.string.delete_exercise_title)
            .setMessage(R.string.delete_exercise_from_training_alert)
            .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                viewModel.deleteExercise(exercise.id.toString())
                view.findNavController().navigate(directions)
            })
            .setNegativeButton(R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun goToExerciseDetail(view: View, exerciseId: String) {
        val exercise = viewModel.getExercise(exerciseId)
        val directions =
            TrainingDetailFragmentDirections.actionNavigationTrainingDetailToNavigationExerciseDetail(
                exercise
            )
        view.findNavController().navigate(directions)
    }
}


