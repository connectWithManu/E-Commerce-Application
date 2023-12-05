package com.manu.shopsyuser.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.manu.shopsyuser.R
import com.manu.shopsyuser.databinding.ActivityHomeMainBinding
import com.manu.shopsyuser.fragments.CartFragment
import com.manu.shopsyuser.fragments.FavoriteFragment
import com.manu.shopsyuser.fragments.HomeFragment
import com.manu.shopsyuser.fragments.NotificationFragment
import com.manu.shopsyuser.fragments.ProfileFragment

class HomeMainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val cart = intent.getBooleanExtra("cart", false)
        if (cart) {
            binding.bottomNavigation.selectedItemId = R.id.cartFragment
            replaceFrag(CartFragment())
        } else {
            replaceFrag(HomeFragment())
        }



        binding.bottomNavigation.setOnItemSelectedListener { menu ->
            when(menu.itemId) {
                R.id.homeFragment -> {
                    replaceFrag(HomeFragment())
                    true
                }
                R.id.cartFragment -> {
                    replaceFrag(CartFragment())
                    true
                }
                R.id.favoriteFragment -> {
                    replaceFrag(FavoriteFragment())
                    true
                }
                R.id.notificationFragment -> {
                    replaceFrag(NotificationFragment())
                    true
                }
                else -> {
                    replaceFrag(ProfileFragment())
                    true
                }
            }
        }
    }



    private fun replaceFrag(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}