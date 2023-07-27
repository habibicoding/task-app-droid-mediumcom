package com.example.task_app_droid_mediumcom


import java.io.InputStreamReader

class FileReader(path: String) {

    val content: String

    init {
        val reader = InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    }

}