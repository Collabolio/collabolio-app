package com.fahixa.collabolio

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.fahixa.collabolio.databinding.ActivityRegisterBinding
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.net.MalformedURLException

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegist.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val task = HttpAsyncTask()
                val username = binding.etUsername.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val jsonInputString = """{"username": "$username", "email": "$email", "password": "$password"}"""
                task.execute("https://collabolio-dev.et.r.appspot.com/api/auth/register", jsonInputString)
            }
        })

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private inner class HttpAsyncTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            return POST(params[0], params[1])
        }

        override fun onPostExecute(result: String) {
            Log.d("HTTP POST Response", result)
            if (result == "New user registered successfully!") {
                Toast.makeText(this@RegisterActivity, result, Toast.LENGTH_LONG).show()
                val intent = Intent(this@RegisterActivity, SignInActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@RegisterActivity, result, Toast.LENGTH_LONG).show()
            }
        }

        private fun POST(url: String, input: String): String {
            var result = ""
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")

                connection.outputStream.use { os ->
                    BufferedWriter(OutputStreamWriter(os, "UTF-8")).use { writer ->
                        writer.write(input)
                    }
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    result = "New user registered successfully!"
                } else {
                    result = "Failed to register new user. Server responded with status code: $responseCode"
                }
            } catch (e: MalformedURLException) {
                Log.d("HTTP POST Error", "Invalid URL", e)
            } catch (e: IOException) {
                Log.d("HTTP POST Error", "IO Exception", e)
            } catch (e: Exception) {
                Log.d("HTTP POST Error", "Unexpected error", e)
            }

            return result
        }
    }
}