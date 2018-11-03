package com.dieterly.datastax.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AwsClient
{
    private final static Logger logger = LoggerFactory.getLogger(AwsClient.class);

    private final String bucket;
    private final String region;

    @Inject
    public AwsClient(@Named("BUCKET") final String bucket,
                     @Named("REGION") final String region)
    {
        this.bucket = bucket;
        this.region = region;
    }

    public String createPresignedUrl(final HttpMethod httpMethod,
                                     final String objectKey,
                                     long timeOutSecs)
    {
        try
        {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(region)
                    .build();

            final java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * timeOutSecs;
            expiration.setTime(expTimeMillis);

            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, objectKey)
                    .withMethod(httpMethod)
                    .withExpiration(expiration);

            return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        }
        catch (SdkClientException e)
        {
            logger.error("Failed to create presigned url", e);
            throw e;
        }
    }

    public void putObject(final String objectKey,
                          final String objectVal)
    {

        try
        {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            logger.info("Putting object '{}' with value '{}'", objectKey, objectVal);

            s3Client.putObject(bucket, objectKey, objectVal);

        }
        catch (SdkClientException e)
        {
            logger.error("Failed to put object", e);
            throw e;
        }
    }

    public String getObject(final String objKey) throws IOException
    {
        try
        {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            logger.info("Getting object '{}'", objKey);
            S3Object fullObject = s3Client.getObject(new GetObjectRequest(bucket, objKey));
            logger.info("Content-Type: " + fullObject.getObjectMetadata().getContentType());
            logger.info("Content: " + fullObject.getObjectContent().toString());

            BufferedReader br = new BufferedReader(new InputStreamReader(fullObject.getObjectContent()));
            return br.readLine();
        }
        catch (SdkClientException | IOException e)
        {
            logger.error("Failed to get object", e);
            throw e;
        }

    }
}
