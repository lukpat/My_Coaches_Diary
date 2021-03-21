package cz.lpatak.mycoachesdiary.ui.stats

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchStatsBinding
import cz.lpatak.mycoachesdiary.ui.stats.viewmodel.MatchStatsViewModel
import cz.lpatak.mycoachesdiary.util.stringDateToTimestamp
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MatchStatsFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: FragmentMatchStatsBinding
    private val matchStatsViewModel: MatchStatsViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_match_stats, container, false)
        with(binding) {
            lifecycleOwner = this@MatchStatsFragment
            filterOn = true
            filterLayout.btnSetFilter.text = "načíst statistiky"
            filterLayout.btnSetFilter.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    applyFilter()
                    setUI()
                }
            }
            filterLayout.dateFrom.setOnClickListener { pickDateFrom() }
            filterLayout.dateTo.setOnClickListener { pickDateTo() }
            matchStatsLayout.btnChangeData.setOnClickListener { filterOn = true }

        }
        return binding.root
    }


    private suspend fun applyFilter() {
        val bool = dateFrom.seconds >= dateTo.seconds
        val matchCategory = binding.filterLayout.type.selectedItem.toString()
        var all = false

        val request = GlobalScope.launch(Dispatchers.Main) {
            if (bool) {
                val error = "Datum počátku je později než datum konce, takže filtr není možné provést"
                binding.filterLayout.dateError.text = error
            } else {
                if (matchCategory == "Všechny zápasy") {
                    all = true
                }
                with(binding) {
                    filterLayout.dateError.visibility = View.GONE
                    filterOn = false
                }
                matchStatsViewModel.loadMatchStats(matchCategory, all, dateFrom, dateTo)
            }
        }
        request.join()
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

    @SuppressLint("SetTextI18n")
    private suspend fun setUI() {
        val request = GlobalScope.launch(Dispatchers.Main) {
            delay(500)
            with(binding.matchStatsLayout) {
                matchCount.text = "Celkový počet zápasů: " + matchStatsViewModel.matches
                wins.text = matchStatsViewModel.win.toString()
                draws.text = matchStatsViewModel.draw.toString()
                lost.text = matchStatsViewModel.lost.toString()
                winPerc.text = matchStatsViewModel.winPercentage.toString() + " %"

                powerPlaysTeam.text = matchStatsViewModel.powerPlaysTeam.toString()
                powerPlaysTeamSuccess.text = matchStatsViewModel.powerPlaysSuccessRate.toString() + " %"
                powerPlaysOpponent.text = matchStatsViewModel.powerPlaysOpponent.toString()
                penaltyKillPercentage.text = matchStatsViewModel.penaltyKillPercentage.toString() + " %"

                scoreTeam.text = matchStatsViewModel.goalsScored.toString()
                scoreOpponent.text = matchStatsViewModel.goalsConceded.toString()

                shotsTeam.text = matchStatsViewModel.shotsTeam.toString()
                shotsToBlock.text = matchStatsViewModel.shotsToBlock.toString()
                shotsOutside.text = matchStatsViewModel.shotsOutside.toString()

                shotsOpponent.text = matchStatsViewModel.shotsOpponent.toString()
                goalskeeperPercentage.text = matchStatsViewModel.goalkeeperSavesPercentage.toString() + " %"

                goalPercentage.text = matchStatsViewModel.goalShotPercentage.toString() + " %"
                shotsOutsidePercentage.text = matchStatsViewModel.shotsOutsidePercentage.toString() + " %"
                shotsToBlockPercentage.text = matchStatsViewModel.shotsToBlockPercentage.toString() + " %"
                shotsOnGoalPercentage.text = matchStatsViewModel.shotsOnGoalPercentage.toString() + " %"

                shotsPerGame.text = matchStatsViewModel.shotsPerGame.toString()
                shotsPerMinute.text = matchStatsViewModel.shotsPerMinute.toString()
                shotsBalance.text = matchStatsViewModel.shotsBalance.toString()
            }

        }
        request.join()
    }

}
