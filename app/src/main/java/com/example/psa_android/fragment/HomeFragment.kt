package com.example.psa_android.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.psa_android.apicalls.PSAApplicationClass
import com.example.psa_android.preferencemanager.PreferenceManager
import com.example.psa_android.activity.MainActivity
import com.example.psa_android.databinding.FragmentHomeBinding
import com.google.firebase.messaging.FirebaseMessaging

class HomeFragment : Fragment() {

    private lateinit var preferenceManager: PreferenceManager


    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var FCMToken=""
        var mainActivity = activity as MainActivity
        preferenceManager = mainActivity.getPreferenceManager()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FCMToken = task.result
                if (preferenceManager.getString("fcmToken", "") != FCMToken) {
                    PSAApplicationClass().updateFCM()
                    preferenceManager.saveString("fcmToken", FCMToken)
                        Log.d("adaad", "Token: $FCMToken")
                    binding.fcmToken.text = preferenceManager.getString("fcmToken", "")
                }

            } else {
                Log.e(
                    "FCM Token",
                    "Failed to retrieve token: ${task.exception}"
                )
            }
        }

        binding.apply {
            userId.text = preferenceManager.getString("userId", "")
            emailId.text = preferenceManager.getString("email", "")
            fcmToken.text = preferenceManager.getString("fcmToken", "")

            logOutButton.setOnClickListener {
                preferenceManager.clearPreferences()
                findNavController().popBackStack()
            }
        }
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(TAG, "Fragment back pressed invoked")
                    requireActivity().finish()
                }
            }
            )
    }



}