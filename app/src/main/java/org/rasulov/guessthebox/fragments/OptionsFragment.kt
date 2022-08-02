package org.rasulov.guessthebox.fragments

import android.os.Bundle
import android.view.KeyCharacterMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.rasulov.guessthebox.R
import org.rasulov.guessthebox.contract.CustomAction
import org.rasulov.guessthebox.contract.HasToolBarCustomAction
import org.rasulov.guessthebox.contract.HasToolBarTitle
import org.rasulov.guessthebox.contract.navigator
import org.rasulov.guessthebox.databinding.FragmentOptionsBinding
import org.rasulov.guessthebox.entity.Options
import java.lang.IllegalArgumentException

class OptionsFragment : Fragment(), HasToolBarCustomAction, HasToolBarTitle {

    private lateinit var binding: FragmentOptionsBinding
    private lateinit var options: Options

    private lateinit var boxCountItems: List<BoxCountItem>
    private lateinit var adapter: ArrayAdapter<BoxCountItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = savedInstanceState?.getParcelable(KEY_OPTIONS)
            ?: arguments?.getParcelable(KEY_OPTIONS)
                    ?: throw IllegalArgumentException("Fragments needs to be open with arguments!")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            enableTimerCheckBox.setOnCheckedChangeListener { _, isChecked ->
                options = options.copy(isTimerEnabled = isChecked)
            }
            setupSpinner()
            cancelButton.setOnClickListener { navigator().goBack() }
            confirmButton.setOnClickListener { confirm() }
        }
    }

    private fun setupSpinner() {
        boxCountItems =
            (1..6).map { BoxCountItem(it, resources.getQuantityString(R.plurals.boxes, it, it))}
        adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            boxCountItems
        )

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)


        binding.boxCountSpinner.adapter = adapter
        binding.boxCountSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val count = boxCountItems[position].count
                    options = options.copy(boxCount = count)
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    override fun onStart() {
        super.onStart()
        updateUi()
    }

    private fun updateUi() {
        binding.apply {
            enableTimerCheckBox.isChecked = options.isTimerEnabled

            val currentIndex = boxCountItems.indexOfFirst { it.count == options.boxCount }
            boxCountSpinner.setSelection(currentIndex)
        }

    }

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            R.drawable.ic_done,
            R.string.done
        ) {
            confirm()
        }
    }

    private fun confirm() {
        navigator().publishResult(options)
        navigator().goBack()
    }

    override fun getTitleRes() = R.string.options


    companion object {
        private const val KEY_OPTIONS = "key_options"

        fun createArgs(options: Options) = bundleOf(KEY_OPTIONS to options)
    }


    class BoxCountItem(
        val count: Int,
        private val optionTitle: String
    ) {
        override fun toString(): String {
            return optionTitle
        }
    }


}