package com.dieterly.datastax.aws;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class AwsClientTest
{
    @InjectMocks
    AwsClient awsClient;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createPresignedUrlTest()
    {

    }

    @Test
    public void putObjectTest()
    {

    }

    @Test
    public void getObjectTest()
    {

    }
}
