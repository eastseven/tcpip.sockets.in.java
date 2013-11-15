package cn.eastseven.tcpip.sockets.in.java;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;


public class TcpClient {

	private static final Logger log = Logger.getLogger(TcpServer.class);
	
	static String host = "localhost";
	static int port = TcpServer.SERVER_PORT;
	
	public static void main(String[] args) {

		try {
			Socket client = new Socket(host, port);
			log.info("client: " + client);
			OutputStream out = client.getOutputStream();
			String data = "hello tcp server";
			out.write(data.getBytes());
			out.close();
			client.close();
			log.info("client send complete, send messge: " + data);
		} catch (UnknownHostException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}

}
