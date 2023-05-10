package com.xbot.vktest.ui.extensions

import android.content.Context
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreferencesDelegate(
    private val key: String,
    private val defaultValue: Int
) : ReadWriteProperty<Fragment, Int> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): Int {
        val sharedPref = thisRef.activity?.getPreferences(Context.MODE_PRIVATE) ?: return defaultValue
        return sharedPref.getInt(key, defaultValue)
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: Int) {
        val sharedPref = thisRef.activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(key, value)
            apply()
        }
    }
}

private const val PREFERENCE_SORT_KEY = "com.xbot.vktest.PREFERENCE_SORT_KEY"
private const val DEFAULT_VALUE = 0

@Suppress("UnusedReceiverParameter")
fun Fragment.sharedPref() = SharedPreferencesDelegate(PREFERENCE_SORT_KEY, DEFAULT_VALUE)
