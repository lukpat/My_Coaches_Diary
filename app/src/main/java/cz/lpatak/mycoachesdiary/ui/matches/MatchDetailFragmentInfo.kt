package cz.lpatak.mycoachesdiary.ui.matches

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchDetailInfoBinding
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchUIModel
import cz.lpatak.mycoachesdiary.util.convertLongToDate
import cz.lpatak.mycoachesdiary.util.stringDateToTimestamp
import java.util.*


class MatchDetailFragmentInfo(
        private val matchFromArgs: Match,
        private val matchUIModel: MatchUIModel,
        private val binding: FragmentMatchDetailInfoBinding
) : Fragment(),
        DatePickerDialog.OnDateSetListener {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        with(binding) {
            lifecycleOwner = this@MatchDetailFragmentInfo
            matchModel = matchUIModel
            helperMatchesLayout.date.setOnClickListener { pickDate() }
        }

        setMatch()

        return binding.root
    }

    private fun setMatch() {
        matchUIModel.opponent.value = matchFromArgs.opponent
        binding.helperMatchesLayout.type.setSelection(getIndex(matchFromArgs.type.toString()))
        matchUIModel.dateString.value = convertLongToDate(matchFromArgs.date!!.seconds)
        matchUIModel.playingTime.value = matchFromArgs.playingTime.toString()
        matchUIModel.note.value = matchFromArgs.note
    }

    private fun getIndex(type: String): Int {
        return when (type) {
            "Přátelák" -> 0
            "Liga" -> 1
            "Turnaj" -> 2
            else -> 0
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val realMonth = month + 1

        val str = "$dayOfMonth.$realMonth.$year"
        matchUIModel.dateString.value = str
        matchUIModel.timestamp = stringDateToTimestamp(str)
    }

    private fun pickDate() {
        val cal = Calendar.getInstance()
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)

        DatePickerDialog(this.requireContext(), this, year, month, dayOfMonth).show()
    }
}