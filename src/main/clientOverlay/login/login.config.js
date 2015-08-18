
SetupLogin({

	locales : [ 'en-us', 'zh-cn'],
	showLicence : true,
	redirectPath : '../',

	onSubmit : function(username, password) {
		var xhr = new XMLHttpRequest();
		xhr.open( 'POST', '../rest/v1/auth/login', true);
		xhr.setRequestHeader( 'Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
		xhr.onload = function () {
			if (xhr.status === 200)
				Login.redirect();
			else
				Login._resetLogin(),
				Login._showMessage(JSON.parse(xhr.responseText).message);
		};
		xhr.onerror = function () {
			Login._resetLogin();
			Login._showMessage('networkError');
		};
		xhr.send('username=' + encodeURIComponent(username) + '&password=' + encodeURIComponent(password));
	}
});
