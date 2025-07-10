package com.bumperpick.bumperpickvendor.Misc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject



class RazorpayActivity : Activity(), PaymentResultWithDataListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val checkout = Checkout()
        checkout.setKeyID("rzp_test_aTDhiLtxk2SY7f") // replace with your key

        try {
            val options = JSONObject().apply {
                put("name", "Bumperpick")
                put("description", "Order Payment")
                put("currency", "INR")
                put("amount", intent.getIntExtra("amount", 100) * 100)

                val prefill = JSONObject().apply {
                    put("email", "test@example.com")
                    put("contact", "9999999999")
                }
                put("prefill", prefill)
            }

            checkout.setFullScreenDisable(true)
            checkout.open(this, options)
            Checkout.clearUserData(this)

        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?, paymentData: PaymentData?) {
        val intent = intent.apply {
            putExtra("payment_id", razorpayPaymentID)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onPaymentError(code: Int, description: String?, paymentData: PaymentData?) {
        val intent = intent.apply {
            putExtra("error", description)
        }
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }
}

