package com.ptoceti.reactive.client;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.ptoceti.reactive.ReactMessage;

public class ClientEndPoint extends Endpoint{

	/**
     * logger log4J
     */
    private static final Logger LOG = Logger.getLogger(ClientEndPoint.class);
    
	Session session;
	ClientHandler handler;
	
	public ClientEndPoint(ClientHandler handler){
		this.handler = handler;
	}
	
	
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		this.session = session;
		
		session.addMessageHandler(new MessageHandler.Whole<ReactMessage>() {

			@Override
			public void onMessage(ReactMessage message) {
				LOG.debug("Client received response. Message: " + message.getMessage());
				handler.handle( message);
			}
		});
		
		LOG.debug("Client end point connected");
	}

	public void sendMessage(ReactMessage message) throws IOException, EncodeException{
		session.getBasicRemote().sendObject(message);
	}
	
	public void close() throws IOException{
		LOG.debug("Client session closed.");
		session.close();
		session = null;
	}
	
}
