package org.rasulov.guessthebox.contract

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface HasToolBarCustomAction {

    fun getCustomAction(): CustomAction

}

class CustomAction(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int,
    val onCustomAction: Runnable
)