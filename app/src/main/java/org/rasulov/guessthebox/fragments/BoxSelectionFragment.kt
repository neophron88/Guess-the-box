package org.rasulov.guessthebox.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.rasulov.guessthebox.R
import org.rasulov.guessthebox.contract.HasToolBarTitle
import org.rasulov.guessthebox.contract.navigator
import org.rasulov.guessthebox.databinding.FragmentBoxSelectionBinding
import org.rasulov.guessthebox.databinding.ItemBoxBinding
import org.rasulov.guessthebox.entity.Options
import java.lang.IllegalArgumentException
import kotlin.math.max
import kotlin.properties.Delegates
import kotlin.random.Random

class BoxSelectionFragment : Fragment(),HasToolBarTitle {

    override fun getTitleRes(): Int {
        return R.string.box
    }
    private lateinit var binding: FragmentBoxSelectionBinding
    private lateinit var options: Options
    private var startTime by Delegates.notNull<Long>()
    private var rightIndex by Delegates.notNull<Int>()
    private var isTimeEnded by Delegates.notNull<Boolean>()

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = arguments?.getParcelable(KEY_OPTIONS)
            ?: throw IllegalArgumentException("BoxSelectionFragment needs the argument!")
        startTime = savedInstanceState?.getLong(START_TIME) ?: System.currentTimeMillis()
        rightIndex = savedInstanceState?.getInt(RIGHT_INDEX) ?: Random.nextInt(options.boxCount)
        isTimeEnded = savedInstanceState?.getBoolean(IS_TIME_ENDED) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoxSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemBoxList = (0 until options.boxCount).map { index ->
            ItemBoxBinding.inflate(layoutInflater, binding.root, false)
                .apply {
                    boxTitleTextView.text = getString(R.string.box_title, index + 1)
                    root.apply {
                        id = View.generateViewId()
                        tag = index
                        setOnClickListener(this@BoxSelectionFragment::onBoxSelected)
                        binding.root.addView(this)
                    }
                }
        }

        binding.flow.referencedIds = itemBoxList.map { it.root.id }.toIntArray()
        Log.d("it0088", "onViewCreated: ")
        setUpTimer()
    }


    private fun onBoxSelected(view: View) {
        if (view.tag as Int == rightIndex) {
            isTimeEnded = true // disabling timer if the user made a right choice
            navigator().showCongratulationsScreen()
        } else {
            Toast.makeText(context, R.string.empty_box, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpTimer() {
        if (options.isTimerEnabled && !isTimeEnded) {
            Log.d("it0088", "setUpTimer: ")
            timer = object : CountDownTimer(getRemainingSeconds() * 1000, 1000) {
                override fun onTick(ignore: Long) {
                    binding.timerTextView.text = getString(
                        R.string.timer_value,
                        getRemainingSeconds()
                    )
                }

                override fun onFinish() {
                    navigator().goBack()
                }

            }

            timer?.start()
            binding.timerTextView.visibility = View.VISIBLE

        } else {
            binding.timerTextView.visibility = View.GONE
        }
    }


    private fun getRemainingSeconds(): Long {
        val end = startTime + DURATION
        return max((end - System.currentTimeMillis()) / 1000, 0)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(START_TIME, startTime)
        outState.putInt(RIGHT_INDEX, rightIndex)
        outState.putBoolean(IS_TIME_ENDED, isTimeEnded)

    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
        binding.timerTextView.visibility = View.GONE
    }

    companion object {
        private const val KEY_OPTIONS = "key_options"
        private const val START_TIME = "start_time"
        private const val RIGHT_INDEX = "right_index"
        private const val IS_TIME_ENDED = "is_time_ended"
        private const val DURATION = 10_000


        fun newInstance(options: Options): BoxSelectionFragment {
            return BoxSelectionFragment()
                .apply {
                    arguments = bundleOf(KEY_OPTIONS to options)
                }
        }
    }
}