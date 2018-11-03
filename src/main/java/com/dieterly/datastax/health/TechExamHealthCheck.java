package com.dieterly.datastax.health;

import com.codahale.metrics.health.HealthCheck;

public class TechExamHealthCheck extends HealthCheck
{
    @Override
    protected Result check()
    {
        // TODO: Implement health check

        return Result.healthy();
    }
}
