package com.project.user_service.config;

import com.project.user_service.dto.PointDTO;
import com.project.user_service.utils.GeometryUtil;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class MapperConfig  {
       @Bean
    public ModelMapper modelMapper(){
           log.info("Initializing ModelMapper bean");
           ModelMapper mapper =  new ModelMapper();

           mapper.typeMap(PointDTO.class, Point.class).setConverter(context -> {
               PointDTO pointDTO = context.getSource();
               log.debug("Converting PointDTO to Point: {}", pointDTO);
               if (pointDTO == null || pointDTO.getCoordinates() == null) {
                   return null;
               }
               return GeometryUtil.createPoint(pointDTO);
           });
           mapper.typeMap(Point.class, PointDTO.class).setConverter(context ->{
               Point point = context.getSource();
               log.debug("Converting Point to PointDTO: {}", point);
               if (point == null) {
                   return null;
               }
               double[] coordinates = {
                       point.getX(),
                       point.getY()
               };
               return new PointDTO(coordinates);
           });
           
           log.info("ModelMapper bean initialized successfully");
           return mapper;
    }

}
