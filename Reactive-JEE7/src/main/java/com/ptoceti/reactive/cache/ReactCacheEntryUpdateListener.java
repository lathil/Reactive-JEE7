package com.ptoceti.reactive.cache;

import java.io.Serializable;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.log4j.Logger;

import com.ptoceti.reactive.model.ObservableItem;

public class ReactCacheEntryUpdateListener implements CacheEntryUpdatedListener<String, ObservableItem>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4313819874746146886L;

	/**
     * logger log4J
     */
    private static final Logger LOG = Logger.getLogger(ReactCacheEntryUpdateListener.class);
    
	@Override
	public void onUpdated(
			Iterable<CacheEntryEvent<? extends String, ? extends ObservableItem>> events)
			throws CacheEntryListenerException {
		for( CacheEntryEvent<? extends String, ? extends ObservableItem> entry : events){
			LOG.debug("Event type: " + entry.getEventType().toString() + ", key: " + entry.getKey());
		}
	}

}
