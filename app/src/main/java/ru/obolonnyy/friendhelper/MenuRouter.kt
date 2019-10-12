package ru.obolonnyy.friendhelper

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.obolonnyy.friendhelper.main.MainFragment
import ru.obolonnyy.friendhelper.settings.SettingsFragment
import ru.obolonnyy.friendhelper.settings.ShareFragment

interface MenuRouter {
    fun navigateToMain()
    fun navigateToSettings()
    fun navigateToShare()
}

class MenuRouterImpl(
    private val fm: FragmentManager,
    @IdRes private val container: Int
) : MenuRouter {

    override fun navigateToMain() {
        MainFragment::class.java.showOrCreate()
    }

    override fun navigateToSettings() {
        SettingsFragment::class.java.showOrCreate()
    }

    override fun navigateToShare() {
        ShareFragment::class.java.showOrCreate()
    }

    private fun Class<out Fragment>.showOrCreate() {
        fm.find(this)?.show(add = false) ?: newInstance().show(add = true)
    }

    private fun FragmentManager.find(fragmentClass: Class<out Fragment>): Fragment? {
        return fragments.find { fragmentClass.isInstance(it) }
    }

    private fun FragmentManager.currentVisibleFragment(): Fragment? {
        return fragments.find { it.isVisible && it.isAdded }
    }

    private fun Fragment.show(add: Boolean) {
        fm.beginTransaction()
            .apply { fm.currentVisibleFragment()?.let(this::hide) }
            .apply { if (add) add(container, this@show) else show(this@show) }
            .commit()
    }
}


