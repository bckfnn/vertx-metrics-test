package io.github.bckfnn;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.dropwizard.impl.AbstractMetrics;

public class MetricsTest extends AbstractVerticle {

    public void start() {
        MetricsService metricsService = MetricsService.create(vertx);
        JsonObject metrics = metricsService.getMetricsSnapshot(vertx);
        System.out.println("metrics json in verticle:" + metrics);
        
        System.out.println(vertx.getClass().getClassLoader());
        System.out.println(AbstractMetrics.class.getClassLoader());
    }
    

    public static void main(String[] args) {
        VertxOptions vertxOptions = new VertxOptions();

        DropwizardMetricsOptions opt = new DropwizardMetricsOptions();
        opt.setEnabled(true);
        opt.setRegistryName("vertxRegistry");
        vertxOptions.setMetricsOptions(opt);

        Vertx vertx = Vertx.vertx(vertxOptions);
        
        DeploymentOptions opts = new DeploymentOptions();
        opts.setInstances(1);
        opts.setRedeploy(true);

        MetricsService metricsService = MetricsService.create(vertx);
        JsonObject metrics = metricsService.getMetricsSnapshot(vertx);
        
        System.out.println("metrics json in main:" + metrics);
        
        vertx.deployVerticle(MetricsTest.class.getName(), opts, res -> {
            if (res.succeeded()) {
                System.out.println("Deployed " + res.result());
            } else {
                System.out.println("Deploy failed " + res.cause());
            }
        });
    }
}

