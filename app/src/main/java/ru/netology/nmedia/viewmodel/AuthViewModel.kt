package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.di.DependencyContainer

class AuthViewModel(
    private val appAuth: AppAuth,
) : ViewModel(){
    val dataAuth: LiveData<AuthState> = appAuth
        .authStateFlow
        .asLiveData()
    val authenticated: Boolean
        get() = appAuth.authStateFlow.value.id != 0L
        //get() = dataAuth.value.id != 0L
        //= get() = data.value.id != 0L - так застолблять нельзя, зн-е д. иметь возм-ть меняться

}