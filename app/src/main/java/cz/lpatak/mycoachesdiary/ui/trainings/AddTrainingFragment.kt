package cz.lpatak.mycoachesdiary.ui.trainings

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
import cz.lpatak.mycoachesdiary.databinding.FragmentAddTrainingBinding
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingUIModel
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import cz.lpatak.mycoachesdiary.util.stringDateToTimestamp
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class AddTrainingFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private val trainingsViewModel: TrainingsViewModel by viewModel()
    private lateinit var binding: FragmentAddTrainingBinding
    private var timestamp = Timestamp(Date(0))

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
            btnSetDate.setOnClickListener { pickDate() }
        }
        return binding.root
    }


    private fun createTeam() {
        val training = Training(
                "",
                binding.trainingModel!!.place.value,
                0,
                timestamp,
                binding.trainingModel!!.startTime.value,
                binding.trainingModel!!.endTime.value,
                0,
                0
        )
        Log.println(Log.ERROR, "AddTraining:", training.toString())
        trainingsViewModel.addTraining(training)
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