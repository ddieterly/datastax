package com.dieterly.datastax.resources;

import com.amazonaws.HttpMethod;
import com.codahale.metrics.annotation.Timed;
import com.dieterly.datastax.api.GetAssetResponse;
import com.dieterly.datastax.api.MarkUploadAssetCompleteResponse;
import com.dieterly.datastax.api.UploadAssetResponse;
import com.dieterly.datastax.aws.AwsClient;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/asset")
@Produces(MediaType.APPLICATION_JSON)
public class AssetResource
{

    private Logger logger = LoggerFactory.getLogger(AssetResource.class);

    private final AwsClient awsClient;

    private final AtomicLong objectCounter = new AtomicLong();
    private final String hostId;
    private final String pid;

    private final String METADATA_ID_PREFIX = "metadata-id-prefix";
    private final String CREATED_STATUS = "created";
    private final String UPLOADED_STATUS = "uploaded";

    private final Long DEFAULT_TIMEOUT_SECONDS = new Long(60 * 60); // 1 hour

    @Inject
    public AssetResource(AwsClient awsClient) throws SocketException
    {
        this.awsClient = awsClient;

        this.pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        logger.info("Using pid {}: ", pid);

        this.hostId = createHostId();
        logger.info("Using host id {}: ", hostId);

    }

    @POST
    @Timed
    public UploadAssetResponse uploadAsset()
    {
        String assetId = createAssetId();
        String url = awsClient.createPresignedUrl(HttpMethod.PUT, assetId, DEFAULT_TIMEOUT_SECONDS);

        awsClient.putObject(createAssetMetadatatId(assetId), CREATED_STATUS);

        return new UploadAssetResponse(url, assetId);
    }

    @PUT
    @Path("{assetId}")
    @Timed
    public MarkUploadAssetCompleteResponse markUploadAssetComplete(@PathParam("assetId") String assetId) throws IOException
    {
        String assetStatus = awsClient.getObject(createAssetMetadatatId(assetId));
        if (!assetStatus.equals(CREATED_STATUS))
        {
            final String msg = String.format("Asset '%s' does not exist or has not been marked as 'created'", assetId);
            throw new WebApplicationException(msg, Response.Status.NOT_FOUND);
        }

        awsClient.putObject(createAssetMetadatatId(assetId), UPLOADED_STATUS);

        return new MarkUploadAssetCompleteResponse(UPLOADED_STATUS);
    }

    @GET
    @Path("{assetId}")
    @Timed
    public GetAssetResponse getAsset(@PathParam("assetId") String assetId,
                                     @QueryParam("timeout") Optional<Long> timeout) throws IOException
    {
        String assetStatus = awsClient.getObject(createAssetMetadatatId(assetId));
        if (!assetStatus.equals(UPLOADED_STATUS))
        {
            final String msg = String.format("Asset '%s' does not exist or has not been marked as 'uploaded'", assetId);
            throw new WebApplicationException(msg, Response.Status.NOT_FOUND);
        }

        String url = awsClient.createPresignedUrl(HttpMethod.GET,
                assetId,
                timeout.orElse(DEFAULT_TIMEOUT_SECONDS));
        return new GetAssetResponse(url);
    }

    /**
     * Create unique object id across all possible instances of tech-exam.
     *
     * @return objectId
     */
    private String createAssetId()
    {
        String id = hostId +
                pid +
                System.currentTimeMillis() +
                String.valueOf(objectCounter.incrementAndGet());
        return Hashing.sha256().hashString(id, Charsets.UTF_8).toString();
    }

    /**
     * Create unique object id to store meta data about asset ids.
     *
     * @param assetId
     * @return assetMetadataId
     */
    private String createAssetMetadatatId(String assetId)
    {
        String metaId = METADATA_ID_PREFIX + assetId;
        return Hashing.sha256().hashString(metaId, Charsets.UTF_8).toString();
    }

    /**
     * Concatenate all ip addresses across all network interfaces for this host to make a pseudo unique host id.
     *
     * @return hostId
     * @throws SocketException
     */
    private String createHostId() throws SocketException
    {
        final StringBuilder sb = new StringBuilder();
        final Enumeration en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements())
        {
            final NetworkInterface ni = (NetworkInterface) en.nextElement();
            final Enumeration ee = ni.getInetAddresses();
            while (ee.hasMoreElements())
            {
                final InetAddress ia = (InetAddress) ee.nextElement();
                final String ipAddress = ia.getHostAddress();
                if (!ipAddress.equals("127.0.0.1"))
                {
                    logger.info("Found usable ip address {}: ", ipAddress);
                    sb.append(ipAddress);
                }
            }
        }

        return sb.toString();
    }

}
