package com.ptoceti.reactive.cache;

import java.io.Serializable;

import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;

import org.apache.log4j.Logger;

import com.ptoceti.reactive.model.ObservableItem;

public class ReactCacheEntryCreateListener implements CacheEntryCreatedListener<String, ObservableItem>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4923696301707461176L;
	
	/**
     * logger log4J
     */
    private static final Logger LOG = Logger.getLogger(ReactCacheEntryCreateListener.class);

	@Override
	public void onCreated(
			Iterable<CacheEntryEvent<? extends String, ? extends ObservableItem>> events)
			throws CacheEntryListenerException {
		for( CacheEntryEvent<? extends String, ? extends ObservableItem> entry : events){
			LOG.debug("Event type: " + entry.getEventType().toString() + ", key: " + entry.getKey());
		}
	}

}
