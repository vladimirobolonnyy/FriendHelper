package ru.obolonnyy.friendhelper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.obolonnyy.friendhelper.main.main.MainFragment
import timber.log.Timber

interface MenuRouter {
    fun navigateToMain()
    fun navigateToSettings()
    fun navigateToShare()
}

class MenuRouterImpl(private val supportFragmentManager: FragmentManager) : MenuRouter {

    override fun navigateToMain() {
        MainFragment::class.java.showOrCreate()
    }

    override fun navigateToSettings() {
        Timber.i("## clicked navigateToSettings")
    }

    override fun navigateToShare() {
        Timber.i("## clicked navigateToShare")
    }

    private fun Class<out Fragment>.showOrCreate() {
        supportFragmentManager.find(this)?.show() ?: newInstance().show(add = true)
    }

    private fun FragmentManager.find(fragmentClass: Class<out Fragment>): Fragment? {
        return fragments.find { fragmentClass.isInstance(it) }
    }

    private fun FragmentManager.currentVisibleFragment(): Fragment? {
        return fragments.find { it.isVisible && it.isAdded }
    }

    private fun Fragment.show(add: Boolean = false) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .apply { supportFragmentManager.currentVisibleFragment()?.let(this::hide) }
            .apply { if (add) add(R.id.container, this@show) else show(this@show) }
            .commit()
    }
}


