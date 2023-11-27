package ru.netology.nmedia.presentation.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.launch
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.ViewModel.PostViewModel
import ru.netology.nmedia.activity.NewPostResultContract
import ru.netology.nmedia.adapter.OnInterationListener
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.AndroidUtils.focusAndShowKeyboard


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewModel: PostViewModel by viewModels()
        val EditPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContentAndSave(result)
        }
        val adapter= PostAdapter(object: OnInterationListener {
            override fun like(post: Post) {
                viewModel.likeById(post.id)
            }

            //override fun share(post: Post) {
            //    viewModel.shareById(post.id)
            //}
            override fun remove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun video(post: Post) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoURL.toString()))
                    startActivity(intent)

            }

            override fun edit(post: Post) {
                viewModel.edit(post)
                EditPostLauncher.launch(post.content)

            }
            override fun share(post: Post){
                viewModel.shareById(post.id)
                val intent=Intent().apply{
                    action=Intent.ACTION_SEND//можно выбирать из choose
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type="text/plain"
                    
                }
                //startActivity(intent) - куцый вариант
                //этот наполненней -
                val shareIntent=Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }
        }


        )
        binding.list.adapter=adapter
        viewModel.data.observe(this){
            posts ->
            val newPost=posts.size > adapter.currentList.size
            adapter.submitList(posts){//по завершении потока - прокрутка
                if(newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }



        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContentAndSave(result)
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch(null)
        }

    }




    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

