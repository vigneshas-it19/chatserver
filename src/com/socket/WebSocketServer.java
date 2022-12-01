package com.socket;
import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/actions/{username}")
public class WebSocketServer {
	 private static final Set<Session> sessions = new HashSet<>();
	 private static HashMap<String, String> users = new HashMap<>();
	 
	 @OnOpen
     public void onOpen(Session session, @PathParam("username") String username) {
		 sessions.add(session);
		 users.put(session.getId(), username);
//		 System.out.println("No of connections = "+sessions.size());
	 }
	
	 @OnClose
	 public void onClose(Session session) {
		 sessions.remove(session);
		 users.remove(session.getId());
	 }
	
	 @OnError
	 public void onError(Throwable error) {
		 Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
	 }
	
	 @OnMessage
	 public void handleMessage(String message, Session session) throws IOException {
//		 System.out.println(message);
		 JSONObject message_json = new JSONObject();
		 message_json.put("from", users.get(session.getId()));
		 message_json.put("message", message);
		 sendToAllConnectedSessions(message_json);
		 
	 }
	 
	 public void sendToAllConnectedSessions(JSONObject message) {
	    	for (Session session : sessions) {
	            sendToSession(session, message);
	        }
	 }

	 private void sendToSession(Session session, JSONObject message_json)  {
	    	 try {
	             session.getBasicRemote().sendText(message_json.toString());
	         } catch (IOException ex) {
	             sessions.remove(session);
	         }
	 }
}
