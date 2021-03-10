package cz.lpatak.mycoachesdiary.ui.trainings

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingDetailInfoBinding
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingUIModel
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import cz.lpatak.mycoachesdiary.util.convertLongToDate
import cz.lpatak.mycoachesdiary.util.createTime
import cz.lpatak.mycoachesdiary.util.stringDateToTimestamp
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class TrainingDetailFragmentInfo(private val trainingFromArgs: Training) : Fragment(),
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
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
            btnSaveTraining.setOnClickListener { updateTraining(trainingModel) }
            trainingHelperLayout.btnSetDate.setOnClickListener { pickDate() }
            trainingHelperLayout.btnSetTimeFrom.setOnClickListener { pickTimeFrom() }
            trainingHelperLayout.btnSetTimeTo.setOnClickListener { pickTimeTo() }
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


    private fun updateTraining(uiModel: TrainingUIModel?) {
        var date = trainingFromArgs.date
        if (timestamp != Timestamp(Date(0))) {
            date = timestamp
        }

        if (uiModel != null) {
            trainingsViewModel.updateTraining(
                Training(
                    trainingFromArgs.id,
                    uiModel.place.value,
                    uiModel.rating.value!!,
                    date,
                    uiModel.startTime.value,
                    uiModel.endTime.value,
                    uiModel.players.value!!,
                    uiModel.goalkeepers.value!!
                )
            )
        }
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

    private var helper = false
    private fun pickTimeFrom() {
        helper = true
        pickTime()
    }

    private fun pickTimeTo() {
        helper = false
        pickTime()
    }

    private fun pickTime() {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        TimePickerDialog(this.requireContext(), this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val str = createTime(hourOfDay, minute)
        if (helper) {
            binding.trainingModel!!.startTime.value = str
        } else {
            binding.trainingModel!!.endTime.value = str
        }
    }
}