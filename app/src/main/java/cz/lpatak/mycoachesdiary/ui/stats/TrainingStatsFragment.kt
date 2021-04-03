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
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingStatsBinding
import cz.lpatak.mycoachesdiary.ui.stats.viewmodel.TrainingStatsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingStatsFragment : Fragment() {
    private lateinit var binding: FragmentTrainingStatsBinding
    private val viewModel: TrainingStatsViewModel by viewModel()
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

        setUI()


        return binding.root
    }


    private fun generateCenterText(): SpannableString {
        val s = SpannableString(getString(R.string.training_stats_graph_title))
        s.setSpan(RelativeSizeSpan(1.5f), 0, s.length, 0)
        return s
    }

    private fun setUI() {
        viewModel.loadTrainingsStats(args.dateFrom, args.dateTo)

        val ds1 = PieDataSet(viewModel.getEntries(), "")
        ds1.setColors(*ColorTemplate.PASTEL_COLORS)
        ds1.sliceSpace = 2f
        ds1.valueTextColor = Color.WHITE
        ds1.valueTextSize = 12f

        val dataSet = PieData(ds1)
        binding.chart.data = dataSet

    }
}
