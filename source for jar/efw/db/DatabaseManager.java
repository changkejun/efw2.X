package efw.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import efw.efwException;
import efw.log.LogManager;
import efw.properties.PropertiesManager;

/**
 * データベース接続を管理するクラス。
 * @author Chang Kejun
 *
 */
public final class DatabaseManager {
	/**
	 * ネーミング操作の開始コンテキストの名称。
	 * 「java:comp/env」に固定。
	 */
	private static final String JAVA_INITCONTEXT_NAME="java:comp/env";
	/**
	 * フレームワークに利用するjdbcリソースの名称。
	 * <br>efw.propertiesのefw.jdbc.resourceで設定、
	 * デフォルトは「jdbc/efw」。
	 */
	private static String jdbcResourceName="jdbc/efw";
	/**
	 * フレームワークに利用するデータソース。
	 */
	private static DataSource dataSource;
	
    /**
     * データベースオブジェクト。
     * スレッドローカルにデータベースオブジェクトを格納する。サーバーサイトJavascriptに利用される。
     */
	private static ThreadLocal<HashMap<String,Database>> database=new ThreadLocal<HashMap<String,Database>>();
	
    public static Database getDatabase(){
    	return (DatabaseManager.database.get()).get(DatabaseManager.jdbcResourceName);
    }
    public static Database getDatabase(String jdbcResourceName){
    	if(jdbcResourceName==null||"".equals(jdbcResourceName)){
    		return DatabaseManager.getDatabase();
    	}
    	return (DatabaseManager.database.get()).get(jdbcResourceName);
    }
    
	/**
	 * フレームワークに利用するデータソースを初期化する。
	 * @throws efwException データソース初期化失敗のエラー。
	 */
	public static synchronized void init() throws efwException{
		try {
	        jdbcResourceName=PropertiesManager.getProperty(PropertiesManager.EFW_JDBC_RESOURCE,jdbcResourceName);
	        if(jdbcResourceName.indexOf("java:")>-1){//if the jdbc resouce begins from [java:], it is full jndi name.
	        	dataSource = (DataSource) new InitialContext().lookup(jdbcResourceName);
	        }else{//or it begins by [java:comp/env/]
	        	dataSource = (DataSource) new InitialContext().lookup(JAVA_INITCONTEXT_NAME+"/"+jdbcResourceName);
	        }

		} catch (NamingException e) {
			e.printStackTrace();
    		throw new efwException(efwException.DataSourceInitFailedException,jdbcResourceName);
		}
	}
	
	private static Boolean fromBatch=false;
	private static HashMap<String,BatchDataSource> batchDataSources=new HashMap<String,BatchDataSource>();
	public static synchronized void initFromBatch() throws efwException{
		fromBatch=true;
    	for (int idx=0;true;idx++){
        	String jdbcResource=PropertiesManager.EFW_JDBC_RESOURCE+(idx==0?"":"."+idx);
        	String url=PropertiesManager.EFW_JDBC_RESOURCE_URL+(idx==0?"":"."+idx);
        	String username=PropertiesManager.EFW_JDBC_RESOURCE_USERNAME+(idx==0?"":"."+idx);
        	String password=PropertiesManager.EFW_JDBC_RESOURCE_PASSWORD+(idx==0?"":"."+idx);
        	String jdbcResourceValue=PropertiesManager.getProperty(jdbcResource, "");
        	if(jdbcResourceValue!=null && !"".equals(jdbcResourceValue)){
        		BatchDataSource dataSource = new BatchDataSource();
        		String urlValue=PropertiesManager.getProperty(url, "");
        		String usernameValue=PropertiesManager.getProperty(username, "");
        		String passwordValue=PropertiesManager.getProperty(password, "");
        		dataSource.setUrl(urlValue);
        		dataSource.setUsername(usernameValue);
        		dataSource.setPassword(passwordValue);
        		if(idx==0)DatabaseManager.dataSource=dataSource;
        		batchDataSources.put(jdbcResourceValue, dataSource);
        	}else{
        		break;
        	}
    	}
	}
    ///////////////////////////////////////////////////////////////////////////
	/**
	 * フレームワーク用データソースからデータベース接続を取得する。
	 * @return データベース接続を戻す。
	 * @throws SQLException データベースアクセスエラー。
	 */
    public static void open() throws SQLException{
		if(DatabaseManager.database.get()==null)
			DatabaseManager.database.set(new HashMap<String,Database>());

		DatabaseManager.database.get()
		.put(DatabaseManager.jdbcResourceName, new Database(dataSource.getConnection()));
        LogManager.CommDebug("DatabaseManager.open");
    }
    /**
     * jdbcリソース名称によりデータベース接続を取得する。
     * @param jdbcResourceName jdbcリソース名称
     * @return　データベース接続を戻す。
     * @throws NamingException　名称不正のエラー。　
     * @throws SQLException　データベースアクセスエラー。
     */
    public static void open(String jdbcResourceName) throws NamingException, SQLException{
    	if (jdbcResourceName==null||"".equals(jdbcResourceName)){
    		DatabaseManager.open();
    		return;
    	}else{
            DataSource ds;
    		if(fromBatch){
    			ds = batchDataSources.get(jdbcResourceName);
    		}else{//from web
                if(jdbcResourceName.indexOf("java:")>-1){//if the jdbc resouce begins from [java:], it is full jndi name.
                	ds = (DataSource) new InitialContext().lookup(jdbcResourceName);
                }else{//or it begins by [java:comp/env/]
                	ds = (DataSource) new InitialContext().lookup(JAVA_INITCONTEXT_NAME+"/"+jdbcResourceName);
                }
    		}
            Database otherdb = new Database(ds.getConnection());
    		if(DatabaseManager.database.get()==null)
    			DatabaseManager.database.set(new HashMap<String,Database>());
    		DatabaseManager.database.get()
    		.put(jdbcResourceName, otherdb);
            LogManager.CommDebug("DatabaseManager.open",jdbcResourceName);
    	}
    }
    /**
     * すべてのデータベースを閉じる。
     * @throws SQLException 　データベースアクセスエラー。
     */
    public static void closeAll() throws SQLException{
		if(DatabaseManager.database.get()==null)
			DatabaseManager.database.set(new HashMap<String,Database>());

		HashMap<String,Database> map=DatabaseManager.database.get();
		for(Entry<String, Database> e : map.entrySet()) {
			Database db=e.getValue();
			db.close();
		}
		DatabaseManager.database.remove();
        LogManager.CommDebug("DatabaseManager.closeAll");
    }

}
