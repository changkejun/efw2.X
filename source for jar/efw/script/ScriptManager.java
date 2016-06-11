package efw.script;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import efw.efwException;

/**
 * サーバーサイトJavaScriptの管理と実行を行うクラス。
 * @author Chang Kejun
 *
 */
public final class ScriptManager {
	/**
	 * サーバー部品JavaScriptファイルの格納パス。
	 * サーブレットから渡される。
	 */
    private static String serverFolder;
	/**
	 * イベントJavaScriptファイルの格納パス。
	 * サーブレットから渡される。
	 */
    private static String eventFolder;
    /**
     * デバッグモードを制御するフラグ。
	 * サーブレットから渡される。
     */
    private static boolean isDebug;
    /**
     * サーバーサイトJavaScriptファイルの文字セット定数。
     * 「UTF-8」に固定。
     */
    private static final String SCRIPT_CHAR_SET="UTF-8";
    /**
     * スクリプトエンジンに渡すサーバー部品JavaScriptファイルの格納パスのキー。
     * 「_eventfolder」に固定。
     */
    private static final String KEY_SERVERFOLDER="_serverfolder";
    /**
     * スクリプトエンジンに渡すイベントJavaScriptファイルの格納パスのキー。
     * 「_eventfolder」に固定。
     */
    private static final String KEY_EVENTFOLDER="_eventfolder";
    /**
     * スクリプトエンジンに渡すデバッグモード制御フラグのキー。
     * 「_isdebug」に固定。
     */
    private static final String KEY_ISDEBUG="_isdebug";
    /**
     * スクリプトエンジンに渡すスクリプトエンジンのキー。
     * 「_engine」に固定。
     */
    private static final String KEY_ENGINE="_engine";

    private static final ScriptEngine se=(new ScriptEngineManager()).getEngineByName("JavaScript");
    
	/**
	 * サーブレットから設定情報を受け取り、スクリプトエンジン管理オブジェクトを初期化する。
	 * @param serverFolder サーバー部品JavaScriptファイルの格納パス。
	 * @param eventFolder イベントJavaScriptファイルの格納パス。
	 * @param isDebug　デバッグモード制御フラグ。
	 * @throws ScriptException 
	 * @throws IOException 
	 */
	public static synchronized void init(String serverFolder,String eventFolder,boolean isDebug)throws efwException {
		ScriptManager.serverFolder=serverFolder;
		ScriptManager.eventFolder=eventFolder;
		ScriptManager.isDebug=isDebug;

		se.put(KEY_SERVERFOLDER, ScriptManager.serverFolder);
		se.put(KEY_EVENTFOLDER, ScriptManager.eventFolder);
		se.put(KEY_ISDEBUG, ScriptManager.isDebug);
		se.put(KEY_ENGINE, ScriptManager.se);
		try {
			load(serverFolder+"/efw.server.js");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new efwException(efwException.ScriptInitFailedException);
		} catch (IOException e) {
			e.printStackTrace();
			throw new efwException(efwException.ScriptInitFailedException);
		}
	}
	/**
	 * リクエストをサーバーサイトJavaScriptに転送する。
	 * もしスレッドにスクリプトエンジンが付けられていないなら、スクリプトエンジンを作成し、共通とするefw.server.jsを実行する。
	 * @param request JQueryがefwサーブレット へ要求したJSON内容を含む HttpServletRequest オブジェクト。
	 * @return 実行結果のJSON文字列を返す。
	 * @throws NoSuchMethodException 
	 * @throws ScriptException スクリプトエラー。
	 * @throws IOException ファイル操作エラー。
	 */
	public static String doPost(String req) throws efwException {
		Invocable invocable = (Invocable) se;
		try {
			return (String)invocable.invokeFunction("doPost", req);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new efwException(efwException.ScriptDoPostFailedException);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new efwException(efwException.ScriptDoPostFailedException);
		}
	}

	/**
	 * 指定ファイル名のサーバーサイトJavaScriptファイルをロードする。
	 * JDK1.6 1.7のMozilla Rhinoエンジンに「load」関数を実装するため。
	 * @param fileName　サーバーサイトJavaScriptファイルの名称。
	 * @throws ScriptException スクリプトエラー。
	 * @throws IOException ファイル操作エラー。
	 */
	public static void load(String fileName) throws ScriptException, IOException  {
		BufferedReader rd=new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName),SCRIPT_CHAR_SET));
		se.eval(rd);
		rd.close();
	}
	  
}
