package dk.bot.webconsole;

import java.io.File;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * Starts betting console web server.
 * 
 * @author daniel
 * 
 */
public class BettingConsoleStart {

	public BettingConsoleStart(int httpServerPort) throws Exception {

		Server server = new Server();
		SocketConnector connector = new SocketConnector();
		// Set some timeout options to make debugging easier.
		connector.setMaxIdleTime(1000 * 60 * 60);
		connector.setSoLingerTime(-1);
		connector.setPort(httpServerPort);
		server.setConnectors(new Connector[] { connector });

		WebAppContext bb = new WebAppContext();
		bb.setServer(server);
		bb.setContextPath("/");
		bb.setResourceBase(new File(BettingConsoleStart.class.getResource("BettingConsoleStart.class").toString()).getParent()
				.replace("\\", "/")
				+ "/");
		server.addHandler(bb);
		server.start();

	}
}
