package com.ptoceti.reactive;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.cache.Cache;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;

import org.apache.log4j.Logger;

import rx.Subscription;

import com.ptoceti.reactive.model.ItemSubscriber;
import com.ptoceti.reactive.model.ObservableItem;

/**
 * Message handler. Decode request messages. Create or delete observers subscription on observable item.
 * Handle update of values on observable items.
 * 
 * Manage storage of observable items in a cache.
 * @author LATHIL
 *
 */
@ApplicationScoped
public class ReactHandler {

	private static final String SESSIONSUBSCRIBER = "sessionsubscriber";
	private static final String SESSIONSUBSCRIPTIONSID = "sessionsubscriptionsid";
	
	/**
     * logger log4J
     */
    private static final Logger LOG = Logger.getLogger(ReactHandler.class);
	
	/*
	 * the cache in which observable items are stored.
	 */
	@Inject
	Cache<String, ObservableItem> cache;
	
	/**
	 * Handle a message coming from a client.
	 * 
	 * @param session
	 * @param message
	 * @return
	 */
	ReactMessage handle( Session session, ReactMessage message){
		
		ReactMessage response = null;
		ObservableItem observableItem;
		ItemSubscriber subscriber;
		Map<String, Subscription> subscriptions;
		
		switch(message.getMessage()) {
		
			// Client want to subscribe to an item. 
			case "subscribe":
				// if item does not exists in cache, create it.
				
				LOG.debug("Server subscribing object id: " + message.getObjectId());
				boolean storedInCache = cache.putIfAbsent(message.getObjectId(), new ObservableItem( message.getObjectId(), message.getObjectValue()));
				if( storedInCache){
					LOG.debug("Server stored object id: " + message.getObjectId() + " cache: " + cache.toString() + " name: " + cache.getName());
				}
				observableItem = cache.get(message.getObjectId());
				// get the subscriber for the session
				subscriber =  getItemSubscribers(session);
				if( subscriber == null ){
					// if it does not exist, create it.
					subscriber = new ItemSubscriber(session);
					session.getUserProperties().put("SESSIONSUBSCRIBER", subscriber);
				}
				// get list of subscriptions for the session
				subscriptions = getSubscriptions(session);
				if(subscriptions.get(message.getObjectId()) == null ){
					// if there is not yet a subscription for the item id, create it. The subscription
					// is the fact of subscribing the subscriber (ItemSubscriber) to the observable (ObservableItem).
					Subscription subscription = observableItem.getObservable().subscribe(subscriber);
					// keep track of the subscription for later unsubscribing 
					subscriptions.put(message.getObjectId(), subscription);
				}
				
				response = new ReactMessage(message.getId(), "ok");
				
				break;
			
			// client want to unsubscribe from an item
			case "unsubscribe":
				// get item from cache
				observableItem = cache.get(message.getObjectId());
				// get session subscriber
				subscriber = getItemSubscribers(session);
				// get subscriptions
				subscriptions = getSubscriptions(session);
				if( observableItem != null && subscriber != null && subscriptions != null) {
					// if ok, 
					Subscription subscription = subscriptions.get(message.getObjectId());
					if(subscription != null){
						// unsubscribe ..
						subscription.unsubscribe();
						// and remove from list
						subscriptions.remove(message.getObjectId());
					}
				}
				// tell client everything is ok
				response = new ReactMessage(message.getId(), "ok");
				
				break;
			
			// Client want to update the item.
			case "update":
				// get observable from cache
				LOG.debug("Server updating object id: " + message.getObjectId());
				observableItem = cache.get(message.getObjectId());
				if(observableItem != null) {
					LOG.debug("Server updating item in cache");
					// and set value.
					observableItem.setValue(message.getObjectValue());
				} else {
					LOG.debug("Server update item not possible: item not in cache !" + " cache: " + cache.toString() + " name: " + cache.getName());
				}
				// tell client everything is ok
				response = new ReactMessage(message.getId(), "ok");
				
				break;
		
			
		}
		return response;
	}
	
	/**
	 * Close handler. Clear subscriber from all subscriptions
	 * 
	 */
	public void close(Session session){
		// clear first all subscriptions
		Map<String, Subscription> subscribtions = getSubscriptions(session);
		for( Entry<String, Subscription> entry : subscribtions.entrySet()){
			entry.getValue().unsubscribe();
		}
		
		// then clear subscriber
		ItemSubscriber subscriber =  getItemSubscribers(session);
		if( subscriber != null ){
			subscriber.unsubscribe();
		}
		
	}
	
	/**
	 * Return the subscriber for the session. There can be only only susbscriber per session, but it can subscribe to several item.
	 * 
	 * @param session the session
	 * @return ItemSubscriber the associated subscriber.
	 */
	ItemSubscriber getItemSubscribers(Session session) {
		return (ItemSubscriber)session.getUserProperties().get("SESSIONSUBSCRIBER");
	}
	
	/**
	 * Return the subscription to different item for a session.
	 * 
	 * @param session the session 
	 * @return a hash map of subscriptions. Key is message is, val is the subscription object.
	 */
	@SuppressWarnings("unchecked")
	Map<String, Subscription> getSubscriptions(Session session){
		
		Map<String, Subscription> subscriptions = (Map<String, Subscription>) session.getUserProperties().get("SESSIONSUBSCRIPTIONSID");
		if( subscriptions == null){
			subscriptions = new HashMap<String, Subscription>();
			session.getUserProperties().put("SESSIONSUBSCRIPTIONSID", subscriptions);
		}
		
		return subscriptions;
	}
}
