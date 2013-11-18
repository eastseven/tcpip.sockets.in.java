package cn.eastseven.tcpip.sockets.in.java;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Date;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaTcpServer {

	private static final Logger log = Logger.getLogger(MinaTcpServer.class);

	private Configuration conf = null;
	private String SERVER_HOST = null;
	private int SERVER_PORT = 0;

	public void loadConfig() {
		try {
			conf = new PropertiesConfiguration("app.properties");
			log.info("app.properties load complete");
			SERVER_HOST = conf.getString("tcp.server.ip");
			SERVER_PORT = conf.getInt("tcp.server.port");
			log.info("server host: " + SERVER_HOST + ", port: " + SERVER_PORT);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		loadConfig();

		try {
			IoAcceptor acceptor = new NioSocketAcceptor();
			acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
			acceptor.setHandler(new TimeServerHandler());
			acceptor.getSessionConfig().setReadBufferSize(2048);
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
			acceptor.bind(new InetSocketAddress(SERVER_PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class TimeServerHandler extends IoHandlerAdapter {
		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
				throws Exception {
			cause.printStackTrace();
		}

		@Override
		public void messageReceived(IoSession session, Object message) throws Exception {
			String str = message.toString();
			if (str.trim().equalsIgnoreCase("quit")) {
				session.close(false);
				return;
			}
			Date date = new Date();
			session.write(date.toString());
			System.out.println("Message written...");
		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status)
				throws Exception {
			System.out.println("IDLE " + session.getIdleCount(status));
		}
	}

	public static void main(String[] args) {
		log.debug("start");
		new MinaTcpServer().start();
	}

}
