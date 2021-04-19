package cz.lpatak.mycoachesdiary.ui.trainings.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.DBConstants.Companion.TRAINING_ID_KEY
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.databinding.TrainingItemBinding
import cz.lpatak.mycoachesdiary.ui.base.DataBoundListAdapter
import cz.lpatak.mycoachesdiary.ui.trainings.TrainingsFragmentDirections
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import cz.lpatak.mycoachesdiary.util.PreferenceManger


class TrainingsAdapter(
    private val onClick: ((Training) -> Unit)? = null
) : DataBoundListAdapter<Training, TrainingItemBinding>(
    diffCallback = object : DiffUtil.ItemCallback<Training>() {
        override fun areItemsTheSame(oldItem: Training, newItem: Training): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Training, newItem: Training): Boolean {
            return oldItem == newItem
        }
    }
) {
    private lateinit var viewModel: TrainingsViewModel
    fun setViewModel(viewModel: TrainingsViewModel) {
        this.viewModel = viewModel
    }

    private lateinit var preferenceManager: PreferenceManger
    fun setPreferenceManager(preferenceManager: PreferenceManger) {
        this.preferenceManager = preferenceManager
    }

    override fun createBinding(parent: ViewGroup): TrainingItemBinding {
        return DataBindingUtil.inflate<TrainingItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.training_item,
            parent, false
        ).apply {
            this.root.setOnClickListener {
                this.training?.let { goToTrainingDetail(this.root, it) }
            }
        }
    }


    override fun bind(binding: TrainingItemBinding, item: Training) {
        binding.training = item
        binding.trainingDateAndTime =
            viewModel.convertDateToString(item.date!!) + " (" + item.startTime + "-" + item.endTime + ")"
    }

    private fun goToTrainingDetail(view: View, training: Training) {
        preferenceManager.run {
            putStringValue(TRAINING_ID_KEY, training.id.toString())
        }

        val directions =
            TrainingsFragmentDirections.actionNavigationTrainingsToNavigationTrainigDetail(training)
        view.findNavController().navigate(directions)
    }

}
