package cz.lpatak.mycoachesdiary.ui.myteams

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Team
import cz.lpatak.mycoachesdiary.databinding.FragmentTeamDetailBinding
import cz.lpatak.mycoachesdiary.ui.myteams.viewmodel.MyTeamsViewModel
import cz.lpatak.mycoachesdiary.ui.myteams.viewmodel.TeamUIModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class TeamDetailFragment : Fragment() {
    private val myTeamsViewModel: MyTeamsViewModel by viewModel()
    private lateinit var binding: FragmentTeamDetailBinding
    private val args: TeamDetailFragmentArgs by navArgs()
    private lateinit var teamFromArgs: Team

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_team_detail, container, false)
        with(binding) {
            lifecycleOwner = this@TeamDetailFragment
            teamModel = TeamUIModel()
            btnSaveTeam.setOnClickListener { updateTeam() }
            btnSetAsCurrent.setOnClickListener { setCurrentTeam() }
        }

        teamFromArgs = args.team

        setTeam(binding.teamModel)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                AlertDialog.Builder(context)
                        .setMessage(R.string.delete_team_alert)
                        .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                            myTeamsViewModel.deleteTeam(teamFromArgs.id.toString())
                            findNavController().navigateUp()
                        })
                        .setNegativeButton(R.string.no, null)
                        .show()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun updateTeam() {
        myTeamsViewModel.updateTeam(
                Team(
                        teamFromArgs.id,
                        teamFromArgs.owner,
                        binding.teamModel!!.name.value.toString(),
                        binding.teamModel!!.season.value.toString()
                )
        )
        findNavController().navigateUp()
    }


    private fun setTeam(uiModel: TeamUIModel?) {
        uiModel?.name?.value = teamFromArgs.name
        uiModel?.season?.value = teamFromArgs.season
    }

    private fun setCurrentTeam() {
        myTeamsViewModel.setCurrentTeam(teamFromArgs)
        findNavController().navigateUp()
    }

}