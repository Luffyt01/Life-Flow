package com.life_flow.geolocation_service.config;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeoConfig {

    @Bean
    public GeometryFactory geometryFactory() {
        // SRID 4326 is a common standard for geographic coordinates (WGS 84)
        return new GeometryFactory(new PrecisionModel(), 4326);
    }
}
