package org.indianmusicacademy.packages.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.indianmusicacademy.packages.R
import org.indianmusicacademy.packages.activities.Registration
import org.indianmusicacademy.packages.model.Course

class CourseAdapter(val context:Context,val courseList:ArrayList<Course>):RecyclerView.Adapter<CourseAdapter.CourseHolder>() {

    class CourseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCourseName)
        val tvStartingDate: TextView = itemView.findViewById(R.id.tvStartingDate)
        val ivImage: ImageView = itemView.findViewById(R.id.ivCourseIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.course_item,parent,false)

        return CourseHolder(itemView)
    }

    override fun getItemCount(): Int = courseList.size

    override fun onBindViewHolder(holder: CourseHolder, position: Int) {

        val courseItem = courseList[position]

        holder.tvName.text = courseItem.name
        holder.tvStartingDate.text = courseItem.starting_date
        Picasso.get().load(courseItem.image).into(holder.ivImage)

        holder.itemView.setOnClickListener {

            val intent = Intent(context,Registration::class.java)
            intent.putExtra("CourseName",courseItem.name)
            context.startActivity(intent)
        }
    }
}