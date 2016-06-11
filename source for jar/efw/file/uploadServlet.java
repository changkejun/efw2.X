package efw.file;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import efw.efwException;

/**
 * ファイルをクライアントからWEBサーバへアップロードする
 * @author Chang Kejun
 */
@SuppressWarnings("serial")
@WebServlet(name = "uploadServlet", urlPatterns = { "/uploadServlet" })
@MultipartConfig()
public class uploadServlet extends HttpServlet {
    /**
     * レスポンスの文字セット定数、XMLHttpRequestのデフォルトに合わせ、「UTF-8」に固定。
     */
    private static final String RESPONSE_CHAR_SET="UTF-8";

	/**
	 * post方法でアップロードされたファイルを一時ファイルに保存する。
	 * @param request HttpServletRequest オブジェクト。
	 * @param response HttpServletResponse オブジェクト 。
	 * @throws efwException IOException 
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding(RESPONSE_CHAR_SET);
        for (Part part : request.getParts()) {
            String uploadFileName=null;
            for (String cd : part.getHeader("Content-Disposition").split(";")) {
                if (cd.trim().startsWith("filename")) {
                	uploadFileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                    File fl=File.createTempFile("efw", null);//efw#####.tmpのファイル名
                    part.write(fl.getAbsolutePath());
                    FileManager.keepUploadFile(request.getSession(),uploadFileName,fl.getAbsolutePath());
                    break;
                }
            }
        }
        response.setCharacterEncoding(RESPONSE_CHAR_SET);
        response.getWriter().print("[]");
    }
}
