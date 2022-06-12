package org.rasulov.guessthebox.contract

import androidx.annotation.StringRes

interface HasToolBarTitle {

    @StringRes
    fun getTitleRes(): Int
}