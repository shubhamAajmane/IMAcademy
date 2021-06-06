package org.indianmusicacademy.packages.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.indianmusicacademy.packages.R
import org.indianmusicacademy.packages.activities.LoginActivity
import org.indianmusicacademy.packages.util.ConnectivityManager

class MyProfile : Fragment() {

    lateinit var tvName: TextView
    lateinit var tvEmail: TextView
    lateinit var tvPhone: TextView
    lateinit var tvCourse: TextView
    lateinit var tvFees: TextView
    lateinit var btnLogout: Button
    lateinit var mAuth: FirebaseAuth
    lateinit var firestore:FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)

        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvPhone = view.findViewById(R.id.tvPhone)
        tvCourse = view.findViewById(R.id.tvCourse)
        tvFees = view.findViewById(R.id.tvFees)
        btnLogout = view.findViewById(R.id.btnLogout)
        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val doc = firestore.collection("courses").document()

        btnLogout.setOnClickListener {

            if (ConnectivityManager().checkConnectivity(activity as Context)) {
                mAuth.signOut()
                startActivity(Intent(context,LoginActivity::class.java))
                activity?.finish()
            } else {
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("No Internet")
                dialog.setMessage("Please check your Internet Connection")
                dialog.setPositiveButton("Open Settings") { text, listener ->

                    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(intent)
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(requireActivity())
                }

                dialog.create()
                dialog.show()
            }
        }

        return view
    }
}