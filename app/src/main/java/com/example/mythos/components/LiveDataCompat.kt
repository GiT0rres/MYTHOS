package com.example.mythos.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData

/**
 * Pequeno wrapper para observar LiveData no Compose sem repetir o import
 * em todas as telas.
 */
@Composable
fun <T> LiveData<T>.observeAsStateCompat(): State<T?> = observeAsState()
