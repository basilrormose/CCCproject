/**
 * Created by rbasil on 19/11/2016.
 */
package com.rormose.capstone

import java.io.File
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.s3.transfer.TransferManager

object Uploader {

    fun uploadFiles(bucket: String, dirKeyPrefix: String,  sourceDir: File, files: MutableList<File>): Unit {
        val credentialProviderChain = DefaultAWSCredentialsProviderChain()
        val tx = TransferManager(credentialProviderChain.credentials)
        val myUpload = tx.uploadFileList(bucket, dirKeyPrefix, sourceDir, files)
        while (myUpload.isDone === false) {
            System.out.println("Transfer: " + myUpload.description)
            System.out.println(" - State: " + myUpload.state)
            System.out.println(" - Progress: " + myUpload.progress.bytesTransferred)
            Thread.sleep(10000)
        }
        //myUpload.addProgressListener(myProgressListener)
        myUpload.waitForCompletion()
        tx.shutdownNow()
    }
}
