package cz.lpatak.mycoachesdiary.ui.matches

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
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchDetailInfoBinding
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchUIModel
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchesViewModel
import cz.lpatak.mycoachesdiary.util.convertLongToDate
import cz.lpatak.mycoachesdiary.util.stringDateToTimestamp
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MatchDetailFragmentInfo(private val matchFromArgs: Match) : Fragment(), DatePickerDialog.OnDateSetListener {
    private val matchesViewModel: MatchesViewModel by viewModel()
    private lateinit var binding: FragmentMatchDetailInfoBinding
    private var timestamp = Timestamp(java.util.Date(0))

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_match_detail_info,
                container,
                false
        )
        with(binding) {
            lifecycleOwner = this@MatchDetailFragmentInfo
            matchModel = MatchUIModel()
            btnSaveMatch.setOnClickListener {
                updateTeam(matchModel)
            }
            btnSetDate.setOnClickListener {
                pickDate()
            }
        }

        setMatch(binding.matchModel)

        return binding.root
    }

    private fun setMatch(uiModel: MatchUIModel?) {
        uiModel?.opponent?.value = matchFromArgs.opponent
        binding.type.setSelection(getIndex(matchFromArgs.type.toString()))
        uiModel?.dateString?.value = convertLongToDate(matchFromArgs.date!!.seconds)
        uiModel?.playingTime?.value = matchFromArgs.playingTime
        uiModel?.note?.value = matchFromArgs.note
    }

    private fun getIndex(type: String): Int {
        return when (type) {
            "Přátelák" -> 0
            "Liga" -> 1
            "Turnaj" -> 2
            else -> 0
        }
    }

    private fun updateTeam(uiModel: MatchUIModel?) {
        if (uiModel != null) {
            matchesViewModel.updateMatch(
                    Match(
                            matchFromArgs.id,
                            matchFromArgs.team,
                            uiModel.opponent.value.toString(),
                            timestamp,
                            binding.type.selectedItem.toString(),
                            uiModel.playingTime.value!!,
                            uiModel.note.value.toString(),
                            matchFromArgs.scoreTeam,
                            matchFromArgs.scoreOpponent,
                            matchFromArgs.powerPlaysTeam,
                            matchFromArgs.powerPlaysOpponent,
                            matchFromArgs.powerPlaysTeamSuccess,
                            matchFromArgs.powerPlaysOpponentSuccess,
                            matchFromArgs.shotsTeam,
                            matchFromArgs.shotsOpponent,
                            matchFromArgs.shotsToBlock,
                            matchFromArgs.shotsOutside
                    )
            )
        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val realMonth = month + 1

        val str = "$dayOfMonth.$realMonth.$year"
        binding.matchModel!!.dateString.value = str
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