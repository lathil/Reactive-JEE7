package com.ptoceti.reactive;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.Session;

import org.apache.log4j.Logger;

/**
 * Front end server websocket endpoint. Handle notification on connection open, close and message or error notifications.
 * 
 * 
 * 
 * @author LATHIL
 *
 */
@ServerEndpoint(value = "/reactive", encoders = {ReactEncoder.class}, decoders = {ReactDecoder.class})
public class ReactiveEndPoint  {
	
	/**
     * logger log4J
     */
    private static final Logger LOG = Logger.getLogger(ReactiveEndPoint.class);
    
	Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	
	@Inject
	ReactHandler handler;

	/**
	 * Connection open. We keep track of the session.
	 * 
	 * @param session the websocket session.
	 */
	@OnOpen
	public void OnOpen(Session session){
		sessions.add(session);
		
		LOG.debug("Server session opened. Id: " + session.getId());
	}
	
	/**
	 * A message has arrived. Message handler is notified and a message ok is sent back to sender through the connection.
	 * 
	 * @param session the websocket connection
	 * @param message the input message
	 */
	@OnMessage
	public void OnMessage(Session session, ReactMessage message){

		LOG.debug("Server message received. Session Id: " + session.getId() + ", message type: " + message.getMessage());
		
		ReactMessage response = handler.handle(session, message);
		if( response == null){
			// if response from handler is null, send a not ok message.
			response = new ReactMessage(message.getId(), "nok");
		}
		// send a response using the async interface.
		session.getAsyncRemote().sendObject(response);
	}
	
	/**
	 * Error notification.
	 * 
	 * @param session the session on which the erroe has arrived.
	 * @param error the error.
	 */
	@OnError
	public void OnError(Session session, Throwable error){
		LOG.debug("Server error  " + error.toString());
		
	}
	
	/**
	 * Session close notification. The session is removed from the list of recognized sessions.
	 * @param session the session that is to be closed.
	 * @param reason why
	 */
	@OnClose
	public void OnClose(Session session, CloseReason reason){
		LOG.debug("Session closed. Id: " + session.getId());
		handler.close(session);
		sessions.remove(session);
	}
}
