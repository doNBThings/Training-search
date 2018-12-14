package com.dothings.training.config;


import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

/**
 * Created by Angela on 2017/4/20.
 */
@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
  //      packages("www.fei.com.aliyun.rest");
    }


    @Resource
    private Environment environment;

    @PostConstruct
    public void init() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Path.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
        registerClasses(scanner.findCandidateComponents(environment.getProperty("base.package")).stream()
                .map(beanDefinition -> ClassUtils.resolveClassName(beanDefinition.getBeanClassName(), this.getClassLoader()))
                .collect(Collectors.toSet()));
     //  packages(environment.getProperty("base.package"));
        register(JacksonJsonProvider.class);
        register(MultiPartFeature.class);

        if(environment.getProperty("swagger.init",Boolean.class)){
            register(ApiListingResource.class);
            register(SwaggerSerializers.class);
            register(MultiPartFeature.class);
            initSwagger();
        }
    }

    private void initSwagger(){
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion(environment.getProperty("swagger.version"));
        beanConfig.setTitle(environment.getProperty("swagger.title"));
        beanConfig.setHost(environment.getProperty("swagger.host"));
        beanConfig.setBasePath(environment.getProperty("swagger.basePath"));
        beanConfig.setResourcePackage(environment.getProperty("swagger.resourcePackage"));
        beanConfig.setScan(true);
    }



}