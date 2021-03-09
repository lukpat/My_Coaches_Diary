package cz.lpatak.mycoachesdiary.ui.trainings

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingDetailInfoBinding
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingUIModel
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import cz.lpatak.mycoachesdiary.util.convertLongToDate
import cz.lpatak.mycoachesdiary.util.stringDateToTimestamp
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class TrainingDetailFragmentInfo(private val trainingFromArgs: Training) : Fragment(), DatePickerDialog.OnDateSetListener {
    private val trainingsViewModel: TrainingsViewModel by viewModel()
    private lateinit var binding: FragmentTrainingDetailInfoBinding
    private var timestamp = Timestamp(Date(0))

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
            btnSaveTraining.setOnClickListener { updateTeam(trainingModel) }
            btnSetDate.setOnClickListener { pickDate() }
        }

        setTraining(binding.trainingModel)

        return binding.root
    }

    private fun setTraining(uiModel: TrainingUIModel?) {
        uiModel?.place?.value = trainingFromArgs.place
        uiModel?.date?.value = convertLongToDate(trainingFromArgs.date!!.seconds)
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
                            timestamp,
                            uiModel.startTime.value,
                            uiModel.endTime.value,
                            uiModel.players.value!!,
                            uiModel.goalkeepers.value!!
                    )
            )
        }
        findNavController().navigateUp()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val realMonth = month + 1

        val str = "$dayOfMonth.$realMonth.$year"
        binding.trainingModel!!.date.value = str
        timestamp = stringDateToTimestamp(str)
    }

    private fun pickDate() {
        val cal = Calendar.getInstance()
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)

        DatePickerDialog(this.requireContext(), this, year, month, dayOfMonth).show()
    }


}