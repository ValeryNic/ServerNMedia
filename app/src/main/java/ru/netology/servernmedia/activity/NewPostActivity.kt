package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            //if(it.action!=Intent.ACTION_SEND){ return@let}
            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            binding.edit.requestFocus()
            if (text.isNullOrBlank()) {
                binding.ok.setOnClickListener {
                    val intent = Intent()
                    if (binding.edit.text.isNullOrBlank()) {
                        setResult(Activity.RESULT_CANCELED, intent)
                    } else {
                        val content = binding.edit.text.toString()
                        intent.putExtra(Intent.EXTRA_TEXT, content)
                        setResult(Activity.RESULT_OK, intent)
                    }
                    finish()
                }
            }else {
                binding.edit.setText(text)
                Snackbar.make(binding.root, text, BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) {
                        finish()
                    }

                return@let
            }
        }

    }
}