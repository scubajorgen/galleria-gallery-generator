/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.gallerygenerator;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
/**
 *
 * @author jorgen
 */
@Configuration
//@EnableAutoConfiguration
public class BeanConfiguration
{
    //@Autowired
    ObjectMapper objectMapper;
    
    @Bean
    public ObjectMapper getObjectMapper()
    {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); 
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }
    
    @Bean
    public ObjectWriter getObjectWriter()
    {
        return objectMapper.writer(new JsonPrettyPrinter());
    }
    

}
