package com.dieterly.datastax.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MarkUploadAssetCompleteResponse
{
    private String Status;

    public MarkUploadAssetCompleteResponse()
    {
    }

    public MarkUploadAssetCompleteResponse(final String Status)
    {
        this.Status = Status;
    }

    @JsonProperty
    public String getStatus()
    {
        return Status;
    }
}
