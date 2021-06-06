package org.indianmusicacademy.packages.model

data class User(
    val name:String,
    val email:String,
    val phone:Int,
    val course:String,
    val feesPaid:Int
)