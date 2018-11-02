package com.dieterly.datastax.resources;

import com.codahale.metrics.annotation.Timed;
import com.dieterly.datastax.api.GetAssetResponse;
import com.dieterly.datastax.api.MarkUploadAssetCompleteResponse;
import com.dieterly.datastax.api.UploadAssetResponse;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/asset")
@Produces(MediaType.APPLICATION_JSON)
public class AssetResource
{

    private final String accessKeyId;
    private final String accessKey;
    private final String bucket;
    private final String region;

    @Inject
    public AssetResource(@Named("ACCESS_KEY_ID") final String accessKeyId,
                         @Named("ACCESS_KEY") final String accessKey,
                         @Named("BUCKET") final String bucket,
                         @Named("REGION") final String region)
    {
        this.accessKeyId = accessKeyId;
        this.accessKey = accessKey;
        this.bucket = bucket;
        this.region = region;
    }

    @POST
    @Timed
    public UploadAssetResponse uploadAsset()
    {
        return new UploadAssetResponse("a", "b");
    }

    @PUT
    @Path("{assetId}")
    @Timed
    public MarkUploadAssetCompleteResponse markUploadAssetComplete(@PathParam("assetId") String assetId)
    {
        return new MarkUploadAssetCompleteResponse("a");
    }

    @GET
    @Path("{assetId}")
    @Timed
    public GetAssetResponse getAsset(@PathParam("assetId") String assetId)
    {
        return new GetAssetResponse("a");
    }

}
