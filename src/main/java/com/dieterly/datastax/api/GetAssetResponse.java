package com.dieterly.datastax.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAssetResponse
{
    private String Download_url;

    public GetAssetResponse()
    {
    }

    public GetAssetResponse(final String Download_url)
    {
        this.Download_url = Download_url;
    }

    @JsonProperty
    public String getDownload_url()
    {
        return Download_url;
    }
}
