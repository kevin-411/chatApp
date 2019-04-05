package webServletLogic;

import model.Message;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(
        value="/chat/{from}/{to}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class)
public class ServerEndPoint {
    private Session session;

    private static Set<ServerEndPoint> chatEndpointSet = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();
    private static Set<ServerEndPoint> chatEndpointIndividual = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> individual = new HashMap<>();
    public String recepient = "all";
    public String currentUser = "";

    @OnOpen
    public void onOpen(Session session, @PathParam("from") String from, @PathParam("to") String to) throws IOException, EncodeException {
        this.session = session;
        chatEndpointSet.add(this);
        currentUser = from;
        users.put(session.getId(), from);
        Message message = new Message();
        message.setFrom(from);
        message.setContent("Connected");
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message, @PathParam("from") String from, @PathParam("to") String to) throws IOException, EncodeException {
        message.setFrom(users.get(session.getId()));
        recepient = to;
        System.out.println("Sending from "+from+" to "+to);
        if(to.equals("all"))broadcast(message);
        else sendMessage(message, to);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        chatEndpointSet.remove(this);
        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setContent("Disconnected!");
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        //
    }

    private static void broadcast(Message message) throws IOException, EncodeException{
        for(ServerEndPoint endpoint : chatEndpointSet){
            synchronized (endpoint){
                try{
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static void sendMessage(Message message, String to) throws IOException, EncodeException{
        for(ServerEndPoint endpoint : chatEndpointSet){
            synchronized (endpoint){
                try{
                    if(!endpoint.currentUser.equals(to)) continue;
                        endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
