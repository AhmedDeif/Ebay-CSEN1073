package UserApp.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
	public static String appHost;
	public static int appPort;

	public static String dbHost;
	public static int dbPort;
	public static String dbName;
	public static String dbUser;
	public static String dbPassword;

	public static String mqResponseHost;
	public static int mqResponsePort;
	public static String mqResponseUser;
	public static String mqResponsePassword;

	public static String mqResponseQueueName;
	public static String mqResponseExchangeName;
	public static String mqResponseRouteKey;
	public static String mqResponseQueueTag;

	public static String mqRequestHost;
	public static int mqRequestPort;
	public static String mqRequestUser;
	public static String mqRequestPassword;

	public static String mqRequestQueueName;
	public static String mqRequestExchangeName;
	public static String mqRequestRouteKey;
	public static String mqRequestQueueTag;

	public static void readConfiguration(String fileName) {

		try {
			Properties props = new Properties();
			InputStream in = ApplicationProperties.class.getResourceAsStream(fileName);
			props.load(in);
			in.close();

			appHost = props.getProperty("appHost");
			appPort = Integer.parseInt(props.getProperty("appPort"));
			
			
			dbHost = props.getProperty("dbHost");
			dbPort = Integer.parseInt(props.getProperty("dbPort"));
			dbName = props.getProperty("dbName");
			dbUser = props.getProperty("dbUser");
			dbPassword = props.getProperty("dbPassword");

			mqResponseHost = props.getProperty("mqResponseHost");
			mqResponsePort = Integer.parseInt(props.getProperty("mqResponsePort"));
			mqResponseUser = props.getProperty("mqResponseUser");
			mqResponsePassword = props.getProperty("mqResponsePassword");
			mqResponseQueueName = props.getProperty("mqResponseQueueName");
			mqResponseExchangeName = props.getProperty("mqResponseExchangeName");
			mqResponseRouteKey = props.getProperty("mqResponseRouteKey");
			mqResponseQueueTag = props.getProperty("mqResponseQueueTag");

			mqRequestHost = props.getProperty("mqRequestHost");
			mqRequestPort = Integer.parseInt(props.getProperty("mqRequestPort"));
			mqRequestUser = props.getProperty("mqRequestUser");
			mqRequestPassword = props.getProperty("mqRequestPassword");
			mqRequestQueueName = props.getProperty("mqRequestQueueName");
			mqRequestExchangeName = props.getProperty("mqRequestExchangeName");
			mqRequestRouteKey = props.getProperty("mqRequestRouteKey");
			mqRequestQueueTag = props.getProperty("mqRequestQueueTag");
		} catch (IOException exp) {
			exp.printStackTrace();
		}


	}

}
