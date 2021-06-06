package org.indianmusicacademy.packages.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails
import org.indianmusicacademy.packages.R

class Registration : AppCompatActivity(),PaymentStatusListener {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etPhone: EditText
    lateinit var tvTotalFees: TextView
    lateinit var etAmount: EditText
    lateinit var btnRegister: Button
    lateinit var mAuth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var tvCourseName: TextView
    private val UPIREQUESTCODE = 416101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Registration"

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        tvTotalFees = findViewById(R.id.tvTotalFees)
        etAmount = findViewById(R.id.etAmount)
        btnRegister = findViewById(R.id.btnRegister)
        mAuth = FirebaseAuth.getInstance()

        etName.setText(mAuth.currentUser?.displayName)
        etEmail.setText(mAuth.currentUser?.email)

        firestore = FirebaseFirestore.getInstance()
        tvCourseName = findViewById(R.id.tvCourseName)

        if (intent != null) {
            tvCourseName.text = intent.getStringExtra("CourseName")
        }

        btnRegister.setOnClickListener {

            if (org.indianmusicacademy.packages.util.ConnectivityManager()
                    .checkConnectivity(this@Registration)
            ) {

                if (etName.text.isEmpty() || etEmail.text.isEmpty() || etPhone.text.isEmpty() || etAmount.text.isEmpty()) {
                    Toast.makeText(this@Registration, "Enter all Details", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (etPhone.text.length != 10) {
                        etPhone.error = "Invalid Mobile Number"
                    } else {
                        if (etAmount.text.toString().toInt() < 500) {
                            etAmount.error = "Pay Minimum Rs.500"
                        } else {
                            Toast.makeText(
                                this@Registration,
                                "Registered Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            /*payUsingUPI(
                                etName.text.toString(),
                                etAmount.text.toString(),
                                tvCourseName.toString()
                            )*/
                            val upi = EasyUpiPayment.Builder().with(this@Registration)
                                .setPayeeVpa(getString(R.string.upiId))
                                .setPayeeName(etName.text.toString())
                                .setTransactionId(etPhone.text.toString())
                                .setTransactionRefId(etEmail.text.toString())
                                .setDescription(tvCourseName.text.toString())
                                .setAmount(etAmount.text.toString())
                                .build()

                            upi.startPayment()
                        }
                    }
                }

            } else {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("No Internet")
                dialog.setMessage("Please check your Internet Connection")
                dialog.setPositiveButton("Open Settings") { text, listener ->

                    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(intent)
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this)
                }

                dialog.create()
                dialog.show()
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun payUsingUPI(name: String, amount: String, courseName: String) {

        val uri =
            Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa", getString(R.string.upiId))
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", courseName)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build()

        val upiIntent = Intent(Intent.ACTION_VIEW)
        upiIntent.data = uri

        val chooser = Intent.createChooser(upiIntent, "Pay with")

        if (chooser.resolveActivity(packageManager) != null) {
            startActivityForResult(chooser, UPIREQUESTCODE)
        } else {
            Toast.makeText(this@Registration, "No UPI app found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun upiPaymentDataOperation(data: ArrayList<String>) {
        if (org.indianmusicacademy.packages.util.ConnectivityManager()
                .checkConnectivity(this@Registration)
        ) {
            var str: String? = data[0]
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str!!)
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in response.indices) {
                val equalStr =
                    response[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].toLowerCase() == "Status".toLowerCase()) {
                        status = equalStr[1].toLowerCase()
                    } else if (equalStr[0].toLowerCase() == "ApprovalRefNo".toLowerCase() || equalStr[0].toLowerCase() == "txnRef".toLowerCase()) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }

            if (status == "success") {
                //Code to handle successful transaction here.
                Toast.makeText(this@Registration, "Transaction successful.", Toast.LENGTH_SHORT)
                    .show()
                Log.d("UPI", "responseStr: $approvalRefNo")
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(this@Registration, "Payment cancelled by user.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    this@Registration,
                    "Transaction failed.Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this@Registration,
                "Internet connection is not available. Please check and try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onTransactionSubmitted() {
        TODO("Not yet implemented")
    }

    override fun onTransactionCancelled() {
        TODO("Not yet implemented")
    }

    override fun onTransactionSuccess() {
        TODO("Not yet implemented")
    }

    override fun onAppNotFound() {
        TODO("Not yet implemented")
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails?) {
        TODO("Not yet implemented")
    }

    override fun onTransactionFailed() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}