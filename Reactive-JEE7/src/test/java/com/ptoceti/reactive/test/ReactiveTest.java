package com.ptoceti.reactive.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.Decoder;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.log4j.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.gradle.archive.importer.embedded.EmbeddedGradleImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ptoceti.reactive.ReactDecoder;
import com.ptoceti.reactive.ReactHandler;
import com.ptoceti.reactive.ReactMessage;
import com.ptoceti.reactive.ReactEncoder;
import com.ptoceti.reactive.client.ClientEndPoint;
import com.ptoceti.reactive.client.ClientHandler;

@RunWith(Arquillian.class)
public class ReactiveTest implements ClientHandler {

	ClientEndPoint clientEndpoint;
	
	boolean messageOk = false;
	
	/**
     * logger log4J
     */
    private static final Logger LOG = Logger.getLogger(ReactiveTest.class);
	
	@Before
	public void setUp() {

		try {
			
			clientEndpoint = new ClientEndPoint(this);
			URI serverEndPointUri = new URI("ws://localhost:8080/Reactive-JEE7/reactive");
			WebSocketContainer wbContainer = ContainerProvider.getWebSocketContainer();
			List<Class<? extends Encoder>> encoders = new ArrayList<Class<? extends Encoder>>();
			encoders.add(ReactEncoder.class);
			List<Class<? extends Decoder>> decoders = new ArrayList<Class<? extends Decoder>>();
			decoders.add(ReactDecoder.class);
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().encoders(encoders).decoders(decoders).build();
			wbContainer.connectToServer(clientEndpoint, config,serverEndPointUri);
			
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeploymentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void mocktest() throws IOException, EncodeException, InterruptedException {

		ReactMessage messageSubscribe = new ReactMessage("01", "subscribe");
		
		messageSubscribe.setObjectId("1");
		messageSubscribe.setObjectValue("0");
		
		clientEndpoint.sendMessage(messageSubscribe);
		LOG.debug("Test client: message subscribe sent !");
		
		// wait 5 seconds for messages from websocket
		while( !messageOk){
			Thread.sleep(10000);
		}
		
		messageOk = false;
		ReactMessage messageUpdate = new ReactMessage("01", "update");
		messageUpdate.setObjectId("1");
		messageUpdate.setObjectValue("1");
		
		clientEndpoint.sendMessage(messageUpdate);
		LOG.debug("Test client: message update sent !");
		
		// wait 5 seconds for messages from websocket
		while( !messageOk){
			Thread.sleep(10000);
		}
		

	}

	@Override
	public void handle( ReactMessage message) {
		if( message.getMessage().equals("ok")){
				messageOk = true;
		}
	}
	
	/**
	 * Test case must be deploy in container as we need an underlaying websocket implementation (testabme=true).
	 * 
	 * @return WebArchive the deployment archive as a war
	 */
	@Deployment(name = "War_Deployment", testable = true)
	public static WebArchive createDeployment() {

		final WebArchive webArch = ShrinkWrap
				.create(EmbeddedGradleImporter.class).forThisProjectDirectory().forTasks("war").importBuildOutput().as(WebArchive.class);
		// from
		// https://github.com/mmatloka/arquillian-gradle-sample/issues/2#issuecomment-59104563
		System.setProperty("javax.xml.parsers.SAXParserFactory",
				"__redirected.__SAXParserFactory");

		return webArch;
	}
	
	@After
	public void tearDown() throws IOException {
		LOG.debug("Test: cleaning client end point connections.");
		clientEndpoint.close();
	}

}
