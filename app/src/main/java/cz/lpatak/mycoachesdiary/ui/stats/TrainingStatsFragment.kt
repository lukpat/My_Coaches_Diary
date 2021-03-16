package cz.lpatak.mycoachesdiary.ui.stats

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.FragmentMatchStatsBinding
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingStatsBinding
import cz.lpatak.mycoachesdiary.ui.stats.viewmodel.MatchStatsViewModel
import cz.lpatak.mycoachesdiary.ui.stats.viewmodel.TrainingStatsViewModel
import cz.lpatak.mycoachesdiary.util.stringDateToTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class TrainingStatsFragment : Fragment() {
    private lateinit var binding: FragmentTrainingStatsBinding
    private val trainingStatsViewModel: TrainingStatsViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_training_stats, container, false)
        with(binding) {
            lifecycleOwner = this@TrainingStatsFragment
            btn.setOnClickListener {
                trainingStatsViewModel.loadTrainingsStats(Timestamp(1610708066, 0), Timestamp(1615805666, 0))
            }
        }



        return binding.root
    }


}

