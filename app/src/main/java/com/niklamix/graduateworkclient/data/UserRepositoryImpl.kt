package com.niklamix.graduateworkclient.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.niklamix.graduateworkclient.domain.entity.ExceptionInfo
import com.niklamix.graduateworkclient.domain.entity.UserFilter
import com.niklamix.graduateworkclient.domain.entity.UserItemRead
import com.niklamix.graduateworkclient.domain.entity.UserItemWrite
import com.niklamix.graduateworkclient.domain.repository.UserRepository
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Credentials
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.CountDownLatch

object UserRepositoryImpl : UserRepository {
    private const val REST_URL = "http://192.168.1.64:8080/"

    private var credentials: String? = null
    private val gson = Gson()

    override fun addUserItem(user: UserItemWrite, context: Context?): Boolean {
        val latch = CountDownLatch(1)
        var result = false
        val urlUpdateUser = "api/guest/register"
        val client = OkHttpClient()
        val json = Gson().toJson(user)
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val requestBody = RequestBody.create(mediaType, json)
        println("-------------- отправляю запрос")
        val request = Request.Builder()
            .url(REST_URL + urlUpdateUser)
            .header("Authorization", credentials!!)
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    getToastMessage("Successful", context)
                    result = true
                } else {
                    getToastMessage("Not successful. " +
                            "Some of the fields are entered incorrectly", context)
                    println("Status code: ${response.code()}")
                    println("response message: ${response.message()}")
                    println("response body: ${response.body()}")
                }
                latch.countDown()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                getToastMessage("An error occurred while connecting to the server. Try a little later",
                    context)
                latch.countDown()
            }
        })
        latch.await()
        return result
    }

    override fun deleteUserItem(user: UserItemRead, context: Context?) {
        val latch = CountDownLatch(1)
        val urlUpdateUser = "api/admin/users/${user.id}"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(REST_URL + urlUpdateUser)
            .header("Authorization", credentials!!)
            .delete()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    getToastMessage("Delete successful", context)
                } else {
                    getToastMessage("Delete is not successful", context)
                    println("Status code: ${response.code()}")
                    println("response message: ${response.message()}")
                    println("response body: ${response.body()}")
                }
                latch.countDown()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                getToastMessage("An error occurred while connecting to the server. Try a little later",
                    context)
                latch.countDown()
            }
        })
        latch.await()
    }

    override fun getCurrentUserItem(context: Context?): UserItemRead? {
        val latch = CountDownLatch(1)
        val urlGetCurrentUser = "api/user/users"
        var userItem: UserItemRead? = null
        val client = OkHttpClient()
        client.newCall(getRequest(urlGetCurrentUser)).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    userItem = gson.fromJson(responseBody, UserItemRead::class.java)
                } else {
                    if (response.code() == 401) {
                        getToastMessage("Login or password entered incorrectly", context)
                    } else {
                        getToastMessage("Response in not successful"
                            , context)
                        println("Status code: ${response.code()}")
                    }

                }
                latch.countDown()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                getToastMessage("An error occurred while connecting to the server. Try a little later"
                    , context)
                latch.countDown()
            }
        })
        latch.await()
        return userItem
    }
    override fun getUserList(page: Int, size: Int, filter: UserFilter, context: Context?):
            MutableLiveData<List<UserItemRead>> {
        val latch = CountDownLatch(1)
        val userListLiveData: MutableLiveData<List<UserItemRead>> = MutableLiveData()
        val urlGetCurrentUser = "api/user/users/filter?page=${page}&size=${size}" +
                "&name=${filter.name}&surname=${filter.surname}&login=${filter.login}"
        val client = OkHttpClient()
        println("-------------- отправляю запрос")
        client.newCall(getRequest(urlGetCurrentUser)).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("Response code ${response.code()}")

                    val responseBody = response.body()?.string()
                    println(responseBody)
                    val userList = gson.fromJson(responseBody, Array<UserItemRead>::class.java).toList()
                    userListLiveData.postValue(userList)
                    getToastMessage("Response is successful."
                        , context)
                } else {
                    println(response.cacheResponse().toString())
                    getToastMessage("Response in not successful. Status code: ${response.code()}"
                        , context)
                }
                latch.countDown()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                getToastMessage("An error occurred while connecting to the server. Try a little later"
                    , context)
                latch.countDown()
            }
        })
        latch.await()
        return userListLiveData
    }

    override fun updateUserItem(userId: Long?, user: UserItemWrite, adminFlag: Boolean, context: Context?): Boolean {
        val latch = CountDownLatch(1)
        var result = false
        var urlUpdateUser = "api/user/users"
        if (adminFlag) {
            urlUpdateUser = "api/admin/users/$userId"
        }
        val client = OkHttpClient()
        val json = Gson().toJson(user)
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val requestBody = RequestBody.create(mediaType, json)
        println("-------------- отправляю запрос")
        val request = Request.Builder()
            .url(REST_URL + urlUpdateUser)
            .header("Authorization", credentials!!)
            .put(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("Status code: ${response.code()}")
                    getToastMessage("Update is successful", context)
                    result = true
                } else {
                    val responseBody = response.body()?.string()
                    val info = gson.fromJson(responseBody, ExceptionInfo::class.java)
                    getToastMessage("Update is not successful. ${info.info}", context)
                    println("Status code: ${response.code()}")
                    println("response message: ${response.message()}")
                    println("response body: ${response.body()}")
                }
                latch.countDown()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                getToastMessage("An error occurred while connecting to the server. Try a little later",
                context)
                latch.countDown()
            }
        })
        latch.await()
        return result
    }

    override fun changeAdminFlag(user: UserItemRead, context: Context?) {
        val urlChangeAdmin = "api/admin/users/${user.id}/admin"
        println("-------------- отправляю запрос")
        updateUserFlag(urlChangeAdmin, context)

    }

    override fun changeEnabledFlag(user: UserItemRead, context: Context?) {
        val urlChangeEnabled = "api/admin/users/${user.id}/enabled"
        println("-------------- отправляю запрос")
        updateUserFlag(urlChangeEnabled, context)
    }

    private fun updateUserFlag(url: String, context: Context?) {
        val latch = CountDownLatch(1)
        val client = OkHttpClient()
        client.newCall(getRequest(url)).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("Status code: ${response.code()}")
                    getToastMessage("Update is successful", context)
                } else {
                    getToastMessage("Update is not successful.", context)
                    println("Status code: ${response.code()}")
                    println("response message: ${response.message()}")
                    println("response body: ${response.body()}")
                }
                latch.countDown()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                getToastMessage("An error occurred while connecting to the server. Try a little later",
                    context)
                latch.countDown()
            }
        })
        latch.await()
    }

    private fun getRequest(url: String): Request {
        return Request.Builder()
            .url(REST_URL + url)
            .header("Authorization", credentials!!)
            .build()
    }

    override fun updateCredentials(login: String, password: String) {
        println("login: $login , password: $password")
        credentials = Credentials.basic(login, password)
    }
    private fun getToastMessage(message: String, context: Context?) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}