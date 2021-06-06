package org.indianmusicacademy.packages.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.indianmusicacademy.packages.R

class Settings : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val bottomNav: BottomNavigationView? = activity?.findViewById(R.id.bottomView)
        bottomNav?.visibility = View.GONE
        return view
    }
}