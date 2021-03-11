/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.helper.SingleLiveEvent
import com.example.androiddevchallenge.utils.Utility
import com.example.androiddevchallenge.utils.Utility.formatTime

class MainViewModel : ViewModel() {

    //region Properties
    private var countDownTimer: CountDownTimer? = null
    //endregion

    //region States
    private val _time = MutableLiveData(Utility.TIME_COUNTDOWN.formatTime())
    val time: LiveData<String> = _time

    private val _progress = MutableLiveData(1.00F)
    val progress: LiveData<Float> = _progress

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    // hold data for celebrate view as boolean

    // private
    private val _celebrate = SingleLiveEvent<Boolean>()

    // accessed publicly
    val celebrate: LiveData<Boolean> get() = _celebrate

    //endregion

    //region Public methods
    fun handleCountDownTimer() {
        if (isPlaying.value == true) {
            pauseTimer()
            _celebrate.postValue(false)
        } else {
            startTimer()
        }
    }
    //endregion

    //region Private methods
    private fun pauseTimer() {
        countDownTimer?.cancel()
        handleTimerValues(false, Utility.TIME_COUNTDOWN.formatTime(), 1.0F, false)
    }

    private fun startTimer() {

        _isPlaying.value = true
        countDownTimer = object : CountDownTimer(Utility.TIME_COUNTDOWN, 1000) {

            override fun onTick(millisRemaining: Long) {
                val progressValue = millisRemaining.toFloat() / Utility.TIME_COUNTDOWN
                handleTimerValues(true, millisRemaining.formatTime(), progressValue, false)
                _celebrate.postValue(false)
            }

            override fun onFinish() {
                pauseTimer()
                _celebrate.postValue(true)
            }
        }.start()
    }

    private fun handleTimerValues(isPlaying: Boolean, text: String, progress: Float, celebrate: Boolean) {
        _isPlaying.value = isPlaying
        _time.value = text
        _progress.value = progress
        _celebrate.postValue(celebrate)
    }
    //endregion
}
