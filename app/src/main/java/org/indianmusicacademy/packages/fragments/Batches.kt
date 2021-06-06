package org.indianmusicacademy.packages.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import org.indianmusicacademy.packages.R
import org.indianmusicacademy.packages.adapters.CourseAdapter
import org.indianmusicacademy.packages.model.Course
import org.indianmusicacademy.packages.util.ConnectivityManager

class Batches : Fragment() {

    lateinit var rvCourse: RecyclerView
    lateinit var firestore: FirebaseFirestore
    lateinit var progressBar: RelativeLayout
    private var courseList = arrayListOf<Course>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_batches, container, false)

        rvCourse = view.findViewById(R.id.rvCourse)
        rvCourse.layoutManager = LinearLayoutManager(activity as Context)

        firestore = FirebaseFirestore.getInstance()
        progressBar = view.findViewById(R.id.progressBar)

        if (ConnectivityManager().checkConnectivity(activity as Context)) {
            val getDoc = firestore.collection("courses").get()

            getDoc.addOnSuccessListener {
                for(document in it) {
                    val course = document.toObject(Course::class.java)
                    courseList.add(course)
                }
                rvCourse.adapter = CourseAdapter(activity as Context,courseList)
                progressBar.visibility = View.GONE
            }
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
        return view
    }
}