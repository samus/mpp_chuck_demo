package com.synappticlabs.chuck

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jokeTextView = this.findViewById<TextView>(R.id.jokeText)
        val reloadButton = this.findViewById<FloatingActionButton>(R.id.reloadJokeButton)
        val vm = ViewModelProviders.of(this)[MainViewModel::class.java]

        vm.jokeText.observe(this, Observer { text ->
            jokeTextView.text = text
        })

        reloadButton.setOnClickListener { vm.fetchJoke() }

        vm.fetchJoke()
    }
}