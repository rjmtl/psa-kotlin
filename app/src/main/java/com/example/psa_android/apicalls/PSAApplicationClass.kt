package com.example.psa_android.apicalls

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import com.example.psa_android.preferencemanager.PreferenceManager
import com.google.firebase.FirebaseApp
import com.snowplowanalytics.snowplow.Snowplow
import com.snowplowanalytics.snowplow.Snowplow.createTracker
import com.snowplowanalytics.snowplow.configuration.NetworkConfiguration
import com.snowplowanalytics.snowplow.configuration.SessionConfiguration
import com.snowplowanalytics.snowplow.configuration.TrackerConfiguration
import com.snowplowanalytics.snowplow.controller.TrackerController
import com.snowplowanalytics.snowplow.event.SelfDescribing
import com.snowplowanalytics.snowplow.event.Structured
import com.snowplowanalytics.snowplow.network.HttpMethod
import com.snowplowanalytics.snowplow.payload.SelfDescribingJson
import com.snowplowanalytics.snowplow.util.TimeMeasure
import java.util.concurrent.TimeUnit

open class PSAApplicationClass : Application() {

    private var tracker: TrackerController? = null
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate() {
        super.onCreate()
        preferenceManager =
            PreferenceManager.getInstance(applicationContext)

        FirebaseApp.initializeApp(this)
        val tracker = createTracker(
            applicationContext, // Android context (LocalContext.current in Compose apps)
            "appTracker", // namespace
            "https://snowplow-collector-url.com" // Event collector URL
        )

        val event = Structured("Category_example", "Action_example")
        tracker.track(event)

        Snowplow.defaultTracker?.track(event)

        val networkConfig = NetworkConfiguration(
            "https://snowplow-collector-url.com",
            HttpMethod.POST
        )
        val trackerConfig = TrackerConfiguration("psa-kotlin-android")
            .base64encoding(false)
            .sessionContext(true)
            .platformContext(true)
            .lifecycleAutotracking(true)
            .screenViewAutotracking(true)
            .screenContext(true)
            .applicationContext(true)
            .exceptionAutotracking(true)
            .installAutotracking(true)
            .userAnonymisation(false)
        val sessionConfig = SessionConfiguration(
            TimeMeasure(30, TimeUnit.MINUTES),
            TimeMeasure(30, TimeUnit.MINUTES)
        )

        createTracker(
            applicationContext,
            "gt-kotlin",
            networkConfig,
            trackerConfig,
            sessionConfig
        )

//       )

    }

    fun notificationReceived() {
        preferenceManager =
            PreferenceManager.getInstance(this)
        var userId =preferenceManager.getString("userId","")
        val data = mapOf(
            "psa_campaign_id" to "PSA Campaign Id",    // pass null for now
            "user_id" to userId.ifEmpty { null }, // if user logged in pass if not null
            "psa_ma_id" to "PSA MA Id" // pass null for now
        )
        val json = SelfDescribingJson(
            "iglu:com.proemsportsanalytics/notification_received/jsonschema/1-0-0",
            data
        )
        val event = SelfDescribing(json)
        tracker?.track(event)

    }

    fun notificationOpened() {
        preferenceManager =
            PreferenceManager.getInstance(this)
        var userId =preferenceManager.getString("userId","")
        val data = mapOf(
            "psa_campaign_id" to "PSA Campaign Id",    // pass null for now
            "user_id" to userId.ifEmpty { null }, // if user logged in pass if not null
            "psa_ma_id" to "PSA MA Id" // pass null for now
        )
        val json = SelfDescribingJson(
            "iglu:com.proemsportsanalytics/notification_opened/jsonschema/1-0-0",
            data
        )
        val event = SelfDescribing(json)
        tracker?.track(event)

    }

    fun updateFCM() {
        Log.d(TAG, "updateFCM: ")
        preferenceManager =
            PreferenceManager.getInstance(this)
        var fcm = preferenceManager.getString("fcmToken","")

        val data = mapOf(
            "fcm_token" to fcm.ifEmpty { null }    // pass null if not available
        )
        val json = SelfDescribingJson(
            "iglu:com.proemsportsanalytics/update_fcm_token/jsonschema/1-0-0",
            data
        )
        val event = SelfDescribing(json)
        tracker?.track(event)

    }

}