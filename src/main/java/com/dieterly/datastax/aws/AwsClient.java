package com.dieterly.datastax.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public String getPresignedUrl(final HttpMethod httpMethod,
                                  final String objectKey,
                                  long timeOutSecs)
    {
        try
        {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(region)
                    .build();

            // Set the pre-signed URL to expire after one hour.
            final java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * timeOutSecs;
            expiration.setTime(expTimeMillis);

            // Generate the pre-signed URL.
            logger.info("Generating pre-signed URL.");
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, objectKey)
                    .withMethod(httpMethod)
                    .withExpiration(expiration);
            return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        }
        catch (SdkClientException e)
        {
            logger.error("Failure", e);
            throw e;
        }
    }
}
