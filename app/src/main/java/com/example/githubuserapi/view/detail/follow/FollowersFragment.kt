package com.example.githubuserapi.view.detail.follow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.data.UserFollow
import com.example.githubuserapi.databinding.FragmentFollowerBinding
import com.example.githubuserapi.view.detail.DetailUserActivity

class FollowersFragment : Fragment() {
    private lateinit var followViewModel: FollowViewModel
    private val list = ArrayList<UserFollow>()
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        followViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowViewModel::class.java)

        arguments?.let {
            it.getString(USERNAME)?.let { user ->
                getFollowers(user)
            }
        }
    }

    private fun getFollowers(user: String) {
        followViewModel.getFollowers(user)
        followViewModel.followers.observe(this, { followers ->
            setUsers(followers)
        })
        followViewModel.isLoading.observe(this, { _loading ->
            showLoading(_loading)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvUsers.setHasFixedSize(true)
    }


    private fun setUsers(items: List<UserFollow>) {
        list.clear()
        list.addAll(items)
        binding.rvUsers.layoutManager = LinearLayoutManager(context)
        val listUserAdapter = FollowAdapter(list)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : FollowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserFollow) {
                val detailUserIntent = Intent(context, DetailUserActivity::class.java)
                detailUserIntent.putExtra(DetailUserActivity.USERNAME, data.login)
                startActivity(detailUserIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val USERNAME = "username"

        @JvmStatic
        fun newInstance(username: String) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, username)
                }
            }
    }
}