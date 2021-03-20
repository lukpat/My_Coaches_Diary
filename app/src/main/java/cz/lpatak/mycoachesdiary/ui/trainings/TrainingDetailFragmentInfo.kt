package cz.lpatak.mycoachesdiary.ui.trainings

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingDetailInfoBinding
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingUIModel
import cz.lpatak.mycoachesdiary.util.convertLongToDate
import cz.lpatak.mycoachesdiary.util.createTime
import cz.lpatak.mycoachesdiary.util.stringDateToTimestamp
import java.util.*


class TrainingDetailFragmentInfo(private val trainingFromArgs: Training, private val UIModel: TrainingUIModel) : Fragment(),
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
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
            trainingModel = UIModel
            trainingHelperLayout.date.setOnClickListener { pickDate() }
            trainingHelperLayout.startTime.setOnClickListener { pickTimeFrom() }
            trainingHelperLayout.endTime.setOnClickListener { pickTimeTo() }
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val realMonth = month + 1

        val str = "$dayOfMonth.$realMonth.$year"
        binding.trainingModel!!.date.value = str
        binding.trainingModel!!.timestamp = stringDateToTimestamp(str)
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