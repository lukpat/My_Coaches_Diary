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
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchDetailBinding
import cz.lpatak.mycoachesdiary.ui.matches.util.TabsMatchManager
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchDetailFragment : Fragment() {
    private lateinit var binding: FragmentMatchDetailBinding
    private val args: MatchDetailFragmentArgs by navArgs()
    private val matchViewModel: MatchesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_match_detail, container, false)

        setupViewPager()

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
                    .setMessage(R.string.delete_match_alert)
                    .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                        matchViewModel.deleteMatch(args.match.id.toString())
                        findNavController().navigateUp()
                    })
                    .setNegativeButton(R.string.no, null)
                    .show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPager() {
        val adapter = TabsMatchManager(activity?.supportFragmentManager!!, lifecycle, args)
        binding.viewPager.adapter = adapter

        val names: Array<String> = arrayOf("Informace", "Statistiky")

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = names[position]
        }.attach()
    }

}





