package org.rasulov.guessthebox.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.about_fragment.view.*
import org.rasulov.guessthebox.R
import org.rasulov.guessthebox.contract.HasToolBarTitle
import org.rasulov.guessthebox.contract.navigator
import org.rasulov.guessthebox.databinding.AboutFragmentBinding

class AboutFragment : Fragment(),HasToolBarTitle {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = AboutFragmentBinding.inflate(layoutInflater, container, false)
        binding.appNameTextView.text = getString(R.string.app_name)
        binding.versionNameTextView.text = "1.0"
        binding.versionCodeTextView.text = "1"
        binding.root.okButton.setOnClickListener{navigator().goBack()}
        return binding.root
    }

    companion object {
        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }

    override fun getTitleRes(): Int {
        return R.string.about
    }
}