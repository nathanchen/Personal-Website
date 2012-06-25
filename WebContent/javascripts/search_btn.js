var $ = function(id) {
	return document.getElementById(id);
};
var tip = function(q, for_q) {
	q = $(q);
	for_q = $(for_q);
	q.onfocus = function() {
		for_q.style.display = 'none';
		q.style.backgroundPosition = "right -17px";
	}
	q.onblur = function() {
		if (!this.value)
			for_q.style.display = 'block';
		q.style.backgroundPosition = "right 0";
	}
	for_q.onclick = function() {
		this.style.display = 'none';
		q.focus();
	}
};

function checkOnSubmit(name, message, errorMessage, postCommentForm)
{
	name =  $(name);
	message = $(message);
	errorMessage = $(errorMessage);
	postCommentForm = $(postCommentForm);
	
	var errorMessageHTML = "";
	
	if(message.value == "" || name.value == "")
	{
		if(name.value == "")
			errorMessageHTML = errorMessageHTML + "姓名不能为空";
		if(name.value == "" && message.value == "")
			errorMessageHTML = errorMessageHTML + "<br /><br />";
		if(message.value == "")
			errorMessageHTML = errorMessageHTML + "留言不能为空\n";
		
		errorMessage.innerHTML = "<p class=\"error\">" + errorMessageHTML + "</p>";
		return false;
	}
	else
	{
		postCommentForm.submit();
	}
};