package com.xbot.vktest.ui.extensions

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.transition.TransitionManager
import com.xbot.vktest.R
import java.lang.ref.WeakReference
import java.util.regex.Pattern

/**
 * The implementation from androidx.navigation has been slightly changed
 */
class ToolbarOnDestinationChangedListener(
    toolbar: Toolbar
) : NavController.OnDestinationChangedListener {

    private val toolbarWeakReference: WeakReference<Toolbar> = WeakReference(toolbar)
    private val arrowDrawable: DrawerArrowDrawable = DrawerArrowDrawable(toolbar.context).apply {
        progress = 1f
    }

    @Suppress("DEPRECATION")
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        val toolbar = toolbarWeakReference.get()
        if (toolbar == null) {
            controller.removeOnDestinationChangedListener(this)
            return
        }

        val label = destination.label
        if (label != null) {
            val title = StringBuffer()
            val fillInPattern = Pattern.compile("\\{(.+?)\\}")
            val matcher = fillInPattern.matcher(label)
            while (matcher.find()) {
                val argName = matcher.group(1)
                if ((arguments != null) && arguments.containsKey(argName)) {
                    matcher.appendReplacement(title, "")
                    title.append(arguments[argName].toString())
                } else {
                    throw IllegalArgumentException(
                        "Could not find \"$argName\" in $arguments to fill label \"$label\""
                    )
                }
            }
            matcher.appendTail(title)
            setTitle(title)
        }

        if (controller.backQueue.size > DEFAULT_BACKSTACK_SIZE) {
            setNavigationIcon(arrowDrawable,  R.string.nav_app_bar_navigate_up_description)
        } else {
            setNavigationIcon(null, 0)
        }
    }

    private fun setTitle(title: CharSequence?) {
        toolbarWeakReference.get()?.let { toolbar ->
            toolbar.title = title
        }
    }

    private fun setNavigationIcon(icon: Drawable?, @StringRes contentDescription: Int) {
        toolbarWeakReference.get()?.run {
            val useTransition = icon == null && navigationIcon != null
            navigationIcon = icon
            setNavigationContentDescription(contentDescription)
            if (useTransition) {
                TransitionManager.beginDelayedTransition(this)
            }
        }
    }

    private companion object {
        const val DEFAULT_BACKSTACK_SIZE = 2
    }
}

/**
 * The implementation from androidx.navigation has been slightly changed
 */
fun Toolbar.setupWithNavController(navController: NavController) {
    navController.addOnDestinationChangedListener(ToolbarOnDestinationChangedListener(this))
    this.setNavigationOnClickListener { navController.navigateUp() }
}