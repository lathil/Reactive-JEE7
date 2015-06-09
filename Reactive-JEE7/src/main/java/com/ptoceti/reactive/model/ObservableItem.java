package com.ptoceti.reactive.model;

import rx.subjects.BehaviorSubject;

/**
 * Represent an observable item
 * 
 * @author LATHIL
 *
 */
public class ObservableItem extends Item {
	
	/**
	 * The observable binded to the Item.
	 */
	private BehaviorSubject<Item> observable;
	
	/**
	 * Constructor. Create the Item ans associated observable.
	 * 
	 * @param name 
	 * @param value
	 */
	public ObservableItem(String name, String value) {
		super( name, value);
		
		observable = BehaviorSubject.create(this.clone());
	}

	/**
	 * Return the associated observable.
	 * 
	 * @return observable.
	 */
	public BehaviorSubject<? extends Item> getObservable() {
		return observable;
	}

	/**
	 * Set a value and notify observers.
	 * 
	 * @param value new value for the item.
	 */
	@Override
	public void setValue(String value){
		super.setValue(value);
		observable.onNext(this.clone());
	}
}
