package cn.eastseven.tcpip.sockets.in.java;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class ConfigFileUtils {
	
	private static final Logger log = Logger.getLogger(ConfigFileUtils.class);
	
	private static Configuration conf = null;
	public static String SERVER_HOST = null;
	public static int SERVER_PORT = 0;

	static {
		try {
			conf = new PropertiesConfiguration("app.properties");
			log.info("app.properties load complete");
			SERVER_HOST = conf.getString("tcp.server.ip");
			SERVER_PORT = conf.getInt("tcp.server.port");
			log.info("tcp.server.ip: " + SERVER_HOST + ", tcp.server.port: " + SERVER_PORT);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
}
