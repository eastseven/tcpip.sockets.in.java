package cn.eastseven.tcpip.sockets.in.java.ch3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class VoteServerTCP {

	private static final Logger log = Logger.getLogger(VoteServerTCP.class);
	private static ServerSocket servSock;
	
	public static void main(String[] args) throws Exception {
		
		servSock = new ServerSocket(9527);
		VoteMsgCoder coder = new VoteMsgBinCoder();
		VoteService service = new VoteService();
		
		while(true) {
			Socket clntSock = servSock.accept();
			log.info("handling client at " + clntSock.getRemoteSocketAddress());
			
			Framer framer = new LengthFramer(clntSock.getInputStream());
			try {
				byte[] req;
				while((req = framer.nextMsg()) != null) {
					log.info("received message ("+req.length+" bytes)");
					VoteMsg responseMsg = service.handleRequest(coder.formWire(req));
					framer.frameMsg(coder.toWire(responseMsg), clntSock.getOutputStream());
				}
			} catch (IOException e) {
				log.error(e);
				e.printStackTrace();
			} finally {
				log.info("closing connection");
				clntSock.close();
			}
		}
		
	}
}
