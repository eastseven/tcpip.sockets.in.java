package cn.eastseven.tcpip.sockets.in.java;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class TcpServer {

	private static final Logger log = Logger.getLogger(TcpServer.class);
	
	public static final int SERVER_PORT = 9527;
	private static ServerSocket ser = null;
	private TcpServer() {}
	
	public void init() {
		try {
			if(ser == null) {
				ser = new ServerSocket(SERVER_PORT);
				log.info("server instantiation");
			}
		} catch (IOException e) {
			log.error(e);
		}
		//this.bindInetAddress();
	}
	
	public void start() {
		this.init();
		log.info("server running");
		while(true) {
			try {
				Socket socket = ser.accept();
				log.info("client connect: " + socket);
				InputStream in = socket.getInputStream();
				int receive = -1;
				String value = "";
				while((receive = in.read()) != -1) {
					value += "," + receive;
				}
				in.close();
				socket.close();
				log.info("client disconnect, receive value: " + value);
			} catch (IOException e) {
				log.error(e);
			}
		}
	}
	
	public void bindInetAddress() {
		try {
			Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
			while(nis.hasMoreElements()) {
				NetworkInterface ni = nis.nextElement();//网卡
				Enumeration<InetAddress> addressList = ni.getInetAddresses();
				while(addressList.hasMoreElements()) {
					InetAddress inetAddress = addressList.nextElement();
					if(inetAddress instanceof Inet4Address) {
						log.info("["+ser.getInetAddress()+"]"+inetAddress.getClass()+": "+inetAddress.getHostName()+", "+inetAddress.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			log.error(e);
		}
	}
	
	public static ServerSocket getServer() {
		return ser;
	}
	
	public static void main(String[] args) {

		TcpServer ser = new TcpServer();
		ser.start();
	}
}
