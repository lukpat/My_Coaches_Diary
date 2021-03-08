package cz.lpatak.mycoachesdiary.ui.trainings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.databinding.FragmentAddTrainingBinding
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingUIModel
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddTrainingFragment : Fragment() {
    private val trainingsViewModel: TrainingsViewModel by viewModel()
    private lateinit var binding: FragmentAddTrainingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_training, container, false)
        with(binding) {
            lifecycleOwner = this@AddTrainingFragment
            trainingModel = TrainingUIModel()
            btnSaveTraining.setOnClickListener { createTeam() }
        }
        return binding.root
    }


    private fun createTeam() {
        val training = Training(
            "",
            binding.trainingModel!!.place.value,
            0,
            binding.trainingModel!!.date.value,
            binding.trainingModel!!.startTime.value,
            binding.trainingModel!!.endTime.value,
            0,
            0
        )
        Log.println(Log.ERROR, "AddTraining:", training.toString())
        trainingsViewModel.addTraining(training)
        findNavController().navigateUp()
    }

}