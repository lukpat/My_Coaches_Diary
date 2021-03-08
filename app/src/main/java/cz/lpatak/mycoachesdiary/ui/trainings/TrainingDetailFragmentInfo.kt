package cz.lpatak.mycoachesdiary.ui.trainings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingDetailInfoBinding
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingUIModel
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class TrainingDetailFragmentInfo(private val trainingFromArgs: Training) : Fragment() {
    private val trainingsViewModel: TrainingsViewModel by viewModel()
    private lateinit var binding: FragmentTrainingDetailInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_training_detail_info,
            container,
            false
        )
        with(binding) {
            lifecycleOwner = this@TrainingDetailFragmentInfo
            trainingModel = TrainingUIModel()
            btnSaveTraining.setOnClickListener {
                updateTeam(trainingModel)
            }
        }

        setTraining(binding.trainingModel)

        return binding.root
    }

    private fun setTraining(uiModel: TrainingUIModel?) {
        uiModel?.place?.value = trainingFromArgs.place
        uiModel?.date?.value = trainingFromArgs.date
        uiModel?.startTime?.value = trainingFromArgs.startTime
        uiModel?.endTime?.value = trainingFromArgs.endTime
        uiModel?.goalkeepers?.value = trainingFromArgs.goalkeepers
        uiModel?.players?.value = trainingFromArgs.players
        uiModel?.rating?.value = trainingFromArgs.rating
    }


    private fun updateTeam(uiModel: TrainingUIModel?) {
        if (uiModel != null) {
            trainingsViewModel.updateTraining(
                Training(
                    trainingFromArgs.id,
                    uiModel.place.value,
                    uiModel.rating.value!!,
                    uiModel.date.value,
                    uiModel.startTime.value,
                    uiModel.endTime.value,
                    uiModel.players.value!!,
                    uiModel.goalkeepers.value!!
                )
            )
        }
        findNavController().navigateUp()
    }


}