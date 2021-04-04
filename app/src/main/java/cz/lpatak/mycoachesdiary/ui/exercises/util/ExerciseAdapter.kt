package cz.lpatak.mycoachesdiary.ui.exercises.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Exercise
import cz.lpatak.mycoachesdiary.databinding.ExerciseItemBinding
import cz.lpatak.mycoachesdiary.ui.base.DataBoundListAdapter
import cz.lpatak.mycoachesdiary.ui.exercises.ExerciseLibraryFragmentDirections
import cz.lpatak.mycoachesdiary.ui.exercises.viewmodel.ExercisesViewModel

class ExerciseAdapter(
        private val onClick: ((Exercise) -> Unit)? = null
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
    private lateinit var viewModel: ExercisesViewModel

    fun setViewModel(viewModel: ExercisesViewModel) {
        this.viewModel = viewModel
    }

    override fun createBinding(parent: ViewGroup): ExerciseItemBinding {
        return DataBindingUtil.inflate<ExerciseItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.exercise_item,
                parent, false
        ).apply {
            this.root.setOnClickListener {
                this.exercise?.let { goToExerciseDetail(this.root, it) }
            }
        }
    }

    override fun bind(binding: ExerciseItemBinding, item: Exercise) {
        binding.exercise = item
    }

    private fun goToExerciseDetail(view: View, exercise: Exercise) {
        val directions =
                ExerciseLibraryFragmentDirections.actionNavigationExerciseLibraryToNavigationExerciseDetail(
                        exercise
                )
        view.findNavController().navigate(directions)
    }
}