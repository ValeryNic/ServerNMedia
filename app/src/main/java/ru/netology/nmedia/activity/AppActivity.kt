package ru.netology.nmedia.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle.State.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.viewmodel.AuthViewModel
import javax.inject.Inject
import androidx.lifecycle.repeatOnLifecycle as lifecycleRepeatOnLifecycle
import ru.netology.nmedia.R


@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.activity_app) {
    private val viewModel: AuthViewModel by viewModels ()
    @Inject
    lateinit var appAuth: AppAuth

    @Inject
    lateinit var firebaseInstallations: FirebaseInstallations

    @Inject
    lateinit var firebaseMessaging: FirebaseMessaging

    @Inject
    lateinit var googleApiAvailability: GoogleApiAvailability
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        requestNotificationsPermission()

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }

            intent.removeExtra(Intent.EXTRA_TEXT)
            findNavController(R.id.nav_host_fragment)
                R.navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = text
                    }
                )
        }

        //для гарантированного фoрм-я меню из любого потока
        //Flow - эквивалент LiveData
        lifecycleScope.launch {
            lifecycle.lifecycleRepeatOnLifecycle(RESUMED) {
                viewModel.dataAuth.observe(this@AppActivity){
                    invalidateOptionsMenu()
                }
            }

        }
        firebaseInstallations.id.addOnCompleteListener { task ->
            if(!task.isSuccessful) {
                println("some stuff happened: ${task.exception}")
                return@addOnCompleteListener
            }
            val token = task.result
            println(token)
        }

        firebaseMessaging.token.addOnCompleteListener { task ->
            if(!task.isSuccessful) {
                println("some stuff happened: ${task.exception}")
                return@addOnCompleteListener
            }
            val token = task.result
            println(token)
        }

        val menu = addMenuProvider(object: MenuProvider {
            //при создании
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }
            //при изменении
            override fun onPrepareMenu(menu: Menu) {
                menu.setGroupVisible(R.id.authenticated, viewModel.authenticated)
                menu.setGroupVisible(R.id.unauthenticated, !viewModel.authenticated)
            }
            //вариант создания и изменения
            //override fun onCreateOptionsMenu(menu: Menu): Boolean {
            //    menuInflater.inflate(R.menu.menu_main, menu)
            //
            //    menu.let {
    //                menu.setGroupVisible(R.id.authenticated, viewModel.authenticated)
    //                menu.setGroupVisible(R.id.unauthenticated, !viewModel.authenticated)
            //    }
            //    return true
            //}


            //обработчик нажатия
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                var result: Boolean
                when(menuItem.itemId){
                    R.id.signin -> {
                        appAuth.setAuth(5, "x-token")//есть такой на ДЗ сервере, не забудь в релизе удалить
                        result = true
                    }
                    R.id.signup -> {
                        appAuth.setAuth(5, "x-token")//есть такой на ДЗ сервере, не забудь в релизе удалить
                        result = true
                    }
                    R.id.signout -> {
                        appAuth.removeAuth()
                        result = true
                    }
                    else -> result = false
                }
                return result
            }
        })

        checkGoogleApiAvailability()
        requestNotificationsPermission()
    }

    override fun onStart() {
        super.onStart()
        //добавление кнопки навигации в actionBar
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                supportActionBar?.setDisplayHomeAsUpEnabled(destination.id == R.id.newPostFragment)
            }
        })
        //сокращенный вариант - лямбда, ошибка?
//        findNavController((R.id.nav_host_fragment).addOnDestinationChangedListener { controller, destination, arguments ->
//            supportActionBar?.setDisplayHomeAsUpEnabled(destination.id == R.id.newPostFragment)
//        })
    }
    private fun requestNotificationsPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }

        val permission = Manifest.permission.POST_NOTIFICATIONS

        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            return
        }

        requestPermissions(arrayOf(permission), 1)
    }

    private fun checkGoogleApiAvailability() {
        with(googleApiAvailability) {
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, R.string.google_play_unavailable, Toast.LENGTH_LONG)
                .show()
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            println(it)
        }
    }
}