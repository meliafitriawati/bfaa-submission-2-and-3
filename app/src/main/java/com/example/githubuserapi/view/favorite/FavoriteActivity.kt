package com.example.githubuserapi.view.favorite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.R
import com.example.githubuserapi.databinding.ActivityFavoriteBinding
import com.example.githubuserapi.local.Favorite
import com.example.githubuserapi.view.detail.DetailUserActivity
import com.example.githubuserapi.view.setting.SettingsActivity
import com.example.githubuserapi.viewmodel.ViewModelFactoryDB

class FavoriteActivity : AppCompatActivity() {
    private lateinit var favesViewModel: FavoriteViewModel
    private val list = ArrayList<Favorite>()
    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvFaves.setHasFixedSize(true)

        title = resources.getString(R.string.text_favorite)

        favesViewModel = obtainViewModel(this@FavoriteActivity)

        getList()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun getList() {
        favesViewModel.getAllFavorite().observe(this, { dataList ->
            if (dataList != null) {
                list.clear()
                list.addAll(dataList)
                binding.rvFaves.layoutManager = LinearLayoutManager(this)
                val listUserAdapter = FavoriteAdapter(list)
                binding.rvFaves.adapter = listUserAdapter

                listUserAdapter.setOnItemClickCallback(object :
                    FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Favorite) {
                        val detailUserIntent =
                            Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                        detailUserIntent.putExtra(DetailUserActivity.USERNAME, data.login)
                        startActivity(detailUserIntent)
                    }

                    override fun onDeleteItemClicked(data: Favorite) {
                        favesViewModel.delete(data)
                        showToast(getString(R.string.deleted))
                        getList()
                    }
                })
            }
        })
    }

    private fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactoryDB.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
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
}