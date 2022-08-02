package org.rasulov.guessthebox

import androidx.navigation.NavController


fun NavController.isOnStartDestination():Boolean {
    return this.currentDestination?.id == this.graph.startDestinationId
}