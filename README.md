# TechExam

Setup the TechExam server
---
Set credentials in the AWS credentials profile file on your local system, located at:

    ~/.aws/credentials on Linux, macOS, or Unix

    C:\Users\USERNAME \.aws\credentials on Windows

This file should contain lines in the following format:

    [default]
    aws_access_key_id = your_access_key_id
    aws_secret_access_key = your_secret_access_key

Substitute your own AWS credentials values for the values your_access_key_id and your_secret_access_key.`

Edit the bucket and region values in TechExamModule.java

    private final String bucket = "your bucket";
    private final String region = "your region"; 

Substitute your own AWS values for bucket and region.

How to start the TechExam server
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/tech-exam-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check for TechExam server 
---

To see your applications health enter url `http://localhost:8081/healthcheck`


Asset uploader service Provided by TechExam server
---
The TechExam server provides an S3 asset uploader API as described below:

#####1. The service has an http POST endpoint to upload a new asset.

    POST /asset

    Body: empty

    Sample response
    {
        “upload_url”: <s3-signed-url-for-upload>,
        “id”: <asset-id>
    }

#####2. The user is able to make a POST call to the s3 signed url to upload the asset.

#####3. To mark the upload operation as complete, the service provides a PUT endpoint as follows:

    PUT /asset/<asset-id>

    Body:
    {
        “Status”: “uploaded”
    }

#####4. When a Get request is made on the asset, the service returns a s3 signed url for download with the timeout in seconds as a url parameter. If the timeout is not specified assume 60 seconds.

    GET /asset/<asset-id>?timeout=100

    Sample response
    {
        “Download_url”: <s3-signed-url-for-upload>
    }

A get call made on an asset which has not been set to “status: uploaded” returns
an error.

#####5. The uploaded asset should be fetched successfully using the returned signed url.