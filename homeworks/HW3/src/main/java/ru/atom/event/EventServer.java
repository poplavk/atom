package ru.atom.event;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.atom.dbhackaton.server.CrossBrowserFilter;

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
        context.setContextPath("/");
        server.setHandler(context);

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


//    private static ServletContextHandler createGsContext() {
//        ServletContextHandler context = new ServletContextHandler();
//        context.setContextPath("/");
//        ServletHolder jerseyServlet = context.addServlet(
//                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
//        jerseyServlet.setInitOrder(0);
//
//        jerseyServlet.setInitParameter(
//                "jersey.config.server.provider.packages",
//                "ru.atom.bombergirl.gameserver"
//        );
//
//        jerseyServlet.setInitParameter(
//                "com.sun.jersey.spi.container.ContainerResponseFilters",
//                CrossBrowserFilter.class.getCanonicalName()
//        );
//
//        return context;
//    }
//
//    private static ContextHandler createResourceContext() {
//        ContextHandler context = new ContextHandler();
//        context.setContextPath("/gs/0/*");
//        ResourceHandler handler = new ResourceHandler();
//        String eventRoot = EventServer.class.getResource("/static").toString();
//        String serverRoot = eventRoot.substring(0, eventRoot.length() - 35) + "frontend/src/main/webapp";
//        handler.setWelcomeFiles(new String[]{serverRoot + "index.html"});
//        handler.setResourceBase(serverRoot);
//        context.setHandler(handler);
//        return context;
//    }

    public static void main(String[] args) {
        start();
    }
}
