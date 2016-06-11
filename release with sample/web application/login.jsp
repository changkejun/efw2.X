<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="efw" uri="efw" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>健康診断</title>

	<efw:Core/>
	<efw:JQueryUI/>
	<efw:InputBehavior/>

	<link href="mdclexam.css" rel="stylesheet">
	<script>
		function btnLogin_onclick(){
			efw.client.fire({
				//				server:"http://127.0.0.1:8080/efwTest",  //cors　test
				eventId:"mdclexam_login",
				success:function(){
					window.location="mdclexam.jsp";
				}
			});
		}
		function btnClear_onclick(){
			$("#txt_uid").val("");
			$("#txt_pwd").val("");
		}
		
		function btnF1_onclick(){
			efw.client.fire({
				eventId:"mdclexam_inputcheck",
				success:function(){
					efw.client.alert("入力は正しいです。");
				},
			});
		}
		
		function btnF2_onclick(){
			efw.client.fire({
				eventId:"mdclexam_upload",
				success:function(){
					show_files();
				},
			});
		}

		function btnF3_onclick(){
			efw.client.fire({
				eventId:"mdclexam_download",
				success:function(){
					show_files();
				}
			});
		}
		
		function show_files(){
			efw.client.fire({
				eventId:"mdclexam_showfiles",
			});
		}
		
		function btnF4_onclick(){
			efw.client.fire({
				eventId:"mdclexam_brmstest",
			});
		}

		$(function(){
			$("#btnLogin,#btnClear,#btnF1,#btnF2,#btnF3,#btnF4").button();
		});
  	</script>
</head>
<body style="background-color:ghostwhite;" onload="show_files()">
<table style="width:100%">
<tr style="height:20px">
	<td></td>
	<td style="width:200px"></td>
	<td style="width:300px"></td>
	<td></td>
</tr>
<tr>
	<td></td>
	<td colspan="2">
	<div class="ui-helper-reset ui-widget-header ui-corner-all mytitle" style="width:500px">健康診断閲覧へようこそ</div>
	<br>
	</td>
	<td></td>
</tr>
<tr>
	<td></td>
	<td style="text-align:right;">アカウント</td><td><input id="txt_uid" type="text"></td>
	<td></td>
</tr>
<tr>
	<td></td>
	<td style="text-align:right">パスワード</td><td><input id="txt_pwd" type="password"></td>
	<td></td>
</tr>
<tr>
	<td></td>
	<td colspan="2" style="text-align:center">
	<p>テストのため、「admin、password」を入力してください。</p>
	<input type="button" value="ログイン" id="btnLogin" onclick="btnLogin_onclick()">
	<input type="button" value="リセット" id="btnClear" onclick="btnClear_onclick()">
	</td>
	<td></td>
</tr>
</table>
<br><br>
<div style="text-align:center">

<span style="display:inline-block;width:540px;text-align:left">テスト文字：<input id="txt_testtext" type="text">5桁以内に入力してください。</span><br>
<span style="display:inline-block;width:540px;text-align:left">テスト数字：<input id="txt_testnumber" type="text" data-format="#,##0.00">-10.00～1,000.00の数字を入力してください。</span><br>
<span style="display:inline-block;width:540px;text-align:left">テスト日付：<input id="txt_testdate" type="text" data-format="yyyy年MM月dd日">今日から一週間以内の日付をyyyyMMddで入力してください。</span><br>
<br>
<input type="button" id="btnF1" style="width:150px" value="F1 入力チェックテスト" data-shortcut="F1" onclick="btnF1_onclick();"><br>
<br>

<span style="display:inline-block;width:540px;text-align:left">アップロードファイル：<input id="txt_file1" type="file"></span><br>
<span style="display:inline-block;width:540px;text-align:left">アップロードファイル：<input id="txt_file2" type="file"></span><br><br>
<input type="button" id="btnF2" style="width:150px" value="F2 アップロード" data-shortcut="F2" onclick="btnF2_onclick();"><br>
<br>

<span style="display:inline-block;width:540px;text-align:left">ダウンロードファイル：<select id="cmb_download" size=5 multiple style="width:350px;height:100px;"><option value="あ.xls">あ.xls</option></select></span><br><br>
<input type="button" id="btnF3" style="width:150px" value="F3 ダウンロード" data-shortcut="F3" onclick="btnF3_onclick();"><br>
<br>

<span style="display:inline-block;width:540px;text-align:left">引数1：<input type="text" id="txt_param1"></span><br>
<span style="display:inline-block;width:540px;text-align:left">引数2：<input type="text" id="txt_param2"></span><br>
<span style="display:inline-block;width:540px;text-align:left">結果-：<input type="text" id="txt_result"></span><br>
<input type="button" id="btnF4" style="width:150px" value="F4 BRMSテスト" data-shortcut="F4" onclick="btnF4_onclick();">
<br>
<br>
<br>

<input type="button" value="F5" data-shortcut="F5" onclick="alert('F5 is clicked!');">
<input type="button" value="F6" data-shortcut="F6" onclick="alert('F6 is clicked!');">
<input type="button" value="F7" data-shortcut="F7" onclick="alert('F7 is clicked!');">
<input type="button" value="F8" data-shortcut="F8" onclick="alert('F8 is clicked!');">
<input type="button" value="F9" data-shortcut="F9" onclick="alert('F9 is clicked!');">
<input type="button" value="F10" data-shortcut="F10" onclick="alert('F10 is clicked!');">
<input type="button" value="F11" data-shortcut="F11" onclick="alert('F11 is clicked!');">
<input type="button" value="F12" data-shortcut="F12" onclick="alert('F12 is clicked!');">

<input type="button" value="CTRL+A" data-shortcut="CTRL+A" onclick="alert('CTRL+A is clicked!');">
<input type="button" value="ALT+Z" data-shortcut="ALT+Z" onclick="alert('ALT+Z is clicked!');">

</div>
</body>
</html>