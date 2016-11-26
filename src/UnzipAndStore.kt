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

        val inputDir: File = File(args[0])
        val outputDir: File = File(args[1])
        val bucket: String = args[2]
        val keyPrefix: String = args[3]

        val unZip = UnZip()

        var fileList: MutableList<File> = mutableListOf()
        if (inputDir.isDirectory) {
            for (file in inputDir.listFiles { item-> item.isDirectory }) {
                fileList.addAll(unzipFilesInDir(file, outputDir, unZip))
            }
            // unzip files in root dir
            fileList.addAll(unzipFilesInDir(inputDir, outputDir, unZip))
        } else {
            // inputDir is a file (zip??)
            fileList.addAll(unzipFilesInDir(inputDir, outputDir, unZip))
        }

        Uploader.uploadFiles(bucket, keyPrefix, outputDir, outputDir.listFiles { item -> item.isFile && item.extension == "csv" }.toMutableList() )

    }

    private fun unzipFilesInDir(inputDir: File, outputDir: File, unZip: UnZip): MutableList<File> {
        var fileList: MutableList<File> = mutableListOf()
        if (inputDir.isDirectory) {
            inputDir.listFiles(FileFilter { item -> item.isFile && item.extension.toLowerCase() == "zip" })
                    .map { unZip.unZipIt(it.absolutePath, outputDir.absolutePath) }
                    .flatMap { it }
                    //.filter { it.extension.toLowerCase() == "csv" }
                    .forEach {
                        printToConsole(it)
                        fileList.add(it)
                        //UploadFile.upload(bucket, it.absolutePath, keyPrefix)
                    }
        } else if (inputDir.isFile && inputDir.extension.toLowerCase() == "zip") {
            unZip.unZipIt(inputDir.absolutePath, outputDir.absolutePath)
                    .forEach {
                        printToConsole(it)
                        fileList.add(it)
                    }
        }
/*
        File(inputDir).listFiles( {item -> item.isDirectory })
                .map { }
*/
        return fileList
    }

    private fun printToConsole(inputFile: File) {
        println(inputFile.name + "\t" + inputFile.absolutePath + "\t" + inputFile.canonicalPath + "\t" + inputFile.path.replace(inputFile.name, "").substringBeforeLast("\\").substring(3))
    }
}
