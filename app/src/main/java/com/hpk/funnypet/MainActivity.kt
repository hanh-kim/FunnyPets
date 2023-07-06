package com.hpk.funnypet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hpk.funnypet.databinding.ActivityMainBinding
import com.hpk.funnypet.views.fragments.a.PhotoListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewDataBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        viewDataBinding.lifecycleOwner = this
        transition(
            PhotoListFragment(),
            R.id.main_container,
            PhotoListFragment::class.java.name,
            false
        )
    }

    private fun transition(fragment: Fragment, id: Int, TAG: String, isAddToBackStack: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(id, fragment)
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(TAG)
        }
        fragmentTransaction.commit()
    }
}