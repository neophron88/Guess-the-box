package org.rasulov.guessthebox.fragments

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.rasulov.guessthebox.entity.Options

class OptionsFragment : Fragment() {

    companion object {
        private const val KEY_OPTIONS = "key_options"

        fun newInstance(options: Options): OptionsFragment {
            return OptionsFragment()
                .apply {
                    arguments = bundleOf(KEY_OPTIONS to options)
                }
        }
    }
}