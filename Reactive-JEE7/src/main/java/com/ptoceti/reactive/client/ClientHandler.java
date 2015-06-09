package com.ptoceti.reactive.client;

import com.ptoceti.reactive.ReactMessage;

public interface ClientHandler {

	void handle(ReactMessage message);
}
