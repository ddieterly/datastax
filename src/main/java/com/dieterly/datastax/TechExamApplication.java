package com.dieterly.datastax;

import com.codahale.metrics.health.HealthCheck;
import com.dieterly.datastax.health.TechExamHealthCheck;
import com.dieterly.datastax.resources.AssetResource;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechExamApplication extends Application<TechExamConfiguration>
{

    private Logger logger = LoggerFactory.getLogger(TechExamApplication.class);

    private final AssetResource assetResource;
    private final HealthCheck techExamHealthCheck;

    public static void main(final String[] args) throws Exception
    {

        Injector injector = Guice.createInjector(new TechExamModule());

        TechExamApplication techExamApplication = injector.getInstance(TechExamApplication.class);

        techExamApplication.run(args);
    }

    @Inject
    private TechExamApplication(final AssetResource assetResource,
                                final TechExamHealthCheck techExamHealthCheck)
    {
        this.assetResource = assetResource;
        this.techExamHealthCheck = techExamHealthCheck;
    }

    @Override
    public String getName()
    {
        return "TechExam";
    }

    @Override
    public void initialize(final Bootstrap<TechExamConfiguration> bootstrap)
    {
        // TODO: application initialization
    }

    @Override
    public void run(final TechExamConfiguration configuration,
                    final Environment environment)
    {

        environment.jersey().register(assetResource);
        environment.healthChecks().register("TechExamHealth", techExamHealthCheck);

    }

}
