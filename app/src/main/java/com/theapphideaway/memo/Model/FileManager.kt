package com.theapphideaway.memo.Model

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class FileManager {
    fun writeFile(oldTitle: String, newTitle:String, content:String){

        var filename: String?

        if(oldTitle == newTitle)
        {
            filename = "$oldTitle.txt"
        }
        else{
            filename =  "$newTitle.txt"
            //TODO Rename the file either in here or further down.
        }
        // create a File object for the parent directory
        val directory = File("/sdcard/M/")
        // have the object build the directory structure, if needed.
        directory.mkdirs()



        // create a File object for the output file
        val outputFile = File(directory, filename)
        // now attach the OutputStream to the file object, instead of a String representation
        try {
            val fos = FileOutputStream(outputFile)
        } catch (e: FileNotFoundException) {
            println(e.message.toString())
        }

        try {
            File("/sdcard/M/$filename").writeText(content)


        } catch (e:Exception){
            println(e.message)
        }
    }

    fun loadFiles():ArrayList<Note>{
        var allNotes = ArrayList<Note>()

        File("/sdcard/M/").walkBottomUp().forEach {
            println(it)

            if(it.toString() != "/sdcard/M") {
                println(it)
                val newFilename = it.name.substring(it.name.lastIndexOf("/") + 1)
                var newTitle = File(newFilename).nameWithoutExtension
                println(newTitle)
                var newContent = readFileDirectlyAsText(it.toString())
                println(newContent)
                var note = Note(newTitle, newContent)

                allNotes.add(note)
            }
        }
        return allNotes
    }

    fun readFileDirectlyAsText(fileName: String): String = File(fileName).readText(Charsets.UTF_8)
}