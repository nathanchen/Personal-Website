<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑器完整版实例</title>
<script type="text/javascript" src="../ueditor/editor_config.js"></script>
<script type="text/javascript" src="../ueditor/editor_all.js"></script>
<link rel="stylesheet" href="../ueditor/themes/default/ueditor.css"/>
</head>
<body>
<script type="text/plain" id="myEditor">初始内容</script>
<script type="text/javascript">
    var editor = new baidu.editor.ui.Editor();
    editor.render("myEditor");
</script>
<p>
    初始内容<img src="../server/uploadfiles/1330084940581.jpg" style="float:none;" title="110757_off" border="0" hspace="0" vspace="0" />
</p>
</body>
</html>