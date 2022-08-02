package org.rasulov.guessthebox

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import org.rasulov.guessthebox.contract.*
import org.rasulov.guessthebox.databinding.ActivityMainBinding
import org.rasulov.guessthebox.entity.Options
import org.rasulov.guessthebox.fragments.*

class MainActivity : AppCompatActivity(), Navigator {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment)
            .navController
    }
    private var currentFragment: Fragment? = null

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)

            if (f !is NavHostFragment) {
                currentFragment = f
                updateUi()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportFragmentManager.registerFragmentLifecycleCallbacks(
            fragmentListener,
            true
        )

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        updateUi()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    //    override fun onSupportNavigateUp() = navController.navigateUp() || super.onSupportNavigateUp()


    override fun showBoxSelectionScreen(options: Options) {
        launchDestination(
            R.id.action_menuFragment_to_boxSelectionFragment,
            BoxSelectionFragment.createArgs(options)
        )
    }


    override fun showOptionsScreen(options: Options) {
        launchDestination(
            R.id.action_menuFragment_to_optionsFragment,
            OptionsFragment.createArgs(options)
        )
    }

    override fun showAboutScreen() {
        launchDestination(R.id.action_menuFragment_to_aboutFragment)

    }

    override fun showCongratulationsScreen() {
        launchDestination(R.id.action_boxSelectionFragment_to_congratulationFragment)

    }

    override fun goBack() {
        onBackPressed()
    }

    override fun goToMenu() {
        navController.popBackStack(R.id.menuFragment, false)
    }

    private fun launchDestination(destinationId: Int, args: Bundle? = null) {
        navController.navigate(
            destinationId,
            args,

            navOptions {
                this.anim {
                    enter = R.anim.slide_in
                    exit = R.anim.fade_out
                    popEnter = R.anim.fade_in
                    popExit = R.anim.slide_out
                }
            })
    }

    override fun <T : Parcelable> publishResult(result: T) {
        supportFragmentManager.setFragmentResult(
            result.javaClass.name,
            bundleOf(KEY_RESULT to result)
        )

    }

    override fun <T : Parcelable> listenResult(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    ) {
        supportFragmentManager.setFragmentResultListener(clazz.name, owner) { _, bundle ->
            listener.invoke(bundle.getParcelable(KEY_RESULT)!!)
        }
    }


    private fun updateUi() {
        val fragment = currentFragment

        if (navController.isOnStartDestination()) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        if (fragment is HasToolBarTitle) {
            binding.toolbar.title = getString(fragment.getTitleRes())
        } else {
            binding.toolbar.title = getString(R.string.app_name)
        }

        if (fragment is HasToolBarCustomAction) {
            createCustomToolbarAction(fragment.getCustomAction())
        } else {
            binding.toolbar.menu.clear()
        }
    }

    private fun createCustomToolbarAction(action: CustomAction) {
        val icon = ContextCompat.getDrawable(this, action.iconRes)!!
        icon.setTint(Color.WHITE)

        val menuItem = binding.toolbar.menu.add(action.textRes)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = icon
        menuItem.setOnMenuItemClickListener {
            action.onCustomAction.run()
            return@setOnMenuItemClickListener true
        }

    }

    companion object {
        @JvmStatic
        private val KEY_RESULT = "RESULT"
    }
}