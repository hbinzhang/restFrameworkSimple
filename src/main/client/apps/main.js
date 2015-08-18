/*global define*/
define([
	'jscore/core',
	'text!./apps.css',
	'i18n!./app.json',
], function(core, style, app) {
	'use strict';

	console.debug(app);
	var template = '<div class="eaApps-Apps">';
	for(var i = 0; i < app.apps.length; ++i) {
		template += '<div class="eaApps-App"><a class="eaApps-App-link" href="#' + app.apps[i].id + '"';
		if( app.apps[i].icon) template +=
				' style="background-image: url(\'assets/1/resources/16px/' + app.apps[i].icon + '_black_16px.svg\')"';
		template += '>' + app.apps[i].name + '</a></div>';
	}
	template += '<div class="eaApps-App"><span class="eaApps-App-text">Welcome: Boelos</span></div>';
	template += '</div>';

	return core.App.extend({
		View : core.View.extend({
			getTemplate: function () {return template;},
			getStyle: function() {return style;},
		}),
		onStart : function(options) {
		},
	});
});
