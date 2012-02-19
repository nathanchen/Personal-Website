
var $ = function(id) {
	return document.getElementById(id);
};

var formSubmit = function(id) {
	document.forms[id].submit();
	return true;
};

var tip = function(q, for_q) {
	q = $(q);
	for_q = $(for_q);
	q.onfocus = function() {
		for_q.style.display = 'none';
		q.style.backgroundPosition = "right -17px";
	};
	q.onblur = function() {
		if (!this.value)
			for_q.style.display = 'block';
		q.style.backgroundPosition = "right 0";
	};
	for_q.onclick = function() {
		this.style.display = 'none';
		q.focus();
	};
};

tip('keyword', 'for-keyword');
