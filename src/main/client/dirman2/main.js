
define([
	'jscore/core',
	'layouts/TopSection',
	'layouts/SlidingPanels',
	'layouts/MultiSlidingPanels',
	'layouts/Carousel',
	'widgets/Button',
	'widgets/Tree',
	'widgets/ExpandableList',
	'tablelib/Table',
	'tablelib/plugins/StickyHeader',
	'tablelib/plugins/StickyScrollbar',
	'tablelib/plugins/Selection',
	'tablelib/plugins/ColorBand',
	'tablelib/plugins/ResizableHeader',
	'tablelib/plugins/SmartTooltips',
	'nfvolib/Abc',
], function (core, TopSection, SlidingPanels, MultiSlidingPanels, Carousel, Button, Tree, ExpandableList, Table, StickyHeader,
		StickyScrollbar, Selection, ColorBand, ResizableHeader, SmartTooltips, Abc) {
console.log( Abc);
	return core.App.extend({

		init: function(options) {
			this.options = options;
			console.debug(options);
		},

		onStart: function (parent) {
			var pageTree = new Tree({items: [
				{
					label: 'Tenant-1',
					icon: 'settings',
					children: [
						{
							label: '服务目录',
							icon: 'settings',
							children: [
								{
									label: 'NSD',
									icon: 'settings',
								},
								{
									label: 'VNFFGD',
									icon: 'settings',
								},
							],
						},
						{
							label: '网元目录',
							icon: 'settings',
							children: [
								{
									label: 'VLD',
									icon: 'settings',
								},
								{
									label: 'VNF',
									icon: 'settings',
								},
							],
						},
						{
							label: '初始化目录',
							icon: 'settings',
							children: [
								{
									label: 'NSID',
									icon: 'settings',
								},
								{
									label: 'VNFID',
									icon: 'settings',
								},
							],
						},
					],
				},
			]});
			var expand = function(item) {
				item.expand();
				item.getItems().forEach(function(i) {expand(i);});
			};
			pageTree.getItems().forEach(function(i) {expand(i);});
			pageTree.getItem(0).select();
			pageTree.addEventHandler('itemselect', function(selected) {
			});
			abc = pageTree;

			var table = new Table({
				plugins: [
					new StickyScrollbar(),
					new StickyHeader({
						topOffset: 32
					}),
					new ColorBand({
						color: function(row) {
							return row.getData().status === '可用'? '#21b319': '#e32119';
						}
					}),
					new Selection({
						selectableRows: true,
					}),
					new ResizableHeader(),
					new SmartTooltips(),
				],
				tooltips: true,
				columns: [
					{ title: "名称",     attribute: "name",},
					{ title: "版本",     attribute: "version",},
					{ title: "类型",},
					{ title: "供应商",   attribute: "vendor",},
					{ title: "状态",     attribute: "status",},
					{ title: "描述",     attribute: "description",},
					{ title: "上载时间", attribute: "uploadTime",},
					{ title: "上载进度", attribute: "uploadProgress",},
					{ title: "上载人",   attribute: "uploader",},
				],
				data: [
					{
						name: "OVF Package 1",
						version: "v1.0",
						vendor: "Ericsson",
						status: "可用",
						description: "OVF Package 1",
						uploadTime: "2015-06-30 12:54:32",
						uploadProgress: 50,
						uploader: "Admin",
					},
					{
						name: "OVF Package 1",
						version: "v1.0",
						vendor: "Ericsson",
						status: "禁用",
						description: "OVF Package 2",
						uploadTime: "2015-06-30 12:57:57",
						uploadProgress: 23,
						uploader: "Admin",
					},
					{
						name: "OVF Package 3",
						version: "v1.0",
						vendor: "Ericsson",
						status: "可用",
						description: "OVF Package 3",
						uploadTime: "2015-06-30 15:12:03",
						uploadProgress: 100,
						uploader: "Admin",
					},
				],
			});

			var topSection = new TopSection({
				context: this.getContext(),
				title: '目录管理',
				breadcrumb: this.options.breadcrumb,
				defaultActions: [
					{
						type: 'button',
						name: '上载',
						icon: 'upload',
						action: function() {},
					},
					{
						type: 'button',
						name: '导出',
						icon: 'export',
						action: function() {},
					},
					{
						type: 'button',
						name: '启用',
						icon: 'tick',
						action: function() {},
					},
					{
						type: 'button',
						name: '禁用',
						icon: 'obsolete',
						action: function() {},
					},
					{
						type: 'button',
						name: '删除',
						icon: 'close_red',
						action: function() {},
					},
					{
						type: 'button',
						name: '下发',
						icon: 'share',
						action: function() {},
					},
					{
						type: 'button',
						name: '部署',
						icon: 'grid3x3',
						action: function() {},
					},
					{
						type: 'button',
						name: '查询',
						icon: 'search',
						action: function() {},
					},
					{
						type: 'button',
						name: '更改',
						icon: 'edit',
						action: function() {},
					},
				]
			});
			topSection.attachTo(this.getElement());
			topSection.setContent( new SlidingPanels({
				context: this.getContext(),
				left: {
					icon: 'duplicate',
					value: 'page-tree',
					label: 'Nodes',
					contents: pageTree,
					expanded: true,
				},
				main: {
					label: 'Main',
					contents: table,
				},
			}));

			console.debug( topSection);
		},
	});
});
