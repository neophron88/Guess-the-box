package org.rasulov.guessthebox.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.rasulov.guessthebox.R
import org.rasulov.guessthebox.contract.HasToolBarTitle
import org.rasulov.guessthebox.contract.navigator
import org.rasulov.guessthebox.databinding.FragmentBoxCongratulationsBinding

class CongratulationFragment : Fragment(),HasToolBarTitle {

    override fun getTitleRes(): Int {
        return R.string.congratulations
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View  {
        val binding = FragmentBoxCongratulationsBinding.inflate(inflater, container, false)
        binding.toMainMenuButton.setOnClickListener{
            navigator().goToMenu()
        }
        return binding.root
    }

    companion object {
        fun newInstance(): CongratulationFragment {
            return CongratulationFragment()
        }
    }
}