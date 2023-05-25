package com.fahixa.collabolio

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fahixa.collabolio.databinding.ActivitySigninBinding
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val task = HttpAsyncTask()
                val email = binding.etEmailLgn.text.toString()
                val password = binding.etPasswordLgn.text.toString()
                val jsonInputString = """{"email": "$email", "password": "$password"}"""
                task.execute("https://collabolio-dev.et.r.appspot.com/api/auth/login", jsonInputString)
            }
        })

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this@SignInActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private inner class HttpAsyncTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            return POST(params[0], params[1])
        }

        override fun onPostExecute(result: String) {
            Log.d("HTTP POST Response", result)
            if (result.startsWith("Login Successful")) {
                Toast.makeText(this@SignInActivity, "Login successful", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@SignInActivity, "Login failed", Toast.LENGTH_LONG).show()
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
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    result = "Login Successful: $responseCode"
                } else {
                    result = "Login Failed: Server responded with status code: $responseCode"
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