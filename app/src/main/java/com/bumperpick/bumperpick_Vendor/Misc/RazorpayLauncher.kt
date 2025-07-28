import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.bumperpick.bumperpick_Vendor.Misc.RazorpayActivity
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

// Singleton to manage payment callbacks
object RazorpayPaymentManager {
    private var currentListener: PaymentResultWithDataListener? = null

    fun setPaymentListener(listener: PaymentResultWithDataListener) {
        currentListener = listener
    }

    fun onPaymentSuccess(paymentId: String?, data: PaymentData?) {
        currentListener?.onPaymentSuccess(paymentId, data)
    }

    fun onPaymentError(code: Int, description: String?, data: PaymentData?) {
        currentListener?.onPaymentError(code, description, data)
    }

    fun clearListener() {
        currentListener = null
    }
}

class RazorpayLauncher(
    private val activity: Activity,
    private val onSuccess: (paymentId: String, data: Map<String, Any?>?) -> Unit,
    private val onFailure: (code: Int, description: String, data: Map<String, Any?>?) -> Unit
) : PaymentResultWithDataListener {

    private val checkout = Checkout().apply {
        setKeyID("rzp_test_aTDhiLtxk2SY7f") // Replace with real key
    }

    fun startPayment(
        amountInPaise: Int,
        email: String,
        contact: String,
        merchantName: String = "BumperPick",
        description: String = "Payment"
    ) {
        // Set this launcher as the current payment listener
        RazorpayPaymentManager.setPaymentListener(this)

        val options = JSONObject().apply {
            put("name", merchantName)
            put("description", description)
            put("currency", "INR")
            put("amount", amountInPaise)
            put("prefill", JSONObject().apply {
                put("email", email)
                put("contact", contact)
            })
            put("theme", JSONObject().apply {
                put("color", "#F37254")
            })
        }

        try {
            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e("RazorpayLauncher", "Payment failed", e)
            Toast.makeText(activity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            RazorpayPaymentManager.clearListener()
        }
    }

    override fun onPaymentSuccess(paymentId: String?, data: PaymentData?) {
        onSuccess(paymentId.orEmpty(), extractData(data))
        RazorpayPaymentManager.clearListener()
    }

    override fun onPaymentError(code: Int, description: String?, data: PaymentData?) {
        onFailure(code, description.orEmpty(), extractData(data))
        RazorpayPaymentManager.clearListener()
    }

    private fun extractData(data: PaymentData?): Map<String, Any?> {
        return mapOf(
            "paymentId" to data?.paymentId,
            "orderId" to data?.orderId,
            "signature" to data?.signature
        )
    }
}

// Extension function to find Activity from Context
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@Composable
fun RazorpayPaymentButton(
    amountInPaise: Int,
    email: String,
    contact: String,
    merchantName: String = "BumperPick",
    description: String = "Payment",
    buttonText: String? = null,
    onPaymentSuccess: ((paymentId: String, data: Map<String, Any?>?) -> Unit)? = null,
    onPaymentFailure: ((code: Int, description: String, data: Map<String, Any?>?) -> Unit)? = null
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    if (activity == null) {
        Log.e("RazorpayPaymentButton", "Activity not found in context")
        return
    }

    // Create Razorpay launcher and remember it
    val launcher = remember(activity) {
        RazorpayLauncher(
            activity = activity,
            onSuccess = { paymentId, data ->
                Toast.makeText(context, "Payment Success: $paymentId", Toast.LENGTH_LONG).show()
                onPaymentSuccess?.invoke(paymentId, data)
            },
            onFailure = { code, desc, data ->
                Log.d("RazorpayPaymentButton", "Payment failed: $desc")
                Toast.makeText(context, "Payment Failed: $desc", Toast.LENGTH_LONG).show()
                onPaymentFailure?.invoke(code, desc, data)
            }
        )
    }

    Button(onClick = {
        launcher.startPayment(
            amountInPaise = amountInPaise,
            email = email,
            contact = contact,
            merchantName = merchantName,
            description = description
        )
    }) {
        Text(buttonText ?: "Pay ₹${amountInPaise / 100}")
    }
}

// Alternative: Create a more flexible payment function
@Composable
fun rememberRazorpayLauncher(
    onPaymentSuccess: (paymentId: String, data: Map<String, Any?>?) -> Unit,
    onPaymentFailure: (code: Int, description: String, data: Map<String, Any?>?) -> Unit
): RazorpayLauncher? {
    val context = LocalContext.current
    val activity = context.findActivity()

    return remember(activity) {
        activity?.let { act ->
            RazorpayLauncher(
                activity = act,
                onSuccess = onPaymentSuccess,
                onFailure = onPaymentFailure
            )
        }
    }
}

@Preview
@Composable
fun PaymentScreen() {
    val launcher = rememberRazorpayLauncher(
        onPaymentSuccess = { paymentId, data ->
            // Handle success
            Log.d("Payment", "Success: $paymentId")
        },
        onPaymentFailure = { code, description, data ->
            // Handle failure
            Log.d("Payment", "Failed: $description")
        }
    )

    Button(onClick = {
        launcher?.startPayment(
            amountInPaise = 10000,
            email = "test@example.com",
            contact = "9876543210"
        )
    }) {
        Text("Pay ₹100")
    }
}
@Preview
@Composable
fun PaymentScreen2() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val paymentId = result.data?.getStringExtra("payment_id")
            Toast.makeText(context, "Success: $paymentId", Toast.LENGTH_LONG).show()
        } else {
            val error = result.data?.getStringExtra("error") ?: "Cancelled"
            Toast.makeText(context, "Failed: $error", Toast.LENGTH_LONG).show()
        }
    }

    Button(onClick = {
        val intent = Intent(context, RazorpayActivity::class.java)
        intent.putExtra("amount", 100) // ₹100
        launcher.launch(intent)
    }) {
        Text("Pay ₹100")
    }
}

