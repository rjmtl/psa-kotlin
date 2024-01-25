package com.example.psa_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.psa_android.apicalls.ApiRequests
import com.example.psa_android.preferencemanager.PreferenceManager
import com.example.psa_android.apicalls.RegisterResponse
import com.example.psa_android.apicalls.SendRegisterData
import com.example.psa_android.activity.MainActivity
import com.example.psa_android.databinding.FragmentSignUpBinding
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SignUpFragment : Fragment() {

    private lateinit var preferenceManager: PreferenceManager

    private lateinit var binding: FragmentSignUpBinding
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
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var mainActivity = activity as MainActivity
        preferenceManager = mainActivity.getPreferenceManager()

        binding.apply {
            SignUpButton.setOnClickListener {
                var name = name.text.toString()
                var email = emailId.text.toString()
                var password = password.text.toString()
                if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please enter email and password",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    progressBar.visibility=View.VISIBLE
                    makeLoginApiCall(name, email, password)

                }
            }
        }
    }


    private fun makeLoginApiCall(name: String, email: String, password: String) {
        val signUpRequest = SendRegisterData(
            "+1 (153) 318-2604",
            "+1 (334) 998-7821",
            "1980-09-20",
            email.trim(),
            name.trim(),
            "+1 (985) 377-4669",
            "",
            password.trim(),
            "+1 (284) 134-3684",
            "+1 (262) 402-9568",
            name.trim()
        )

        val call = apiService.SignUpUser(signUpRequest)

        call.enqueue(object : retrofit2.Callback<RegisterResponse> {

            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    response.let {
                        if (it.code() == 200) {
                            response.body()?.data?.let { data ->
                                preferenceManager.saveString("userId", data._id)
                                preferenceManager.saveString("email", data.email)
                            }
                            Toast.makeText(requireContext(), it.body()?.message, Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBar.visibility=View.GONE
                            findNavController().popBackStack()
                        } else {
                            binding.progressBar.visibility=View.GONE
                            Toast.makeText(requireContext(), it.body()?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility=View.GONE
                    // Handle error
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                binding.progressBar.visibility=View.INVISIBLE
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


}