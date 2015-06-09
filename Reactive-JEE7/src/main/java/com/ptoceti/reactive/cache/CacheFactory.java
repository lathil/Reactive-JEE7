package com.ptoceti.reactive.cache;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.ptoceti.reactive.model.ObservableItem;

@ApplicationScoped
public class CacheFactory {


	@Produces
	Cache<String, ObservableItem> getCache() {
		Cache<String, ObservableItem> cache =  Caching.getCachingProvider().getCacheManager().getCache("react");
		
		cache.registerCacheEntryListener(
				   (CacheEntryListenerConfiguration<String, ObservableItem>) new MutableCacheEntryListenerConfiguration<String, ObservableItem>
				   (FactoryBuilder.factoryOf(new ReactCacheEntryCreateListener()),null,false,false));
		cache.registerCacheEntryListener(
				   (CacheEntryListenerConfiguration<String, ObservableItem>) new MutableCacheEntryListenerConfiguration<String, ObservableItem>
				   (FactoryBuilder.factoryOf(new ReactCacheEntryExpiredListener()),null,false,false));
		cache.registerCacheEntryListener(
				   (CacheEntryListenerConfiguration<String, ObservableItem>) new MutableCacheEntryListenerConfiguration<String, ObservableItem>
				   (FactoryBuilder.factoryOf(new ReactCacheEntryRemovedListener()),null,false,false));
		cache.registerCacheEntryListener(
				   (CacheEntryListenerConfiguration<String, ObservableItem>) new MutableCacheEntryListenerConfiguration<String, ObservableItem>
				   (FactoryBuilder.factoryOf(new ReactCacheEntryUpdateListener()),null,false,false));
		
		return cache;
	}
}
