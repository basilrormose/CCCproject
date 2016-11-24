/**
 * Created by rbasil on 19/11/2016.
 */
package com.rormose.capstone

import java.io.File
import java.io.IOException
import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.PutObjectRequest

object UploadFile {

    @Throws(IOException::class)
    fun  upload(bucketName: String, uploadFileName: String, keyPrefix: String) {
        val s3client = AmazonS3Client(ProfileCredentialsProvider().credentials)
        try
        {
            println("Uploading a new object to S3 from a file\n")
            val file = File(uploadFileName)
            val keyName = "$keyPrefix/${file.name}"
            s3client.putObject(PutObjectRequest(
                    bucketName, keyName, file))
        }
        catch (ase:AmazonServiceException) {
            println("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.")
            System.out.println("Error Message: " + ase.errorMessage)
            System.out.println("HTTP Status Code: " + ase.statusCode)
            System.out.println("AWS Error Code: " + ase.errorCode)
            System.out.println("Error Type: " + ase.errorType)
            System.out.println("Request ID: " + ase.requestId)
        }
        catch (ace:AmazonClientException) {
            println("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.")
            System.out.println("Error Message: " + ace.message)
        }
    }
}
