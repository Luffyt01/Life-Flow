package com.project.user_service.config;

import com.project.user_service.dto.PointDTO;
import com.project.user_service.utils.GeometryUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Configuration
public class MapperConfig  {
       @Bean
    public ModelMapper modelMapper(){
           ModelMapper mapper =  new ModelMapper();

           mapper.typeMap(PointDTO.class, Point.class).setConverter(context -> {
               PointDTO pointDTO = context.getSource();
               return GeometryUtil.createPoint(pointDTO);
           });
           mapper.typeMap(Point.class, PointDTO.class).setConverter(context ->{
               Point point = context.getSource();
               double[] coordinates = {
                       point.getX(),
                       point.getY()
               };
               return new PointDTO(coordinates);
           });
           return mapper;
    }

}
