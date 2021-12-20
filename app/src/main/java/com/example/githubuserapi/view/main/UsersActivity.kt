package com.example.githubuserapi.view.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.R
import com.example.githubuserapi.data.User
import com.example.githubuserapi.databinding.ActivityUsersBinding
import com.example.githubuserapi.utils.SettingPreferences
import com.example.githubuserapi.utils.ViewModelFactory
import com.example.githubuserapi.view.detail.DetailUserActivity
import com.example.githubuserapi.view.favorite.FavoriteActivity
import com.example.githubuserapi.view.setting.SettingsActivity
import com.example.githubuserapi.view.setting.SettingsViewModel

class UsersActivity : AppCompatActivity() {
    private lateinit var usersViewModel: UsersViewModel
    private val list = ArrayList<User>()
    private var _binding: ActivityUsersBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUsers.setHasFixedSize(true)

        usersViewModel = ViewModelProvider(
            this@UsersActivity,
            ViewModelProvider.NewInstanceFactory()
        )[UsersViewModel::class.java]
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    usersViewModel.findUsers(query)
                    usersViewModel.users.observe(this@UsersActivity, { users ->
                        setUsers(users)
                    })
                    usersViewModel.isLoading.observe(this@UsersActivity, {
                        showLoading(it)
                    })
                } else {
                    list.clear()
                    binding.rvUsers.adapter?.notifyDataSetChanged()
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }

            R.id.favorite -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUsers(items: List<User>) {
        list.clear()
        list.addAll(items)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = UsersAdapter(list)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val detailUserIntent = Intent(this@UsersActivity, DetailUserActivity::class.java)
                detailUserIntent.putExtra(DetailUserActivity.USERNAME, data.login)
                startActivity(detailUserIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}