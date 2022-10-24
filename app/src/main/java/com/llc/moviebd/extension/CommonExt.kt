package com.llc.moviebd.extension

//sample function
fun hourMinute(t: Long): String {
    val hours: Long = t / 60 //since both are ints, you get an int
    val minutes: Long = t % 60
    return "${hours}h ${minutes}m"
}

//This is extension function
fun Long.toHourMinute(): String {
    val hours: Long = this / 60 //since both are ints, you get an int
    val minutes: Long = this % 60
    return "${hours}h ${minutes}m"
}