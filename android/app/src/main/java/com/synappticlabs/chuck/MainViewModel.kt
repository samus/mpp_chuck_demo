package com.synappticlabs.chuck

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.synappticlabs.chuck.network.JokeApiClient
import com.synappticlabs.chuck.network.JokeExclusions
import com.synappticlabs.chuck.network.launchAndCatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.android.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val jokeText = MutableLiveData<String?>()
    val apiClient = JokeApiClient(Dispatchers.Main)

    fun fetchJoke() {
        jokeText.value = getApplication<Application>().resources.getString(R.string.loading)

        launchAndCatch(Dispatchers.Main, onError = {error ->
            jokeText.value = error.localizedMessage
        }){
            apiClient.random(exclusions = JokeExclusions.Explicit).jokes.firstOrNull()?.let { joke ->
                jokeText.value = joke.text
            }
        }
    }
}