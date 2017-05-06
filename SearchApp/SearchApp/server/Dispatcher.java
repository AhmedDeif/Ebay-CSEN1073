package SearchApp.server;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

import com.zaxxer.hikari.HikariDataSource;

import SearchApp.config.ApplicationProperties;
import SearchApp.commands.Command;
import SearchApp.server.ClientHandle;
import SearchApp.server.ClientRequest;

//import Dispatcher.Command;

public class Dispatcher {

	protected Hashtable _htblCommands;
	protected ExecutorService _threadPoolCmds;
	protected HikariDataSource _hikariDataSource;
	protected Properties _properties;

	public Dispatcher() {
	}

	protected void dispatchRequest(ClientHandle clientHandle, ClientRequest clientRequest) throws Exception {

		Command cmd;
		String strAction;
		strAction = clientRequest.getAction();

		Class<?> cmdClass = (Class<?>) _htblCommands.get(strAction);
		Object cmdInstance = cmdClass.newInstance();
		cmd = (Command) cmdInstance;
		cmd.init(_hikariDataSource, clientHandle, clientRequest);
		System.out.println(cmd.toString());
		_threadPoolCmds.execute((Runnable) cmd);
	}

	protected void loadHikari(String strAddress, int nPort, String strDBName, String strUserName, String strPassword) {

		System.out.println("Loading hikari");
		_hikariDataSource = new HikariDataSource();

		_hikariDataSource.setJdbcUrl("jdbc:postgresql://" + strAddress + ":" + nPort + "/" + strDBName);
		_hikariDataSource.setUsername(strUserName);
		_hikariDataSource.setPassword(strPassword);

		_hikariDataSource.setInitializationFailFast(true);
	}

	protected void loadCommands() throws Exception {
		_htblCommands = new Hashtable();
		Properties prop = new Properties();
		InputStream in = ApplicationProperties.class.getResourceAsStream("commands.properties");
		prop.load(in);
		in.close();
		Enumeration enumKeys = prop.propertyNames();
		String strActionName, strClassName;

		while (enumKeys.hasMoreElements()) {
			strActionName = (String) enumKeys.nextElement();
			strClassName = (String) prop.get(strActionName);
			Class<?> innerClass = Class.forName("SearchApp.commands." + strClassName);
			_htblCommands.put(strActionName, innerClass);
		}
	}

	protected void loadThreadPool() {
		_threadPoolCmds = Executors.newFixedThreadPool(20);
	}

	public void init() throws Exception {
		System.out.println("APPLICATION HOST: " + ApplicationProperties.appHost);
		loadHikari(ApplicationProperties.dbHost,ApplicationProperties.dbPort, ApplicationProperties.dbName, ApplicationProperties.dbUser, ApplicationProperties.dbPassword);
//		loadHikari("localhost", 5432, "ebay","postgres", "2428");

		loadThreadPool();
		loadCommands();

	}
}