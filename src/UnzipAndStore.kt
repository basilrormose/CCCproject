/**
 * Created by rbasil on 19/11/2016.
 */

package com.rormose.capstone

import java.io.File
import java.io.FileFilter
import kotlin.system.exitProcess

object UnzipAndStore {
    private val INPUT_ZIP_FILE = "C:\\Users\\rbasil\\Dropbox\\Courses\\Cloud Computing\\Cloud Computing Capstone\\air_carrier_stats_US\\2008.zip"
    private val OUTPUT_FOLDER = "C:\\outputzip"
    private val BUCKET_NAME = "C:\\outputzip"

    @JvmStatic fun main(args: Array<String>) {

        if (args.isEmpty() || args.size < 4) {
            println("Invalid number of arguments: ${args.size} should be 4!")
            println("Arguments required: inputDir outputDir bucket")
            exitProcess(1)
        }

        val inputDir: String = args[0]
        val outputDir: String = args[1]
        val bucket: String = args[2]
        val keyPrefix: String = args[3]

        val unZip = UnZip()

        //val fileList = unZip.unZipIt(inputDir, outputDir)
        val testFldr = File("C:\\Users\\rbasil\\Dropbox\\Natalya\\Project 1 2016-17")
        Uploader.uploadFiles(bucket, keyPrefix, testFldr, testFldr.listFiles().toMutableList() )

/*        Uploader.uploadFiles(bucket, keyPrefix, File(outputDir), File(outputDir).listFiles { item -> item.isFile && item.extension == "csv" }.toMutableList() )*/

        if (File(inputDir).isDirectory) {
            unzipFilesInDir(inputDir, outputDir, unZip)
        }

        // Runtime.getRuntime().exec("mycommand.sh")
    }

    private fun unzipFilesInDir(inputDir: String, outputDir: String, unZip: UnZip): MutableList<File> {
        var fileList: MutableList<File> = mutableListOf()
        File(inputDir).listFiles(FileFilter { item -> item.isFile && item.extension == "zip" })
                .map { unZip.unZipIt(it.absolutePath, outputDir) }
                .flatMap { it }
                .forEach {
                    println(it.name + "\t" + it.absolutePath + "\t" + it.canonicalPath + "\t" + it.path.replace(it.name, "").substringBeforeLast("\\").substring(3))
                    fileList.add(it)
                    //UploadFile.upload(bucket, it.absolutePath, keyPrefix)
                }
        File(inputDir).listFiles( {item -> item.isDirectory })
                .map { }
        return fileList
    }
}
