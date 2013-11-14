package cn.eastseven.tcpip.sockets.in.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;


public class App {
	
	static Logger log = Logger.getLogger(App.class.getName());
	
	public static void main(String[] args) {
		log.info("start...");
		
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		StringBuffer sb = null;
		try{
			socket = new Socket("172.28.202.21", 8080);
			System.out.println("connect:"+socket.isConnected());
			System.out.println("isclose:"+socket.isClosed());
			System.out.println("isBound:"+socket.isBound());
			System.out.println("out:"+socket.isOutputShutdown());
			out = new PrintWriter(socket.getOutputStream());
			out.println("test");
			out.flush();
			socket.shutdownOutput();
			System.out.println("out2:"+socket.isOutputShutdown());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("input:"+socket.isBound());
			String line = null;
			sb = new StringBuffer();
			while ((line = in.readLine()) != null){
				System.out.println(line);
				sb.append(line);
			}
			out.close();
			socket.close();
			System.out.println("-----client: socket is closed------");
		}catch(IOException e){
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		log.info("end...");
	}
}
