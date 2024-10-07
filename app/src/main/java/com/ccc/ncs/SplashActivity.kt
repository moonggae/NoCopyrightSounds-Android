package com.ccc.ncs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity: ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()
    private val cacheViewModel: CacheViewModel by viewModels()

    private var isCacheUiStateLoaded: Boolean = false
    private var isSplashUiStateLoaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cacheViewModel.initCacheManager(this.applicationContext)

        val content: View = findViewById(android.R.id.content)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    cacheViewModel.uiState.collect {
                        isCacheUiStateLoaded = it is CacheUiState.Success
                    }
                }

                launch {
                    viewModel.uiState.collect {
                        isSplashUiStateLoaded = it !is SplashUiState.Loading
                    }
                }
            }
        }

        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isSplashUiStateLoaded && isCacheUiStateLoaded) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }
}