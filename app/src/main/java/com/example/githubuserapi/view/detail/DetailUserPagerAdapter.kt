package com.example.githubuserapi.view.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.example.githubuserapi.view.detail.follow.FollowersFragment
import com.example.githubuserapi.view.detail.follow.FollowingFragment


class DetailUserPagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment.newInstance(username)
            1 -> fragment = FollowingFragment.newInstance(username)
        }
        return fragment as Fragment
    }

}