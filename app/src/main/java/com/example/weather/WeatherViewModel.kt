package com.example.weather

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.API.Constants
import com.example.weather.API.NetworkResponse
import com.example.weather.API.RetrofitInstance
import com.example.weather.API.WeatherResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WeatherViewModel : ViewModel() {

    //calling the retrofit api to our viewmodel
    private val weatherAPI = RetrofitInstance.weatherAPI

    //Livedata
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherResponse>>()
    val weatherResult: LiveData<NetworkResponse<WeatherResponse>> = _weatherResult

    //from the UI Search for City//
    fun getData(city: String) {

        viewModelScope.launch {
            _weatherResult.value = NetworkResponse.Loading
            try {
                val response = weatherAPI.getCurrentWeather(
                    apiKey = Constants.apikey, location = city
                )
                if (response.isSuccessful) {
                    _weatherResult.value = NetworkResponse.Success(response.body()!!)
                    Log.d(TAG, response.body().toString())
                } else {
                    _weatherResult.value = NetworkResponse.Failure(message = "Failed to fetch data")
                    Log.d(
                        TAG,
                        "you have searched this city: ${NetworkResponse.Failure(message = "Failed to fetch data")}"
                    )
                }
            }catch (e: IOException){
                _weatherResult.value = NetworkResponse.Failure(message = "No internet connection")
                Log.d(TAG, "you have searched this city: ${NetworkResponse.Failure(message = "No internet connection")}")
            }catch (e: HttpException){
                _weatherResult.value = NetworkResponse.Failure(message = "Something went wrong")
                Log.d(TAG, "you have searched this city: ${NetworkResponse.Failure(message = "Something went wrong")}")

            }
        }
    }
}