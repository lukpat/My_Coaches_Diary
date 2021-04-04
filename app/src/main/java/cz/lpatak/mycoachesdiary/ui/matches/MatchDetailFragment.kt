package cz.lpatak.mycoachesdiary.ui.matches

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Match
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchDetailBinding
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchDetailInfoBinding
import cz.lpatak.mycoachesdiary.ui.matches.util.TabsMatchManager
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchUIModel
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchesViewModel
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.StatsUIModel
import cz.lpatak.mycoachesdiary.util.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MatchDetailFragment : Fragment() {
    private lateinit var binding: FragmentMatchDetailBinding
    private lateinit var binding2: FragmentMatchDetailInfoBinding
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
        binding2 =
                DataBindingUtil.inflate(inflater, R.layout.fragment_match_detail_info, container, false)

        matchFromArgs = args.match

        setupViewPager()

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
                        })
                        .setNegativeButton(R.string.no, null)
                        .show()
            }
            R.id.save -> {
                if (binding.viewPager.currentItem == 0) {
                    saveInfoUpdates()
                } else {
                    saveStatsUpdates()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPager() {
        val adapter = TabsMatchManager(
                activity?.supportFragmentManager!!,
                lifecycle,
                args,
                matchUIModel,
                statsUIModel,
                binding2
        )
        binding.viewPager.adapter = adapter

        val names: Array<String> = arrayOf("Informace", "Statistiky")

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = names[position]
        }.attach()
    }


    private fun saveInfoUpdates() {
        if (!matchUIModel.checkInputs()) {
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
                            binding2.helperMatchesLayout.type.selectedItem.toString(),
                            matchUIModel.playingTime.value!!.toInt(),
                            matchUIModel.note.value.toString(),
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

            showToast(getString(R.string.changes_were_saved))
        }
    }

    private fun saveStatsUpdates() {
        if (!statsUIModel.checkInputs()) {
            showToast(getString(R.string.wrong_values_save_error))
        } else {
            val match = Match(
                    matchFromArgs.id,
                    matchFromArgs.team,
                    matchFromArgs.opponent,
                    matchFromArgs.date,
                    matchFromArgs.type,
                    matchFromArgs.playingTime,
                    matchFromArgs.note,
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
            matchViewModel.updateMatch(
                    match
            )
            showToast(getString(R.string.changes_were_saved))
        }
    }

}



