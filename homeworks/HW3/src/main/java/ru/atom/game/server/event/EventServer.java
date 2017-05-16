package ru.atom.game.server.event;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.atom.game.server.CrossBrowserFilter;

public class EventServer {
    public static Server server;

    public static void start() {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8089);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        context.setContextPath("/");
        contexts.setHandlers(new Handler[]{ context });
        server.setHandler(contexts);

        // Add a websocket to a specific path spec
        ServletHolder holderEvents = new ServletHolder("ws-events", EventServlet.class);
        context.addServlet(holderEvents, "/events/*");

        try {
            server.start();
            server.dump(System.err);
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        start();
    }
}
