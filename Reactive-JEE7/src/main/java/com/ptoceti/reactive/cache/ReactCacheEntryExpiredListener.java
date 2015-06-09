package com.ptoceti.reactive.cache;

import java.io.Serializable;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryExpiredListener;
import javax.cache.event.CacheEntryListenerException;

import org.apache.log4j.Logger;

import com.ptoceti.reactive.model.ObservableItem;

public class ReactCacheEntryExpiredListener implements CacheEntryExpiredListener<String, ObservableItem>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4145715293764933854L;
	
	/**
     * logger log4J
     */
    private static final Logger LOG = Logger.getLogger(ReactCacheEntryExpiredListener.class);

	@Override
	public void onExpired(
			Iterable<CacheEntryEvent<? extends String, ? extends ObservableItem>> events)
			throws CacheEntryListenerException {
		for( CacheEntryEvent<? extends String, ? extends ObservableItem> entry : events){
			LOG.debug("Event type: " + entry.getEventType().toString() + ", key: " + entry.getKey());
		}
	}

}
