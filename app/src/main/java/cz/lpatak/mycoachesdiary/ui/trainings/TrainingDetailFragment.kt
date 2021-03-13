package cz.lpatak.mycoachesdiary.ui.trainings

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
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingDetailBinding
import cz.lpatak.mycoachesdiary.ui.trainings.util.TabsTrainingManager
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingDetailFragment : Fragment() {
    private lateinit var binding: FragmentTrainingDetailBinding
    private val args: TrainingDetailFragmentArgs by navArgs()
    private val trainingsViewModel: TrainingsViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_training_detail, container, false)

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
                        .setMessage(R.string.delete_trainig_alert)
                        .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                            trainingsViewModel.deleteTraining(args.training.id.toString())
                            findNavController().navigateUp()
                        })
                        .setNegativeButton(R.string.no, null)
                        .show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPager() {
        val adapter = TabsTrainingManager(activity?.supportFragmentManager!!, lifecycle, args)
        binding.viewPager.adapter = adapter

        val names: Array<String> = arrayOf("Informace", "Cvičení")

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = names[position]
        }.attach()
    }


}





