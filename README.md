# TechExam

Setup the TechExam application
---
Set credentials in the AWS credentials profile file on your local system, located at:

    ~/.aws/credentials on Linux, macOS, or Unix

    C:\Users\USERNAME \.aws\credentials on Windows

This file should contain lines in the following format:

    [default]
    aws_access_key_id = your_access_key_id
    aws_secret_access_key = your_secret_access_key

Substitute your own AWS credentials values for the values your_access_key_id and your_secret_access_key.`

How to start the TechExam application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/tech-exam-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
