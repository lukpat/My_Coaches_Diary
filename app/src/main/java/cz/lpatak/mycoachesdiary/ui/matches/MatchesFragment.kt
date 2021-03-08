package cz.lpatak.mycoachesdiary.ui.matches

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchesBinding
import cz.lpatak.mycoachesdiary.ui.matches.util.MatchesAdapter
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchesViewModel
import cz.lpatak.mycoachesdiary.util.stringDateToTimestamp
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MatchesFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private val matchesViewModel: MatchesViewModel by viewModel()
    private lateinit var binding: FragmentMatchesBinding
    private val adapter: MatchesAdapter = MatchesAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_matches, container, false)
        with(binding) {
            lifecycleOwner = this@MatchesFragment
            matchesList.adapter = adapter
            fabAddMatch.setOnClickListener {
                val directions =
                        MatchesFragmentDirections.actionNavigationMatchesToNavigationAddMatch()
                findNavController().navigate(directions)
            }
            isTeamSelected = matchesViewModel.isTeamSelected()
            filterOn = false
            btnSetFilter.setOnClickListener { applyFilter() }
            btnSetDateFrom.setOnClickListener { pickDateFrom() }
            btnSetDateTo.setOnClickListener { pickDateTo() }
        }

        adapter.setViewModel(matchesViewModel)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> {
                binding.filterOn = binding.filterOn == false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        loadMatches()
    }

    private fun loadMatches() {
        matchesViewModel.loadMatches().observe(viewLifecycleOwner, { result ->
            binding.result = result
            if (result is Result.Success) {
                adapter.submitList(result.data)
            }
        })
    }

    private fun loadMatchesWithFilter(matchCategory: String, all: Boolean, dateFrom: Timestamp, dateTo: Timestamp) {
        matchesViewModel.loadMatchesFilter(matchCategory, all, dateFrom, dateTo).observe(viewLifecycleOwner, { result ->
            binding.result = result
            if (result is Result.Success) {
                adapter.submitList(result.data)
            }
        })
    }

    private fun applyFilter() {
        Log.println(Log.ERROR, "dateFrom", dateFrom.toString())
        Log.println(Log.ERROR, "dateTo", dateTo.toString())
        if (dateFrom.seconds >= dateTo.seconds) {
            val error = "Datum počátku je později než datum konce, takže filtr není možné provést"
            binding.dateError.text = error
            return
        }

        val matchCategory = binding.matchCategory.selectedItem.toString()
        var all = false
        if (matchCategory == "Všechny zápasy") {
            all = true
        }

        loadMatchesWithFilter(matchCategory, all, dateFrom, dateTo)
        binding.filterOn = false
    }


    private var dateFrom = Timestamp(Date(0))
    private var dateTo = Timestamp(Date(0))
    private var helper = false

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val realMonth = month + 1
        val str = "$dayOfMonth.$realMonth.$year"

        if (helper) {
            binding.dateFrom.text = str
            dateFrom = stringDateToTimestamp(str)
        } else {
            binding.dateTo.text = str
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