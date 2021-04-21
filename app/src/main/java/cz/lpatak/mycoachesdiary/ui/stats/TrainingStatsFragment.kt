package cz.lpatak.mycoachesdiary.ui.stats

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingStatsBinding
import cz.lpatak.mycoachesdiary.ui.stats.viewmodel.TrainingStatsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingStatsFragment : Fragment() {
    private lateinit var binding: FragmentTrainingStatsBinding
    private val trainingStatsViewModel: TrainingStatsViewModel by viewModel()
    private val args: TrainingStatsFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_training_stats, container, false)

        with(binding) {
            chart.description.isEnabled = false
            chart.legend.isEnabled = false
            chart.centerText = generateCenterText()
            chart.holeRadius = 45f
            chart.transparentCircleRadius = 50f
        }

        loadTrainings(args.dateFrom, args.dateTo)

        return binding.root
    }

    private fun loadTrainings(
            dateFrom: Timestamp,
            dateTo: Timestamp
    ) {
        val trainingsList: MutableList<Training> = mutableListOf()

        trainingStatsViewModel.loadTrainings(dateFrom, dateTo)
                .observe(viewLifecycleOwner, { result ->
                    if (result is Result.Success) {
                        trainingsList.addAll(result.data)
                        setUITraining(trainingsList)
                        loadExercises(trainingsList)
                    }
                })
    }

    private fun loadExercises(trainingList: MutableList<Training>) {
        val exerciseList: MutableList<ExerciseInTraining> = mutableListOf()

        for (training in trainingList) {
            trainingStatsViewModel.loadExercises(training.id.toString()).observe(
                    viewLifecycleOwner, { result ->
                if (result is Result.Success) {
                    exerciseList.addAll(result.data)
                    setUI(exerciseList)
                }
            })
        }
    }

    private fun generateCenterText(): SpannableString {
        val s = SpannableString(getString(R.string.training_stats_graph_title))
        s.setSpan(RelativeSizeSpan(1.5f), 0, s.length, 0)
        return s
    }

    private fun setUITraining(trainingList: MutableList<Training>) {
        if (trainingList.size == 0) {
            binding.moreThanZero = false
            binding.noTrainings.text = getString(R.string.no_data_found)
        } else {
            binding.moreThanZero = true
            trainingStatsViewModel.setTrainingStats(trainingList, binding)
        }
    }

    private fun setUI(exerciseList: MutableList<ExerciseInTraining>) {
        val ds1 = PieDataSet(trainingStatsViewModel.getEntries(exerciseList), "")
        ds1.setColors(*ColorTemplate.PASTEL_COLORS)
        ds1.sliceSpace = 2f
        ds1.valueTextColor = Color.WHITE
        ds1.valueTextSize = 12f

        val dataSet = PieData(ds1)
        binding.chart.data = dataSet
    }
}
