package cz.lpatak.mycoachesdiary.ui.matches

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchDetailBinding
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchUIModel
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchesViewModel
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.StatsUIModel
import cz.lpatak.mycoachesdiary.util.convertLongToDate
import cz.lpatak.mycoachesdiary.util.hideKeyboard
import cz.lpatak.mycoachesdiary.util.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MatchDetailFragment : Fragment() {
    private lateinit var binding: FragmentMatchDetailBinding
    private val args: MatchDetailFragmentArgs by navArgs()
    private val matchViewModel: MatchesViewModel by viewModel()
    private val matchUIModel: MatchUIModel by viewModel()
    private val statsUIModel: StatsUIModel by viewModel()
    private lateinit var matchFromArgs: Match

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_match_detail, container, false)

        matchFromArgs = args.match

        setUI()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                AlertDialog.Builder(context)
                    .setMessage(R.string.delete_match_alert)
                    .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                        matchViewModel.deleteMatch(args.match.id.toString())
                        findNavController().navigateUp()
                        hideKeyboard(requireActivity())
                    })
                    .setNegativeButton(R.string.no, null)
                    .show()
            }
            R.id.save -> {
                saveUpdates()
                hideKeyboard(requireActivity())
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setUI() {
        binding.matchModel = matchUIModel
        binding.statsModel = statsUIModel

        matchUIModel.opponent.value = matchFromArgs.opponent
        binding.helperMatchesLayout.type.setSelection(getIndex(matchFromArgs.type.toString()))
        matchUIModel.dateString.value = convertLongToDate(matchFromArgs.date!!.seconds)
        matchUIModel.playingTime.value = matchFromArgs.playingTime.toString()
        matchUIModel.note.value = matchFromArgs.note
        statsUIModel.scoreTeam.value = matchFromArgs.scoreTeam.toString()
        statsUIModel.scoreOpponent.value = matchFromArgs.scoreOpponent.toString()
        statsUIModel.powerPlaysTeam.value = matchFromArgs.powerPlaysTeam.toString()
        statsUIModel.powerPlaysOpponent.value = matchFromArgs.powerPlaysOpponent.toString()
        statsUIModel.powerPlaysTeamSuccess.value = matchFromArgs.powerPlaysTeamSuccess.toString()
        statsUIModel.powerPlaysOpponentSuccess.value =
            matchFromArgs.powerPlaysOpponentSuccess.toString()
        statsUIModel.shotsTeam.value = matchFromArgs.shotsTeam.toString()
        statsUIModel.shotsOpponent.value = matchFromArgs.shotsOpponent.toString()
        statsUIModel.shotsToBlock.value = matchFromArgs.shotsToBlock.toString()
        statsUIModel.shotsOutside.value = matchFromArgs.shotsOutside.toString()
    }

    private fun getIndex(type: String): Int {
        return when (type) {
            "Přátelák" -> 0
            "Liga" -> 1
            "Turnaj" -> 2
            else -> 0
        }
    }

    private fun saveUpdates() {
        if (!matchUIModel.checkInputs() || !statsUIModel.checkInputs()) {
            showToast(getString(R.string.wrong_values_save_error))
        } else {
            var date = matchFromArgs.date
            if (matchUIModel.timestamp != Timestamp(Date(0))) {
                date = matchUIModel.timestamp
            }

            matchViewModel.updateMatch(
                Match(
                    matchFromArgs.id,
                    matchFromArgs.team,
                    matchUIModel.opponent.value.toString(),
                    date,
                    binding.helperMatchesLayout.type.selectedItem.toString(),
                    matchUIModel.playingTime.value!!.toInt(),
                    matchUIModel.note.value.toString(),
                    statsUIModel.scoreTeam.value!!.toInt(),
                    statsUIModel.scoreOpponent.value!!.toInt(),
                    statsUIModel.powerPlaysTeam.value!!.toInt(),
                    statsUIModel.powerPlaysOpponent.value!!.toInt(),
                    statsUIModel.powerPlaysTeamSuccess.value!!.toInt(),
                    statsUIModel.powerPlaysOpponentSuccess.value!!.toInt(),
                    statsUIModel.shotsTeam.value!!.toInt(),
                    statsUIModel.shotsOpponent.value!!.toInt(),
                    statsUIModel.shotsToBlock.value!!.toInt(),
                    statsUIModel.shotsOutside.value!!.toInt()
                )
            )

            showToast(getString(R.string.changes_were_saved))
        }
    }

}



