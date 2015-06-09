package com.ptoceti.reactive.cache;

import java.io.Serializable;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryRemovedListener;

import org.apache.log4j.Logger;

import com.ptoceti.reactive.model.ObservableItem;

public class ReactCacheEntryRemovedListener implements CacheEntryRemovedListener<String, ObservableItem>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6809717999662900069L;

	/**
     * logger log4J
     */
    private static final Logger LOG = Logger.getLogger(ReactCacheEntryRemovedListener.class);
    
	@Override
	public void onRemoved(
			Iterable<CacheEntryEvent<? extends String, ? extends ObservableItem>> events)
			throws CacheEntryListenerException {
		
		for( CacheEntryEvent<? extends String, ? extends ObservableItem> entry : events){
			LOG.debug("Event type: " + entry.getEventType().toString() + ", key: " + entry.getKey());
		}
		
	}

}
