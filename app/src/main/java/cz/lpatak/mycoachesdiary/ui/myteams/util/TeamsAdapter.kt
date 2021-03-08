package cz.lpatak.mycoachesdiary.ui.myteams.util

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Team
import cz.lpatak.mycoachesdiary.databinding.TeamItemBinding
import cz.lpatak.mycoachesdiary.ui.base.DataBoundListAdapter
import cz.lpatak.mycoachesdiary.ui.myteams.HomeFragmentDirections
import cz.lpatak.mycoachesdiary.ui.myteams.viewmodel.MyTeamsViewModel

class TeamsAdapter(
        private val onClick: ((Team) -> Unit)? = null
) : DataBoundListAdapter<Team, TeamItemBinding>(
        diffCallback = object : DiffUtil.ItemCallback<Team>() {
            override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
                return oldItem == newItem
            }
        }
) {
    private lateinit var viewModel: MyTeamsViewModel

    fun setViewModel(viewModel: MyTeamsViewModel) {
        this.viewModel = viewModel
    }

    override fun createBinding(parent: ViewGroup): TeamItemBinding {
        return DataBindingUtil.inflate<TeamItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.team_item,
                parent, false
        ).apply {
            this.root.setOnClickListener {
                this.team?.let { goToTeamDetail(this.root, it) }
            }
            this.buttonDelete.setOnClickListener {
                this.team?.let { deleteTeam(this.root, it) }
            }
        }
    }


    override fun bind(binding: TeamItemBinding, item: Team) {
        binding.team = item
    }

    private fun goToTeamDetail(view: View, team: Team) {
        val directions = HomeFragmentDirections.actionNavigationHomeToNavigationTeamDetail(team)
        view.findNavController().navigate(directions)
    }

    private fun deleteTeam(view: View, team: Team) {
        val directions = HomeFragmentDirections.actionNavigationHomeSelf()

        AlertDialog.Builder(view.context)
                .setTitle(R.string.delete_team_title)
                .setMessage(R.string.delete_team_alert)
                .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                    viewModel.deleteTeam(team.id.toString())
                    view.findNavController().navigate(directions)
                })
                .setNegativeButton(R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }
}
