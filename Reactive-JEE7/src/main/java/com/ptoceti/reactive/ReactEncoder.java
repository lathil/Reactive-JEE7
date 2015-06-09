package com.ptoceti.reactive;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReactEncoder implements Encoder.Text<ReactMessage>{

	private ObjectMapper objectMapper;
	
	public void init(EndpointConfig config) {
		
		if( objectMapper == null) { 
			
			System.out.println("ReactEncoder: configuring objectMapper.");
			JsonFactory factory = new JsonFactory();
			factory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
			objectMapper =  new ObjectMapper(factory);
			objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public String encode(ReactMessage object) throws EncodeException {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonParseException e) {
			throw new EncodeException( object, e.getMessage(), e );
		} catch (JsonMappingException e) {
			throw new EncodeException( object, e.getMessage(), e );
		} catch (IOException e) {
			throw new EncodeException( object, e.getMessage(), e );
		}
	}

}
