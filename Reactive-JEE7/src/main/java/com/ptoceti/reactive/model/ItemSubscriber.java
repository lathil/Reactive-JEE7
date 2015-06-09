package com.ptoceti.reactive.model;

import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.ptoceti.reactive.ReactHandler;
import com.ptoceti.reactive.ReactMessage;

import rx.Subscriber;

/**
 * This subscriber is in fact a observer over an ObservableItem observable.
 * ObservableItem send notification to onNext when a new value is set.
 * 
 * 
 * @author LATHIL
 *
 */
public class ItemSubscriber extends Subscriber<Item> {
	
	/**
     * logger log4J
     */
    private static final Logger LOG = Logger.getLogger(ItemSubscriber.class);

	Session session;
	public ItemSubscriber(Session session){
		this.session = session;
	}
	
	@Override
	public void onCompleted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Throwable e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Notify here that the observable item has had a value change.
	 */
	@Override
	public void onNext(Item t) {
		
		ReactMessage notificationMessage = new ReactMessage("XX", "changed");
		notificationMessage.setObjectId(t.getName());
		notificationMessage.setObjectValue(t.getValue());
		
		LOG.debug("Cient sessionID: " + session.getId() + " Item name: " + t.getName() + " changed value : " + t.getValue());
		
		session.getAsyncRemote().sendObject(notificationMessage);
	}

}
