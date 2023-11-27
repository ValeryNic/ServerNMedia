package ru.netology.nmedia.activity

import android.R.string.ok
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import ru.netology.nmedia.databinding.ActivityIntentHandlerBinding
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R

class IntentHandlerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityIntentHandlerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrBlank()) {
                Snackbar.make(binding.root, R.string.error_empty_content, LENGTH_INDEFINITE)
                    .setAction(ok) {
                        finish()
                    }
                    .show()
                return@let
            }else {
                Snackbar.make(binding.root, text, LENGTH_INDEFINITE)
                    .setAction(ok) {
                        finish()
                    }
                binding.contentTextView.setText(text)
                return@let
            }
        }
    }
    //Способ с нарушением принципа Solid - один метод один автор
    //override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    //    super.onActivityResult(requestCode, resultCode, data)
    //}
}