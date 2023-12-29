package ru.netology.servernmedia.activity

import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import okhttp3.*
import ru.netology.servernmedia.databinding.ActivityAppBinding
import java.io.IOException
import java.util.concurrent.TimeUnit
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import androidx.navigation.findNavController
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.internal.notify
import ru.netology.servernmedia.R
import ru.netology.servernmedia.activity.NewPostFragment.Companion.textArg

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    private val urls = listOf("netology.jpg", "sber.jpg", "tcs.jpg", "404.png")
    private var index = 0
    private val worker = WorkerThread().apply {
        start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val handler = Handler(Looper.getMainLooper())
//        worker.callback = {
//            handler.post {
//                binding.image.setImageBitmap(it)
//            }
//        }

        binding.load.setOnClickListener {
            if (index == urls.size) {
                index = 0
            }

            val url = "http://10.0.2.2:9999/avatars/${urls[index++]}"
            Glide.with(binding.image)
                .load(url)
                .placeholder(R.drawable.ic_loading_100dp)
                .apply(RequestOptions().circleCrop())
                .error(R.drawable.ic_error_100dp)
                .timeout(10_000)
                .into(binding.image)

//            Picasso.get()
//                .load(url)
//                .error(R.drawable.ic_error_100dp)
//                .into(binding.image);
        }
    }
}
class WorkerThread: Thread(){
    private lateinit var handler: Handler
    var callback:(Bitmap) -> Unit = {}
    private val okHttpClient=OkHttpClient.Builder()
        .connectTimeout(30,TimeUnit.SECONDS)
        .build()
    fun download (url:String){
        val message = handler.obtainMessage()
        message.obj = url
        handler.sendMessage(message)
    }
    override fun run(){
        Looper.prepare()
        handler=Handler(Looper.myLooper()!!){message:Message ->
            val request = Request.Builder()
                .url(message.obj as String)
                .build()

            val result = okHttpClient.newCall(request)
                .execute()
            println(result.body?.string()?.length)
            callback(BitmapFactory.decodeStream(result.body?.byteStream()))

            true
        }
        Looper.loop()
    }

}
//fun myDouble(){//остатки прошлой программы
//        requestNotificationsPermission()
//
//        intent?.let {
//            if (it.action != Intent.ACTION_SEND) {
//                return@let
//            }
//
//            val text = it.getStringExtra(Intent.EXTRA_TEXT)
//            if (text?.isNotBlank() != true) {
//                return@let
//            }
//
//            intent.removeExtra(Intent.EXTRA_TEXT)
//            findNavController(R.id.nav_host_fragment)
//                .navigate(
//                    R.id.action_feedFragment_to_newPostFragment,
//                    Bundle().apply {
//                        textArg = text
//                    }
//                )
//        }
//
//        checkGoogleApiAvailability()
//    }
//
//    private fun requestNotificationsPermission() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
//            return
//        }
//
//        val permission = Manifest.permission.POST_NOTIFICATIONS
//
//        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
//            return
//        }
//
//        requestPermissions(arrayOf(permission), 1)
//    }
//
//    private fun checkGoogleApiAvailability() {
//        with(GoogleApiAvailability.getInstance()) {
//            val code = isGooglePlayServicesAvailable(this@AppActivity)
//            if (code == ConnectionResult.SUCCESS) {
//                return@with
//            }
//            if (isUserResolvableError(code)) {
//                getErrorDialog(this@AppActivity, code, 9000)?.show()
//                return
//            }
//            Toast.makeText(this@AppActivity, R.string.google_play_unavailable, Toast.LENGTH_LONG)
//                .show()
//        }
//
//        FirebaseMessaging.getInstance().token.addOnSuccessListener {
//            println(it)
//        }
//    }
//}