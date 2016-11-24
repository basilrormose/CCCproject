package com.rormose.capstone

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class UnZip {

    /**
     * Unzip it
     * @param zipFile input zip file
     * *
     * @param output zip file output folder
     */
    fun unZipIt(zipFile: String, outputFolder: String, filter: String = "(?i)(Readme|Terms|html)") : MutableList<File> {

        var fileList: MutableList<File> = mutableListOf()
        val buffer = ByteArray(1024)

        try {

            //create output directory is not exists
            val folder = File(outputFolder)
            if (!folder.exists()) {
                folder.mkdir()
            }

            try {
                //get the zip file content
                val zis = ZipInputStream(FileInputStream(zipFile))
                //get the zipped file list entry
                var ze: ZipEntry? = zis.nextEntry

                while (ze != null) {

                    //only the files with extension 'filter'
                    if (!ze.name.contains(regex = Regex(filter)) && ze.name.endsWith("csv")) {
                        val newFile = File(outputFolder + File.separator + ze.name)

                        println("file unzip : " + newFile.absoluteFile)
                        fileList.add(newFile)

                        //create all non exists folders
                        //else you will hit FileNotFoundException for compressed folder
                        File(newFile.parent).mkdirs()

                        val fos = FileOutputStream(newFile)

                        var len: Int = zis.read(buffer)
                        while (len > 0) {
                            fos.write(buffer, 0, len)
                            len = zis.read(buffer)
                        }

                        fos.close()
                    }
                    ze = zis.nextEntry
                }
                zis.closeEntry()
                zis.close()

            } catch (e: Exception) {
                println("Exception $e.message for $zipFile")
            }

            println("Done")

        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return fileList
    }


}
