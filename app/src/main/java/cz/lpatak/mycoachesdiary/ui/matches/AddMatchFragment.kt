package cz.lpatak.mycoachesdiary.ui.matches

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
import cz.lpatak.mycoachesdiary.databinding.FragmentAddMatchBinding
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchUIModel
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchesViewModel
import cz.lpatak.mycoachesdiary.util.stringDateToTimestamp
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AddMatchFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private val matchesViewModel: MatchesViewModel by viewModel()
    private lateinit var binding: FragmentAddMatchBinding
    private var timestamp = Timestamp(Date(0))

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_match, container, false)
        with(binding) {
            lifecycleOwner = this@AddMatchFragment
            matchModel = MatchUIModel()
            btnSaveMatch.setOnClickListener { createMatch() }
            btnSetDate.setOnClickListener { pickDate() }
        }

        return binding.root
    }

    private fun createMatch() {
        matchesViewModel.addMatch(
                binding.matchModel!!.opponent.value.toString(),
                timestamp,
                binding.type.selectedItem.toString(),
                binding.matchModel!!.playingTime.value!!
        )
        findNavController().navigateUp()
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