/*global define*/
define([
	'jscore/core',
], function(core) {

	return core.App.extend({
		View : core.View.extend({
			getTemplate: function () {return '<div>hey</div>';},
		}),
		onStart: function() {
			app = this;
			this.nav = {};
			this.getEventBus().subscribe( 'nav:add', this.addNavItem.bind(this));
			this.getEventBus().subscribe( 'nav:remove', this.removeNavItem.bind(this));
		},
		addNavItem: function( data) {
			// example:
			// data = {
			//    path: ['sysman'],
			//    title: 'System Management',
			//    hash: 'sysman',
			//    priority: 4,
			// }
			if( !( data.path.length > 0)) return;
			var parent = this.getNavItem( data.path.slice( 0, data.path.length - 1), true);
			parent[data.path[data.path.length - 1]] = {
				title: data.title,
				hast: data.hash,
				priority: data.priority,
			};
			this.updateView();
		},
		removeNavItem: function( data) {
			// data = {
			//    path: ['sysman'],
			// }
			if( !( data.path.length > 0)) return;
			var parent = this.getNavItem( data.path.slice( 0, data.path.length - 1), false);
			if( parent) delete parent[ data.path[ data.path.length - 1]];
			this.updateView();
		},
		getNavItem: function( path, create) {
			var nav = this.nav;
			for( var i = 0; i < path.length; ++i)
				if( nav[path[i]]) nav = nav[path[i]];
				else if( !create) return null;
				else nav = nav[path[i]] = { title: path[i]};
			return nav;
		},
		updateView: function() {
			this.getElement().setText( JSON.stringify( this.nav));
		},
	});
});
