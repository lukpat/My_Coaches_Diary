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
import cz.lpatak.mycoachesdiary.util.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel


class TeamDetailFragment : Fragment() {
    private val myTeamsViewModel: MyTeamsViewModel by viewModel()
    private val teamUIModel: TeamUIModel by viewModel()
    private val args: TeamDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentTeamDetailBinding
    private lateinit var teamFromArgs: Team

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        teamFromArgs = args.team

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_team_detail, container, false)
        with(binding) {
            lifecycleOwner = this@TeamDetailFragment
            teamModel = teamUIModel
            isCurrentTeam = !myTeamsViewModel.isTeamCurrentTeam(teamFromArgs.id.toString())
            btnSetAsCurrent.setOnClickListener { setCurrentTeam() }
        }

        setTeam()

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
                    .setMessage(R.string.delete_team_alert)
                    .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                        myTeamsViewModel.deleteTeam(teamFromArgs.id.toString())
                        findNavController().navigateUp()
                    })
                    .setNegativeButton(R.string.no, null)
                    .show()
            }
            R.id.save -> {
                updateTeam()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updateTeam() {
        if (teamUIModel.checkInputs()) {
            myTeamsViewModel.updateTeam(
                Team(
                    teamFromArgs.id,
                    teamFromArgs.owner,
                    teamUIModel.name.value.toString(),
                    teamUIModel.season.value.toString()
                )
            )
            showToast(R.string.changes_were_saved)
            findNavController().navigateUp()
        } else {
            showToast(getString(R.string.wrong_values_save_error))
        }
    }


    private fun setTeam() {
        teamUIModel.name.value = teamFromArgs.name
        teamUIModel.season.value = teamFromArgs.season
    }

    private fun setCurrentTeam() {
        myTeamsViewModel.setCurrentTeam(teamFromArgs)
        findNavController().navigateUp()
    }

}