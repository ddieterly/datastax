package com.dieterly.datastax;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechExamModule extends AbstractModule
{
    private Logger logger = LoggerFactory.getLogger(TechExamModule.class);

    private final String bucket = "ddieterly-bucket-0";
    private final String region = "us-west-1";

    @Override
    protected void configure()
    {

        bind(String.class)
                .annotatedWith(Names.named("BUCKET"))
                .toInstance(bucket);

        bind(String.class)
                .annotatedWith(Names.named("REGION"))
                .toInstance(region);
    }
}
