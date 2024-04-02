package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }
        })
        binding.list.adapter = adapter
        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading) { viewModel.loadPosts() }
                    .show()
            }
        }
        viewModel.data.observe(viewLifecycleOwner) { state ->
            val t1 = state.size
            val t2 = adapter.currentList.size
            println("$t1")
            println("$t2")
            var newPost = t1>t2 && adapter.itemCount > 0
            //submitList - асинхронный метод
            if(newPost) {
                val count = viewModel.newerCount
                println("new posts = $count")
                binding.newList.isVisible = true
                binding.newList.text = "new posts = $count"


            }
            //binding.emptyText.isVisible = FeedModel.
        }



//        viewModel.newerCount.observe(viewLifecycleOwner){
//            binding.newList.text = "new posts = $it"
//            binding.newList.isVisible = true
//            println(it)
//        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.refreshPosts()
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.newList.setOnClickListener{
            viewModel.data.observe(viewLifecycleOwner) { state ->

                adapter.submitList(state.toDto())
            }
            binding.list.smoothScrollToPosition(0)
            binding.newList.isVisible = false
        }

        return binding.root
    }
}
