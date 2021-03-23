package cz.lpatak.mycoachesdiary.ui.trainings

import android.app.AlertDialog
import android.os.Bundle
import android.provider.Contacts
import android.view.*
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingDetailBinding
import cz.lpatak.mycoachesdiary.ui.trainings.util.TabsTrainingManager
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingUIModel
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import cz.lpatak.mycoachesdiary.util.convertLongToDate
import cz.lpatak.mycoachesdiary.util.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TrainingDetailFragment : Fragment() {
    private lateinit var binding: FragmentTrainingDetailBinding
    private val args: TrainingDetailFragmentArgs by navArgs()
    private val trainingsViewModel: TrainingsViewModel by viewModel()
    private val trainingUIModel: TrainingUIModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_training_detail, container, false)

        setupViewPager()
        setTraining()

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
                        .setMessage(R.string.delete_trainig_alert)
                        .setPositiveButton(R.string.yes) { _, _ ->
                            trainingsViewModel.deleteTraining(args.training.id.toString())
                            findNavController().navigateUp()
                        }
                        .setNegativeButton(R.string.no, null)
                        .show()
            }
            R.id.save -> {
                if (binding.viewPager.currentItem == 0) {
                    updateTraining()
                } else {
                    // TODO:!!!!!!
                    showToast("niciiiiiiiiiiiiiiiiiiiiiiiiiiiic")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPager() {
        val adapter = TabsTrainingManager(activity?.supportFragmentManager!!, lifecycle,trainingUIModel)
        binding.viewPager.adapter = adapter

        val names: Array<String> = arrayOf("Informace","Cvičení")

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = names[position]
        }.attach()
    }

    private fun updateTraining() {
        if (trainingUIModel.checkInputs()) {
            var date = args.training.date
            if (trainingUIModel.timestamp != Timestamp(Date(0))) {
                date = trainingUIModel.timestamp
            }

            trainingsViewModel.updateTraining(
                    Training(
                            args.training.id,
                            trainingUIModel.place.value,
                            trainingUIModel.rating.value!!.toInt(),
                            date,
                            trainingUIModel.startTime.value,
                            trainingUIModel.endTime.value,
                            trainingUIModel.players.value!!.toInt(),
                            trainingUIModel.goalkeepers.value!!.toInt()
                    )
            )
            showToast(getString(R.string.changes_were_saved))
        } else {
            showToast(getString(R.string.wrong_values_save_error))
        }
    }


    private fun setTraining() {
        val trainingFromArgs = args.training
        trainingUIModel.place.value = trainingFromArgs.place
        trainingUIModel.date.value = convertLongToDate(trainingFromArgs.date!!.seconds)
        trainingUIModel.startTime.value = trainingFromArgs.startTime
        trainingUIModel.endTime.value = trainingFromArgs.endTime
        trainingUIModel.goalkeepers.value = trainingFromArgs.goalkeepers.toString()
        trainingUIModel.players.value = trainingFromArgs.players.toString()
        trainingUIModel.rating.value = trainingFromArgs.rating.toString()
    }


}




