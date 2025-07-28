package com.bumperpick.bumperpick_Vendor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bumperpick.bumperpick_Vendor.Navigation.AppNavigation
import com.bumperpick.bumperpick_Vendor.ui.theme.BumperPickVendorTheme
import com.google.firebase.messaging.FirebaseMessaging
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener

class MainActivity : ComponentActivity(), PaymentResultWithDataListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firebaseInstance= FirebaseMessaging.getInstance()

        firebaseInstance .subscribeToTopic("admin")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to 'admin' topic")
                }
            }

        enableEdgeToEdge()
        setContent {
            BumperPickVendorTheme {
                AppNavigation()
            }
        }
    }
    override fun onPaymentSuccess(paymentId: String?, data: PaymentData?) {
        // Forward to the payment manager
        RazorpayPaymentManager.onPaymentSuccess(paymentId, data)
    }

    override fun onPaymentError(code: Int, description: String?, data: PaymentData?) {
        // Forward to the payment manager
        RazorpayPaymentManager.onPaymentError(code, description, data)
    }

}

