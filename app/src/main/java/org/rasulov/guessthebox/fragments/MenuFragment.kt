package org.rasulov.guessthebox.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.rasulov.guessthebox.contract.navigator
import org.rasulov.guessthebox.entity.Options
import org.rasulov.guessthebox.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private lateinit var options: Options
    private lateinit var binding: FragmentMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = savedInstanceState?.getParcelable(KEY_OPTIONS) ?: Options.DEFAULT

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            openBoxButton.setOnClickListener { openBoxSelectionFragment() }
            optionsButton.setOnClickListener { openOptionsFragment() }
            aboutButton.setOnClickListener { openAboutFragment() }
            exitButton.setOnClickListener { exit() }
        }
        listenResult()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    private fun openBoxSelectionFragment() {
        navigator().showBoxSelectionScreen(options)
    }

    private fun openOptionsFragment() {
        navigator().showOptionsScreen(options)
    }

    private fun openAboutFragment() {
        navigator().showAboutScreen()
    }

    private fun exit() {
        navigator().goBack()
    }

    private fun listenResult() {
        navigator().listenResult(Options::class.java, viewLifecycleOwner) {
            options = it
        }

    }

    companion object {
        private const val KEY_OPTIONS = "key_options"
        fun newInstance(): MenuFragment {
            return MenuFragment()
        }
    }


}