package org.purboyndradev.rt_rw.features.components

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.Activity
import compose.icons.feathericons.Book
import compose.icons.feathericons.Home
import compose.icons.feathericons.User

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val id: Int,
) {
    object Home : BottomNavItem("home", "Home", FeatherIcons.Home, 1)
    object News : BottomNavItem("news", "News", FeatherIcons.Book, 2)
    object Activity :
        BottomNavItem("activity", "Activity", FeatherIcons.Activity, 3)

    object Profile : BottomNavItem("profile", "Profile", FeatherIcons.User, 4)

    companion object {
        val items = listOf(Home, News, Activity, Profile)
    }
}
