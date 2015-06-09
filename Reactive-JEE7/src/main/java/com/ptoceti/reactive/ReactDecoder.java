package com.ptoceti.reactive;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReactDecoder implements Decoder.Text<ReactMessage>{

	private ObjectMapper objectMapper;
	 
	public void init(EndpointConfig config) {
		
		if( objectMapper == null) { 
			JsonFactory factory = new JsonFactory();
			factory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
			objectMapper =  new ObjectMapper(factory);
			objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public ReactMessage decode(String s) throws DecodeException {
		try {
			return objectMapper.readValue(s, ReactMessage.class);
		} catch (JsonParseException e) {
			throw new DecodeException( s, e.getMessage(), e );
		} catch (JsonMappingException e) {
			throw new DecodeException( s, e.getMessage(), e );
		} catch (IOException e) {
			throw new DecodeException( s, e.getMessage(), e );
		}
	}

	public boolean willDecode(String s) {
		return true;
	}

}
