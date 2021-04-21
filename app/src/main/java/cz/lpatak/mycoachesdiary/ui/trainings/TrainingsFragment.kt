package cz.lpatak.mycoachesdiary.ui.trainings

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingsBinding
import cz.lpatak.mycoachesdiary.ui.base.FilterMenuBaseFragment
import cz.lpatak.mycoachesdiary.ui.trainings.util.TrainingsAdapter
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import cz.lpatak.mycoachesdiary.util.stringDateToTimestamp
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TrainingsFragment : FilterMenuBaseFragment() {
    private val trainingsViewModel: TrainingsViewModel by viewModel()
    private lateinit var binding: FragmentTrainingsBinding
    private val adapter: TrainingsAdapter = TrainingsAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trainings, container, false)
        with(binding) {
            lifecycleOwner = this@TrainingsFragment
            trainingsList.adapter = adapter
            fabAddTraining.setOnClickListener {
                val directions =
                        TrainingsFragmentDirections.actionNavigationTrainingsToNavigationAddTraining()
                findNavController().navigate(directions)
            }
            isTeamSelected = trainingsViewModel.isTeamSelected
            filterOn = false
            filterLayout.btnSetFilter.setOnClickListener { applyFilter() }
            filterLayout.dateFrom.setOnClickListener { pickDateFrom() }
            filterLayout.dateTo.setOnClickListener { pickDateTo() }
        }

        adapter.setViewModel(trainingsViewModel)
        adapter.setPreferenceManager(trainingsViewModel.getPreferenceManager())

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadTrainings()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> {
                binding.filterOn = binding.filterOn == false
                binding.filterLayout.dateError.text = ""
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadTrainings() {
        trainingsViewModel.loadTrainings().observe(viewLifecycleOwner, { result ->
            binding.result = result
            if (result is Result.Success) {
                adapter.submitList(result.data)
            }
        })
    }


    private fun loadTrainingsWithFilter(dateFrom: Timestamp, dateTo: Timestamp) {
        trainingsViewModel.loadTrainingsWithFilter(dateFrom, dateTo)
                .observe(viewLifecycleOwner, { result ->
                    binding.result = result
                    if (result is Result.Success) {
                        adapter.submitList(result.data)
                    }
                })
    }

    private fun applyFilter() {
        val default = Timestamp(Date(0))
        if (dateFrom.seconds > dateTo.seconds || dateFrom.seconds == default.seconds || dateTo.seconds == default.seconds) {
            binding.filterLayout.dateError.text = getString(R.string.date_error)
            return
        }

        loadTrainingsWithFilter(dateFrom, dateTo)
        binding.filterOn = false
    }


    private var dateFrom = Timestamp(Date(0))
    private var dateTo = Timestamp(Date(0))
    private var helper = false

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val realMonth = month + 1
        val str = "$dayOfMonth.$realMonth.$year"

        if (helper) {
            binding.filterLayout.dateFromVal = str
            dateFrom = stringDateToTimestamp(str)
        } else {
            binding.filterLayout.dateToVal = str
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