package cz.lpatak.mycoachesdiary.ui.trainings.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Exercise
import cz.lpatak.mycoachesdiary.databinding.ExerciseItemBinding
import cz.lpatak.mycoachesdiary.ui.base.DataBoundListAdapter
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel

class ExerciseAdapter(
    private val onClick: ((ExerciseItemBinding) -> Unit)? = null
) : DataBoundListAdapter<Exercise, ExerciseItemBinding>(
    diffCallback = object : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }
    }
) {
    private lateinit var viewModel: TrainingsViewModel

    fun setViewModel(viewModel: TrainingsViewModel) {
        this.viewModel = viewModel
    }

    override fun createBinding(parent: ViewGroup): ExerciseItemBinding {
        return DataBindingUtil.inflate<ExerciseItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.exercise_item,
            parent, false
        ).apply {
            this.root.setOnClickListener {
                this.exercise?.let { addExerciseToTraining(it, this.root) }
            }
        }
    }

    override fun bind(binding: ExerciseItemBinding, item: Exercise) {
        binding.exercise = item
    }

    private fun addExerciseToTraining(exercise: Exercise, view: View) {
        viewModel.addExerciseToTraining(exercise)
        Toast.makeText(
            view.context,
            "Cvičení " + exercise.name + " bylo přidáno.",
            Toast.LENGTH_SHORT
        ).show()
    }
}
