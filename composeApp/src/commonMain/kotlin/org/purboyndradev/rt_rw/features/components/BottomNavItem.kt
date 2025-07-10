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
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", FeatherIcons.Home)
    object Profile : BottomNavItem("profile", "Profile", FeatherIcons.User)
    object News : BottomNavItem("news", "News", FeatherIcons.Book)
    object Activity : BottomNavItem("activity", "Activity", FeatherIcons.Activity)

    companion object {
        val items = listOf(Home, Profile, News, Activity)
    }
}
