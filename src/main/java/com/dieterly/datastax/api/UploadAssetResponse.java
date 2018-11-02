package com.dieterly.datastax.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadAssetResponse
{
    private String upload_url;
    private String id;

    public UploadAssetResponse()
    {
    }

    public UploadAssetResponse(final String upload_url,
                               final String id)
    {
        this.upload_url = upload_url;
        this.id = id;
    }

    @JsonProperty
    public String getUpload_url()
    {
        return upload_url;
    }

    @JsonProperty
    public String getId()
    {
        return id;
    }
}
