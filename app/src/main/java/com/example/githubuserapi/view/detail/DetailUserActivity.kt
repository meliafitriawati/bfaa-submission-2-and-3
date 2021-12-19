package com.example.githubuserapi.view.detail

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.ImageViewCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapi.R
import com.example.githubuserapi.data.UserResponse
import com.example.githubuserapi.databinding.ActivityDetailUserBinding
import com.example.githubuserapi.local.Favorite
import com.example.githubuserapi.utils.SettingPreferences
import com.example.githubuserapi.utils.ViewModelFactory
import com.example.githubuserapi.view.setting.SettingsActivity
import com.example.githubuserapi.view.setting.SettingsViewModel
import com.example.githubuserapi.viewmodel.ViewModelFactoryDB
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailUserActivity : AppCompatActivity() {
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var username: String
    private lateinit var fav: Favorite

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = resources.getString(R.string.detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        detailUserViewModel = obtainViewModel(this@DetailUserActivity)

        intent.getStringExtra(USERNAME)?.let {
            username = it
            detailUserViewModel.getUser(username)
            detailUserViewModel.user.observe(this@DetailUserActivity, { user ->
                setUser(user)
            })
            detailUserViewModel.isLoading.observe(this@DetailUserActivity, { _loading ->
                showLoading(_loading)
            })
        }

        val sectionsPagerAdapter = DetailUserPagerAdapter(this, username)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = ViewModelFactoryDB.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }


    private fun setUser(resp: UserResponse) {
        binding.apply {
            tvName.text = resp.name
            tvUsername.text = "@${resp.login}"
            tvFollowing.text = resp.following.toString()
            tvFollowers.text = resp.followers.toString()
            tvRepository.text = resp.publicRepos.toString()
            tvLocation.text = resp.location
            tvCompany.text = resp.company
            Glide.with(binding.imgAvatar.context)
                .load(resp.avatarUrl)
                .centerCrop()
                .into(imgAvatar)
        }

        detailUserViewModel.getFave(resp.id).observe(this, {
            if (it) {
                ImageViewCompat.setImageTintList(
                    binding.addFav,
                    ColorStateList.valueOf(resources.getColor(R.color.pink_400))
                )

                binding.addFav.setOnClickListener {
                    fav = Favorite(resp.id, resp.login, resp.avatarUrl)
                    detailUserViewModel.delete(fav)
                    showToast(getString(R.string.deleted))
                    finish()
                }

            } else {
                ImageViewCompat.setImageTintList(
                    binding.addFav,
                    ColorStateList.valueOf(Color.WHITE)
                )

                binding.addFav.setOnClickListener {
                    fav = Favorite(resp.id, resp.login, resp.avatarUrl)
                    detailUserViewModel.insert(fav)
                    showToast(getString(R.string.added))
                    finish()
                }
            }
        })
    }

    private fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.text_followers,
            R.string.text_following
        )
    }
}