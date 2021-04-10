package cz.lpatak.mycoachesdiary.ui.stats

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
import cz.lpatak.mycoachesdiary.databinding.FragmentStatsBinding
import cz.lpatak.mycoachesdiary.ui.stats.viewmodel.TrainingStatsViewModel
import cz.lpatak.mycoachesdiary.util.stringDateToTimestamp
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class StatsFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private val trainingStatsViewModel: TrainingStatsViewModel by viewModel()
    private lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stats, container, false)

        with(binding) {
            filterOn = false
            isTeamSelected = trainingStatsViewModel.isTeamSelected
            btnTrainingStats.setOnClickListener { setTrainingStatsLayout() }
            btnMatchesStats.setOnClickListener { setMatchStatsLayout() }
        }

        setTrainingStatsLayout()

        return binding.root
    }

    private fun setTrainingStatsLayout() {
        with(binding){
            filterOn = false
            trainingFilter.btnSetFilter.text = getString(R.string.get_training_stats)
            trainingFilter.dateError.text = ""
            trainingFilter.btnSetFilter.setOnClickListener { applyFilter() }
            trainingFilter.dateFrom.setOnClickListener { pickDateFrom() }
            trainingFilter.dateTo.setOnClickListener { pickDateTo() }
            dateFrom = Timestamp(Date(0))
            dateTo = Timestamp(Date(0))
        }
    }

    private fun setMatchStatsLayout(){
        with(binding) {
            filterOn = true
            matchFilter.btnSetFilter.text = getString(R.string.get_match_stats)
            matchFilter.dateError.text = ""
            matchFilter.btnSetFilter.setOnClickListener { applyFilter() }
            matchFilter.dateFrom.setOnClickListener { pickDateFrom() }
            matchFilter.dateTo.setOnClickListener { pickDateTo() }
            dateFrom = Timestamp(Date(0))
            dateTo = Timestamp(Date(0))
        }
    }

    private fun applyFilter() {
        val default = Timestamp(Date(0))
        if (dateFrom.seconds > dateTo.seconds || dateFrom.seconds == default.seconds || dateTo.seconds == default.seconds) {
            binding.trainingFilter.dateError.text = getString(R.string.date_error)
            binding.matchFilter.dateError.text = getString(R.string.date_error)
            return
        } else {
            val directions = if (binding.filterOn!!) {

                var bool = false
                if (binding.matchFilter.type.selectedItem.toString() == "Všechny zápasy") {
                    bool = true
                }

                StatsFragmentDirections.actionNavigationStatsToNavigationMatchStats(
                        binding.matchFilter.type.selectedItem.toString(),
                        bool,
                        dateFrom,
                        dateTo
                )
            } else {
                StatsFragmentDirections.actionNavigationStatsToNavigationTrainingStats(
                        dateFrom,
                        dateTo
                )
            }
            findNavController().navigate(directions)
        }

    }

    private var dateFrom = Timestamp(Date(0))
    private var dateTo = Timestamp(Date(0))
    private var helper = false

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val realMonth = month + 1
        val str = "$dayOfMonth.$realMonth.$year"

        if (helper) {
            binding.trainingFilter.dateFromVal = str
            binding.matchFilter.dateFromVal = str
            dateFrom = stringDateToTimestamp(str)
        } else {
            binding.trainingFilter.dateToVal = str
            binding.matchFilter.dateToVal = str
            dateTo = stringDateToTimestamp(str)
        }
    }

    private fun pickDateFrom() {
        helper = true
        pickDate()
    }

    private fun pickDateTo() {
        helper = false
        pickDate()
    }

    private fun pickDate() {
        val cal = Calendar.getInstance()
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)

        DatePickerDialog(this.requireContext(), this, year, month, dayOfMonth).show()
    }
}
