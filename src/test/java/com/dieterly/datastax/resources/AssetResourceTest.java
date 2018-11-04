package com.dieterly.datastax.resources;

import com.amazonaws.HttpMethod;
import com.dieterly.datastax.api.GetAssetResponse;
import com.dieterly.datastax.api.MarkUploadAssetCompleteResponse;
import com.dieterly.datastax.api.UploadAssetResponse;
import com.dieterly.datastax.aws.AwsClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssetResourceTest
{
    @InjectMocks
    AssetResource assetResource;

    @Mock
    AwsClient awsClient;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void UploadAssetTest()
    {
        UploadAssetResponse r = assetResource.uploadAsset();
        verify(awsClient).createPresignedUrl(any(), anyString(), anyLong());
        verify(awsClient).putObject(anyString(), anyString());
    }

    @Test
    public void markUploadAssetCompleteTest() throws IOException
    {
        when(awsClient.getObject(anyString())).thenReturn("created");
        MarkUploadAssetCompleteResponse r = assetResource.markUploadAssetComplete("1");
        verify(awsClient).getObject(anyString());
        verify(awsClient).putObject(anyString(), anyString());
    }

    @Test
    public void getAssetTest() throws IOException
    {
        when(awsClient.getObject(anyString())).thenReturn("uploaded");
        java.util.Optional<Long> timeout = Optional.of(new Long(100));
        GetAssetResponse r = assetResource.getAsset("1", timeout);
        verify(awsClient).getObject(anyString());
        verify(awsClient).createPresignedUrl(HttpMethod.GET, "1", 100);
    }
}
