package com.example.psa_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.psa_android.apicalls.ApiRequests
import com.example.psa_android.apicalls.LoginResponse
import com.example.psa_android.apicalls.PSAApplicationClass
import com.example.psa_android.preferencemanager.PreferenceManager
import com.example.psa_android.R
import com.example.psa_android.apicalls.SendRequestData
import com.example.psa_android.activity.MainActivity
import com.example.psa_android.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://app-backend-psa-d2c4cf36bd0c.herokuapp.com/"

class LoginFragment : Fragment() {

    private lateinit var preferenceManager: PreferenceManager
    var FCMToken = ""

    private lateinit var binding: FragmentLoginBinding
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val apiService: ApiRequests = retrofit.create(ApiRequests::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        var mainActivity = activity as MainActivity
        preferenceManager = mainActivity.getPreferenceManager()

        if (preferenceManager.getString("isRegistered","")=="true"){
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
        PSAApplicationClass().notificationOpened()

        binding.apply {

            loginButton.setOnClickListener {
                var email = emailId.text.toString()
                var password = password.text.toString()
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please enter email and password",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    progressBar.visibility=View.VISIBLE
                    makeLoginApiCall(email, password)

                }
            }
            signUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }


        }
    }

    private fun makeLoginApiCall(email: String, password: String) {
        val loginRequest = SendRequestData(email.trim(), password.trim())
        val call = apiService.loginUser(loginRequest)

        call.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    response.let { response ->
                        if (response.body()?.status == 200) {
                            response.body()?.data?.let { data ->
                                preferenceManager.saveString("userId", data._id)
                                preferenceManager.saveString("email", data.email)
                                preferenceManager.saveString("isRegistered", "true")
                            }
                            binding.progressBar.visibility=View.INVISIBLE
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            binding.progressBar.visibility=View.INVISIBLE
                            Toast.makeText(
                                requireContext(),
                                response.body()?.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                } else {
                    binding.progressBar.visibility=View.INVISIBLE
                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_SHORT).show()
                    // Handle error
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.progressBar.visibility=View.INVISIBLE
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                // Handle failure
            }
        })
    }


}