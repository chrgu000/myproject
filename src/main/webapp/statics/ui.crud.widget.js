/*
 * UI crud Widget based jQuery and jQuery ui.
 * Copyright 2011 Janins@163.com
 * Depends:
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 *	jquery.ui.button.js
 *	jquery.ui.datepicker.js
 *	
 *	tinyMCE
 */
;(function($) {
	window.__crud_image_cut_callback_cache={};
	window.__crud_image_data_cache={};
	window.__crud_image_cut_callback=function(id,field,base64){
		var callback=__crud_image_cut_callback_cache['crud_'+id+'_'+field];
		if(callback){callback.callback.apply(callback.target,[field,base64]);}
	};
	window.__crud_image_view_data=function(id){
		return __crud_image_data_cache[id];
	};
	var opts={viewLinkField:null,url :'',title:null,showListHeader:true,countPerPage:50,dateFormat:'yy-mm-dd',autoRefresh:true,
			buttons:{},params:{},searchParams:{},editParams:{},showFooterActionBar:false,
			searchable:true,delable:true,createable:true,editable:true,selectable:true,
			refreshable:true,paged:true,showheader:true,viewable:true,
			initAction:'list',editTarget:null,viewTarget:null,editSuccessCallback:null,
			searchFieldChangeCallback:null,editFieldChangeCallback:null,exportable:true,multiselect:false,
			tinymceUrl:'/statics/scripts/tiny_mce/tiny_mce.js',
			imageCutSwf:'/statics/scripts/ui-widgets/image_upload.swf',
			imageViewSwf:'/statics/scripts/ui-widgets/base64image_view.swf',
			captchaImgUrl:'',filemanagerUrl:'',cssClass:null,description:null,
			editListColumn:1,viewListColumn:1,listSuccessCallback:null,
			i18n:{searchLabel:'搜索',refreshLabel:'刷新',createLabel:'添加',delLabel:'删除',editLabel:'修改',
				listAllLabel:'列出全部',pageInfoFunc:function(total){return '总共'+total+'条记录';},
				firstPageLabel:'首页',lastPageLabel:'末页',nextPageLabel:'下一页',prePageLabel:'上一页',returnLabel:'返回',
				saveSuccessInfo:'保存<strong>成功！</strong>您可以直接返回列表。',saveFailInfo:'出现错误，请按提示操作!',
				viewLabel:'详细',exportLabel:'导出',busyInfo:'正在操作...请稍等！',exportAllLabel:'导出全部',
				exportPageLabel:'导出当前页',cancelLabel:'取消',
				errorInfo:'<p><em>当前操作出现错误！</em>建议您进行如下操作：<br/><ul><li>注销重新登录</li></ul></p>',
				viewFileLabel:'打开',downloadFileLabel:'下载',selectAllLabel:'全选',
				unselectAllLabel:'取消全选',selectValueLabel:'选择...',selectValueConfirmLabel:'确定'}
			};
	if($.fn.crudOptions){
		$.fn.crudOptions=$.extend(true,opts,$.fn.crudOptions);
	}else{
		$.fn.crudOptions=opts;
	}
	$.widget('javaee.crud', {
		options : $.extend(true,{},$.fn.crudOptions),
		_create : function() {//这个方法在实例化后立刻运行一次。
			var c,caches;
			$.ajaxSetup({cache: false});//设置全局ajax选项，不缓存
			/*this.element 它是插件的实例化元素,相当于返回jquery对象
			比如，如果你是使用$("#foo").myWidget()，让后在你你的控件对象中this.element是一个包含id为foo的元素的jquery对象。*/
			this.element.addClass('ui-widget ui-crud-widget');
			this.element.empty();
			c=$('<div>'+
					'<div class="ui-widget-header ui-corner-all ui-header"></div>'+
					'<div class="ui-widget-content ui-corner-all">'+
						'<div class="ui-search-bar ui-helper-hidden"></div>'+
						'<div class="ui-action-bar ui-action-bar-top"></div>'+
						'<div class="ui-page-bar ui-page-bar-top ui-helper-hidden"></div>'+
						'<div class="ui-body"></div>'+
						'<div class="ui-page-bar ui-page-bar-bottom ui-helper-hidden"></div>'+
						'<div class="ui-action-bar ui-action-bar-bottom"></div>'+
					'</div>'+
				'</div>');
			this.element.append(c);
			this.element.data('_caches', 
			{
				_header : null,_body : null,_searchbar : null,_actionbar_bottom:null,_actionbar : null,
				_pagebar : null,_pagebar_top:null,_page : 1,_listtable:null,_totalPage:0,_multiSelect:false,
				_paged:false,_searchable:false,Count:0,_isSearch:false
			});
			caches=this.element.data('_caches');
			caches._header=$('.ui-header',this.element);
			caches._actionbar=$('.ui-action-bar-top',this.element);
			caches._actionbar_bottom=$('.ui-action-bar-bottom',this.element);
			caches._searchbar=$('.ui-search-bar',this.element);
			caches._body=$('.ui-body',this.element);
			caches._pagebar=$('.ui-page-bar-bottom',this.element);
			caches._pagebar_top=$('.ui-page-bar-top',this.element);
		},
		_init : function() {
			this._clearCaches();
			if(this.options.url!=''){
				var t=this, elem=t.element,caches=elem.data('_caches');
				if(t.options.cssClass!=null){elem.addClass(t.options.cssClass);}
				
				if(t.options.description!=null){
					caches._description=
						$('<div class="ui-state-highlight ui-corner-all">'+
								'<p>'+
								'<span class="ui-icon ui-icon-info" style="float: left; margin-right: 0.3em;"></span>'+
								'<span class="infomation">'+t.options.description+'</span>'+
								'</p>'+
							'</div>');
					caches._searchbar.before(caches._description);
				}
				if(t.options.buttons){
					var i=0;
					$.each(t.options.buttons,function(idx,val){
						if($.isFunction(val)){
							var bid='extend_btn_'+i;
							caches._actionbar.append('<button class="'+bid+'">'+idx+'</button>');
							if(t.options.showFooterActionBar){
								caches._actionbar_bottom.append('<button class="'+bid+'">'+idx+'</button>');
								var b=$('.'+bid,caches._actionbar_bottom);
								if($.fn.button){b.button();}
								b.click(function(){val.apply(elem);});
							}
							var b=$('.'+bid,caches._actionbar);
							if($.fn.button){b.button();}
							b.click(function(){val.apply(elem);});
							i++;
						}
					});
				}
				if(t.options.initAction=='view'){/*view the special entity*/
					if(t.options.editable){
						caches._actionbar
							.append('<button class="ui-edit-button ui-form-action">'+t.options.i18n.editLabel+'</button>');
						var eb=$('button.ui-edit-button',caches._actionbar);
						if($.fn.button){eb.button();}
						eb.click(function(){t._loadEdit(t.options.viewTarget);});
					}
					t._showBusy();
					var query=t._generateQueryString(3);
					$.get(t.options.url+'/view/'+t._urlEncode(t.options.viewTarget)+query,function(data){t._onviewsuccess(data);},'xml').error(function(){t._onviewerr();});
				}else if(t.options.initAction=='edit'){/*create or edit entity*/
					if(t.options.editTarget){t._loadEdit(t.options.editTarget);
					}else{t._loadEdit('new');}
				}else{t._loadIndex();/* load index data */}
				caches._header.text(t.options.title?t.options.title:'');
			}
		},
		destroy : function() {
			this.element.removeClass('ui-crud-widget').empty();
			if(this.options.cssClass){this.element.removeClass(this.options.cssClass);}
			$.Widget.prototype.destroy.call(this);
		},
		_setOption : function(key, value) {
			if($.isPlainObject(this.options[key])&&$.isPlainObject(value)){$.extend(true,this.options[key],value);
			}else{this.options[key] = value;}
		},
		_loadIndex : function(){
			var target=this, elem=this.element,caches=elem.data('_caches'),query;
			this._showBusy();
			query=this._generateQueryString(3);
			$.get(this.options.url+query,function(data){target._onindexsuccess(data);},'xml').error(function(){target._onindexerr();});
		},
		_onindexsuccess : function(data){
			var xml=$(data),instance=this, elem=this.element,caches=elem.data('_caches');
			if(instance._checksuccess(xml)){
				var response=xml.find('response');
				if(instance.options.title){caches._header.text(instance.options.title);
				}else{caches._header.text(response.attr('pageTitle'));}
				if(instance.options.description==null){
					var pageDesc=response.attr('pageDescription');
					if(pageDesc){
						caches._description=$('<div class="ui-state-highlight ui-corner-all"><p><span class="ui-icon ui-icon-info" style="float: left; margin-right: 0.3em;"></span><span class="infomation">'+pageDesc+'</span></p></div>');
						caches._searchbar.before(caches._description);
					}
				}
				if(instance.options.showheader){caches._header.show();
				}else{caches._header.hide();}
				/* Generate the search filed*/
				var requiredParams=[];
				if(response.attr('searchable')==='true'&&instance.options.searchable){
					var search=response.find('search'),html;
					if(search){
						caches._actionbar.prepend('<div class="ui-list-title">'+instance.options.i18n.listHeaderLabel+'</div>');
						html='<form method="post"><ul>';
						search.find('field').each(function(){
							var t=$(this),type=t.attr('dataType'),label=t.attr('label'),name=t.attr('name'),val=t.attr('value');
							if(val==undefined){val='';}
							html=html+'<li class="ui-field-'+name+'"><label>'+label+"</label>";
							switch(type){
							case 'input':
								html=html+'<input class="ui-field" name="'+name+'" value="'+val+'"/>';
								break;
							case 'password':
								html=html+'<input type="password" class="ui-field" name="'+name+'" value="'+val+'"/>';
								break;
							case 'select':
								var ref=t.attr('ref');
								if(ref!=undefined){
									requiredParams.push({query:'select[name="'+ref+'"],input[name="'+ref+'"]',name:ref});
									html=html+'<select name="'+name+'" class="ui-field ref-'+ref+'">';
								}else{
									html=html+'<select name="'+name+'" class="ui-field">';
								}
								
								t.find('dictionary').find('option').each(function(){
									var tt=$(this),valval=tt.attr('value')+"";
									if(val===valval){
										html=html+'<option selected="selected" value="'+valval+'">'+tt.attr('label')+'</option>';
									}else{
										html=html+'<option value="'+valval+'">'+tt.attr('label')+'</option>';
									}
								});
								html=html+'</select>';
								break;
							case 'textarea':
								html=html+'<input class="ui-field" name="'+name+'">'+val+'</textarea>';
								break;
							case 'date':
								html=html+'<input class="ui-field ui-date" value="'+val+'" name="'+name+'"/>';
								break;
							case 'time':
								html=html+'<input style="width:40px;" class="ui-field ui-time" name="'+name+'" value="'+val+'"/>';
								break;
							case 'datetime':
								html=html+'<input type="hidden" class="ui-field ui-date-time" name="'+name+'" value="'+val+'"/>';
								var date='',hour='',minute='';
								if(val!=''){
									var vals=val.split(' ');
									date=vals[0];time=vals[1].split(':'),hour=time[0],minute=time[1];
									if(hour.length<2){
										hour='0'+hour;
									}
									if(minute.length<2){
										minute='0'+minute;
									}
								}
								html=html+'<input class="ui-field ui-date ui-helper ui-helper-date" target="'+name+'" value="'+date+'" name="helper_date_'+name+'"/>';
								html=html+'<select class="ui-field ui-hour ui-helper ui-helper-hour" target="'+name+'" name="helper_hour_'+name+'">';
								for(var hi=0;hi<24;hi++){
									var hiv=hi>9?(hi+''):('0'+hi);
									html=html+'<option value="'+hiv+'" '+(hiv==hour?'selected="selected"':'')+'>'+hiv+'</option>';
								}
								html=html+'</select>';
								html=html+'<select class="ui-field ui-minute ui-helper ui-helper-minute" target="'+name+'" name="helper_minute_'+name+'">';
								for(var hi=0;hi<60;hi++){
									var hiv=hi>9?(hi+''):('0'+hi);
									html=html+'<option value="'+hiv+'" '+(hiv==minute?'selected="selected"':'')+'>'+hiv+'</option>';
								}
								html=html+'</select>';
								break;
							case 'collect':
								var ref=t.attr('ref'),map=[];
								if(ref!=undefined){
									html=html+'<span name="'+name+'" class="ref-'+ref+'">';
								}else{
									html=html+'<span>';
								}
								if(val!=''){
									map=val.split(',');
								}
								t.find('dictionary').find('option').each(function(){
									var tt=$(this),valval=tt.attr('value')+"";
									if($.inArray(valval,map)===-1){
										html=html+'<input style="width:20px;" type="checkbox" name="'+name+'" value="'+valval+'"/>'+tt.attr('label');
									}else{
										html=html+'<input style="width:20px;" type="checkbox" class="ui-field" checked="checked" name="'+name+'" value="'+valval+'"/>'+tt.attr('label');
									}
								});
								html=html+"</span>";
								break;
							case 'url':
								var dic=t.find('dictionary'),ref=t.attr('ref');
								if(ref&&ref.length>0){requiredParams.push({query:'select[name="'+ref+'"],input[name="'+ref+'"]',name:ref});ref=' ref-'+ref;}else{ref='';}
								var multiSel=(dic.attr('multiSelect')=='true'),labelField=dic.attr('labelField'),
								idField=dic.attr('idField'),dtype=dic.attr('type');
								if(multiSel){
									html=html+'<span idField="'+idField+'" dicType="'+dtype+'" labelField="'+labelField+'" multiSel="'+multiSel+'" url="'+dic.attr('url')+'" class="dictionary_'+name+ref+'">';
									f.find('dictionary').find('option').each(function(){
										var tt=$(this),valval=tt.attr('value');
										html=html+'<input style="width:20px;" type="checkbox" checked="checked" name="'+name+'" value="'+valval+'"/>'+tt.attr('label');
									});
									html=html+'</span><button type="button" class="ui-button-value-select select-type-'+dtype+'" field="'+name+'">'+instance.options.i18n.selectValueLabel+'</button>';
								}else{
									html=html+'<span idField="'+idField+'" dicType="'+dtype+'" labelField="'+labelField+'" multiSel="'+multiSel+'" url="'+dic.attr('url')+'" class="dictionary_'+name+ref+'">';
									if(dic.attr('idValue')){
										html=html+'<input style="width:20px;" readonly="readonly" type="checkbox" checked="checked" name="'+name+'" value="'+dic.attr('idValue')+'"/>'+dic.attr('labelValue');
									}
									html=html+'</span><button type="button" class="ui-button-value-select select-type-'+dtype+'" field="'+name+'">'+instance.options.i18n.selectValueLabel+'</button>';
								}
								break;
							default:
								html=html+'<input class="ui-field" name="'+name+'" value="'+val+'"/>';
							}
							html=html+'</li>';
						});
						html=html+'<li><label></label><button class="ui-search-button" type="button">'+instance.options.i18n.searchLabel+'</button><button class="ui-listall-button" type="button">'+instance.options.i18n.listAllLabel+'</button></li></ul></form>';
						caches._searchbar.html('<div class="ui-search-title">'+instance.options.i18n.searchHeaderLabel+'</div><div class="ui-search-form">'+html+'<div class="clear-both"></div></div>');
						//caches._actionbar.prepend('<div class="ui-list-title">列 表 区 域</div>');
						
						if($.datepicker){
							var now=new Date();
							var yearrange = (now.getFullYear()-60)+":"+(now.getFullYear()+7);
							$('.ui-date',caches._searchbar).datepicker({
								changeMonth: true,changeYear: true,
								dateFormat:instance.options.dateFormat,
								yearRange:yearrange
							});
						}
						$('.ui-helper',caches._searchbar).change(function(){
							var h=$(this),tf=h.attr('target'),tar=$('.ui-field[name="'+tf+'"]',caches._searchbar);
							if(tar&&tar.length==1){
								if(tar.hasClass('ui-date-time')){
									var date=$('.ui-helper[name="helper_date_'+tf+'"]',caches._searchbar),
									hour=$('.ui-helper[name="helper_hour_'+tf+'"]',caches._searchbar);
									minute=$('.ui-helper[name="helper_minute_'+tf+'"]',caches._searchbar);
									tar.val(date.val()+' '+hour.val()+':'+minute.val());
								}
							}
						});
						$('.ui-search-button',caches._searchbar).click(function(){
							caches._isSearch=true;
							instance._loadPage(1);
							instance._loadCount();
						});
						$('.ui-listall-button',caches._searchbar).click(function(){
							caches._isSearch=false;
							instance._loadPage(1);
							instance._loadCount();
						});
						$('.ui-field',caches._searchbar).change(function(){instance._onselectchange($(this),0);});
						for(var j=0;j<requiredParams.length;j++){
							var val=$(requiredParams[j].query,caches._searchbar).val();
							if(val&&val.length>0){
								instance.options.searchParams['__field_'+requiredParams[j].name]=val;
							}
						}
						$('.ui-button-value-select',caches._searchbar).click(function(){
							var t=$(this),fieldName=t.attr('field'),span=$('.dictionary_'+fieldName,caches._searchbar);
							function callback(sels){
								if(sels){
									if(span.attr('multiSel')=='true'){
										var map=[],len=sels.length;
										$('input[type="checkbox"]',span).each(function(){
											var cb=$(this);
											map.push(cb.val());
										});
										for(var i=0;i<len;i++){
											var sel=sels[i],v=sel.value,l=sel.label;
											if($.inArray(v,map)===-1){
												var tmpInput=$('<input style="width:20px;" type="checkbox" checked="checked" name="'+fieldName+'" value="'+v+'"/>');
												span.append(tmpInput);
												span.append('<span class="ui-label">'+l+'</span>');
											}
										}
									}else{
										var v=sels.value,l=sels.label,
										tmpInput=$('<input style="width:20px;" type="checkbox" readonly="readonly" class="ui-field" checked="checked" name="'+fieldName+'" value="'+v+'"/>');
										span.html(tmpInput);
										span.append('<span class="ui-label">'+l+'</span>');
										instance._onselectchange(tmpInput,0);
									}
								}
							}
							if(!instance._trigger('onSelectValue',null,{field:fieldName,span:span,callback:callback})){
								return;
							}
							if($.fn.dialog){
								var sfield=$('.dictionary_'+t.attr('field'),caches._searchbar),
								dialog=$('<div field="'+t.attr('field')+'"></div>');
								dialog.dialog({
									width:600,height:500,title:instance.options.i18n.selectValueLabel,modal:true,
									dialogClass:'ui-dialog-buttonleft',
									buttons:[{
										text:instance.options.i18n.selectValueConfirmLabel,
										click:function(){
											var tt=$(this),sels=null;
											if(sfield.attr('dicType')=='TREE'){sels=tt.tree('selected');
											}else{sels=tt.crud('selected');}
											if(sels){
												var span=$('.dictionary_'+tt.attr('field'),caches._searchbar);
												if(span.attr('multiSel')=='true'){
													var map=[];
													$('input[type="checkbox"]',span).each(function(){
														var cb=$(this);
														map.push(cb.val());
													});
													sels.each(function(){
														var sel=$(this);
														if($.inArray(sel.val(),map)===-1){
															var label=sel.data(span.attr('labelField'));
															if(!label){label=sel.data('label');}
															var input=$('<input style="width:20px;" type="checkbox" checked="checked" name="'+tt.attr('field')+'" value="'+sel.val()+'"/>');
															span.append(input);
															span.append('<span class="ui-label">'+label+'</span>');
														}
													});
												}else{
													var label=sels.data(span.attr('labelField')),input;
													if(!label){label=sels.data('label');}
													input=$('<input style="width:20px;" type="checkbox" readonly="readonly" class="ui-field" checked="checked" name="'+tt.attr('field')+'" value="'+sels.val()+'"/>');
													span.html(input);
													span.append('<span class="ui-label">'+label+'</span>');
													instance._onselectchange(input,0);
												}
											}
											tt.dialog('destroy');
											tt.crud('destroy');
										}
									},{
										text:instance.options.i18n.cancelLabel,
										click:function(){
											$(this).dialog('destroy').crud('destroy');
										}
									}],
									close:function(){$(this).dialog('destroy').crud('destroy');}
								})
								if(t.hasClass('select-type-URL')){
									dialog.crud({
										url:sfield.attr('url'),viewable:false,
										showheader:false,exportable:false,multiselect:(sfield.attr('multiSel')=='true'),
										params:$.extend(true,{},instance.options.params,instance.options.searchParams)
									});
								}else if(t.hasClass('select-type-TREE')&&$.fn.tree){
									dialog.tree({url:sfield.attr('url'),showheader:false,showfooter:false,params:$.extend(true,{},instance.options.params,instance.options.searchParams)});
								}
								
							}else{instance._errparser();}
						});
					}
					if($.fn.button){$('button',caches._searchbar).button();}
					caches._searchable=true;
				}else{
					caches._searchable=false;
					caches._searchbar.hide();
				}
				/* Generate action bar */
				if(instance.options.multiselect||response.attr('multiselect')==='true'){
					caches._multiSelect=true;
				}else{
					caches._multiSelect=false;
				}
				if(caches._multiSelect&&instance.options.selectable){
					/*
					caches._actionbar.append('<button class="ui-selectall-button ui-form-action">'+instance.options.i18n.selectAllLabel+'</button>');
					var selallb=$('button.ui-selectall-button',caches._actionbar);
					if($.fn.button){
						selallb.button();
					}
					function parseSelectAll(){
						var sels=$('input.ui-checkbox',caches._body),selallbthis=$(this),select=selallbthis.data('select');
						selallbthis.data('select',!select);
						if(select){
							if($.fn.button){
								selallbthis.button({'label':instance.options.i18n.selectAllLabel});
							}else{
								selallbthis.text(instance.options.i18n.selectAllLabel);
							}
							sels.each(function(){
								$(this).removeAttr('checked');
							});
						}else{
							if($.fn.button){
								selallbthis.button({'label':instance.options.i18n.unselectAllLabel});
							}else{
								selallbthis.text(instance.options.i18n.unselectAllLabel);
							}
							sels.each(function(){
								$(this).attr('checked','checked');
							});
						}
					}
					selallb.data('select',false);
					selallb.click(parseSelectAll);
					
					if(instance.options.showFooterActionBar){
						caches._actionbar_bottom.append('<button class="ui-selectall-button ui-form-action">'+instance.options.i18n.selectAllLabel+'</button>');
						var selallb=$('button.ui-selectall-button',caches._actionbar_bottom);
						if($.fn.button){
							selallb.button();
						}
						selallb.data('select',false);
						selallb.click(parseSelectAll);
					}
					*/
				}
				if(instance.options.actionHint){
					caches._actionbar.append('<span class="ui-action-hint">'+instance.options.actionHint+'</span>');
				}
				if(response.attr('createable')==='true'&&instance.options.createable){
					caches._actionbar.show();
					caches._actionbar.append('<button class="ui-create-button ui-form-action">'+instance.options.i18n.createLabel+'</button>');
					var cb=$('button.ui-create-button',caches._actionbar);
					if($.fn.button){
						cb.button();
					}
					cb.click(function(){
						instance._loadEdit('new');
					});
					if(instance.options.showFooterActionBar){
						caches._actionbar_bottom.show();
						caches._actionbar_bottom.append('<button class="ui-create-button ui-form-action">'+instance.options.i18n.createLabel+'</button>');
						var cb=$('button.ui-create-button',caches._actionbar_bottom);
						if($.fn.button){
							cb.button();
						}
						cb.click(function(){
							instance._loadEdit('new');
						});
					}
				}
				if(response.attr('editable')==='true'&&instance.options.editable){
					caches._actionbar.show();
					caches._actionbar.append('<button class="ui-edit-button ui-form-action">'+instance.options.i18n.editLabel+'</button>');
					var eb=$('button.ui-edit-button',caches._actionbar);
					if($.fn.button){
						eb.button();
					}
					function parseEdit(){
						var sels=$('input.ui-check:checked',caches._body);
						if(sels.length>0){
							var sel=$(sels[0]);
							instance._loadEdit(sel.val());
						}
					}
					eb.click(parseEdit);
					if(instance.options.showFooterActionBar){
						caches._actionbar_bottom.show();
						caches._actionbar_bottom.append('<button class="ui-edit-button ui-form-action">'+instance.options.i18n.editLabel+'</button>');
						var eb=$('button.ui-edit-button',caches._actionbar_bottom);
						if($.fn.button){
							eb.button();
						}
						eb.click(parseEdit);
					}
				}
				if(response.attr('delable')==='true'&&instance.options.delable){
					caches._actionbar.show();
					caches._actionbar.append('<button class="ui-del-button ui-form-action">'+instance.options.i18n.delLabel+'</button>');
					if($.fn.button){
						var db=$('button.ui-del-button',caches._actionbar)
						db.button();
					}
					db.click(function(event){
						instance._delete(event);
					});
					if(instance.options.showFooterActionBar){
						caches._actionbar_bottom.show();
						caches._actionbar_bottom.append('<button class="ui-del-button ui-form-action">'+instance.options.i18n.delLabel+'</button>');
						if($.fn.button){
							var db=$('button.ui-del-button',caches._actionbar_bottom)
							db.button();
						}
						db.click(function(event){
							instance._delete(event);
						});
					}
				}
				if(instance.options.refreshable){
					caches._actionbar.show();
					caches._actionbar.append('<button class="ui-refresh-button ui-form-action">'+instance.options.i18n.refreshLabel+'</button>');
					var refreshb=$('button.ui-refresh-button',caches._actionbar);
					if($.fn.button){
						refreshb.button();
					}
					function parseRefresh(){
						instance._loadPage(caches._page);
						instance._loadCount();
					}
					refreshb.click(parseRefresh);
					if(instance.options.showFooterActionBar){
						caches._actionbar_bottom.show();
						caches._actionbar_bottom.append('<button class="ui-refresh-button ui-form-action">'+instance.options.i18n.refreshLabel+'</button>');
						var refreshb=$('button.ui-refresh-button',caches._actionbar_bottom);
						if($.fn.button){
							refreshb.button();
						}
						refreshb.click(parseRefresh);
					}
				}
				if(response.attr('exportable')==='true'&&instance.options.exportable){
					caches._actionbar.show();
					caches._actionbar.append('<button class="ui-export-button ui-form-action">'+instance.options.i18n.exportLabel+'</button>');
					var eb=$('button.ui-export-button',caches._actionbar);
					if($.fn.button){
						eb.button();
					}
					function parseExport(event){
						var status=instance._showStatus('<p><button class="export-all">'+instance.options.i18n.exportAllLabel+'</button><button class="export-page">'+instance.options.i18n.exportPageLabel+'</button><button class="cancel">'+instance.options.i18n.cancelLabel+'</button>',event);
						if($.fn.button){
							$('button',status).button();
						}
						$('.export-all',status).click(function(){
							if(caches._isSearch){
								var form=$('form',caches._searchbar);
								if(form){
									var f=form.get(0);
									form.attr('target','export');
									var query=instance._generateQueryString(2);
									query=(query==''?'?':(query+'&'));
									query=query+'__export=true';
									form.attr('action',instance.options.url+'/export/search/0'+query);
									f.submit();
								}
							}else{
								var query=instance._generateQueryString(3);
								query=(query==''?'?':(query+'&'));
								query=query+'pageCount='+instance.options.countPerPage+'&__export=true';
								window.open(instance.options.url+'/export/list/0'+query,'export');
							}
							instance._hideStatus();
						});
						$('.export-page',status).click(function(){
							if(caches._isSearch){
								var form=$('form',caches._searchbar);
								if(form){
									var f=form.get(0);
									form.attr('target','export');
									var query=instance._generateQueryString(2);
									query=(query==''?'?':(query+'&'));
									query=query+'pageCount='+instance.options.countPerPage+'&__export=true';
									form.attr('action',instance.options.url+'/export/search/'+caches._page+query);
									f.submit();
								}
							}else{
								var query=instance._generateQueryString(3);
								query=(query==''?'?':(query+'&'));
								query=query+'pageCount='+instance.options.countPerPage+'&__export=true';
								window.open(instance.options.url+'/export/list/'+caches._page+query,'export');
							}
							instance._hideStatus();
						});
						$('.cancel',status).click(function(){
							instance._hideStatus();
						});
					}
					eb.click(parseExport);
					if(instance.options.showFooterActionBar){
						caches._actionbar_bottom.show();
						caches._actionbar_bottom.append('<button class="ui-export-button ui-form-action">'+instance.options.i18n.exportLabel+'</button>');
						var eb=$('button.ui-export-button',caches._actionbar_bottom);
						if($.fn.button){
							eb.button();
						}
						eb.click(parseExport);
					}
				}				
				
				if(response.attr('paged')==='true'&&instance.options.paged){
					caches._paged=true;
					caches._pagebar.show('slow');
					if(instance.options.showFooterActionBar){
						caches._pagebar_top.show('slow');
					}
				}else{
					caches._paged=false;
					caches._pagebar.hide('slow');
					if(instance.options.showFooterActionBar){
						caches._pagebar_top.hide('slow');
					}
				}
				/*Enter the default view*/
				if(instance.options.initAction=='search'&&instance.options.searchable){
					caches._isSearch=true;
				}else{
					caches._isSearch=false;
				}
				instance._loadPage(1);
				if(caches._paged){
					instance._loadCount();
				}
				instance._enterListView(false);
				instance._hideBusy();
			}else{
				instance._onindexerr(response?response.attr('errMsg'):undefined);
			}
		},
		_onindexerr : function (errMsg){
			if(errMsg==undefined){
				this._errparser();
			}else{
				this._showStatus('<p>'+errMsg+'</p>');
			}
		},
		_loadPage : function (page){
			var target=this, elem=this.element,caches=elem.data('_caches');
			target._showBusy();
			caches._page=page;
			var query=target._generateQueryString(caches._isSearch?2:3);
			query=(query==''?'?':query+'&');
			if(caches._isSearch){
				var form=$('form',caches._searchbar);
				$.post(target.options.url+'/search/'+page+''+query+'pageCount='+target.options.countPerPage,form.serialize(),function(data){target._onlistsuccess(data);},'xml').error(function(){target._onlisterr();});
			}else{
				$.get(target.options.url+'/list/'+page+''+query+'pageCount='+target.options.countPerPage,function(data){target._onlistsuccess(data);},'xml').error(function(){target._onlisterr();});
			}
			/*enable or disable the page button*/
			if($.fn.button){
				$('.ui-firstpage-button',caches._pagebar).button({disabled:(caches._page==1)});
				$('.ui-prepage-button',caches._pagebar).button({disabled:(caches._page<2)});
				$('.ui-nextpage-button',caches._pagebar).button({disabled:(caches._page>=caches._totalPage)});
				$('.ui-lastpage-button',caches._pagebar).button({disabled:(caches._page>=caches._totalPage)});
				if(target.options.showFooterActionBar){
					$('.ui-firstpage-button',caches._pagebar_top).button({disabled:(caches._page==1)});
					$('.ui-prepage-button',caches._pagebar_top).button({disabled:(caches._page<2)});
					$('.ui-nextpage-button',caches._pagebar_top).button({disabled:(caches._page>=caches._totalPage)});
					$('.ui-lastpage-button',caches._pagebar_top).button({disabled:(caches._page>=caches._totalPage)});
				}
			}else{
				if(caches._page<2){
					$('.ui-firstpage-button',caches._pagebar).hide();
					$('.ui-prepage-button',caches._pagebar).hide();
				}else{
					$('.ui-prepage-button',caches._pagebar).show();
					$('.ui-firstpage-button',caches._pagebar).show();
				}
				if(caches._page>=caches._totalPage){
					$('.ui-nextpage-button',caches._pagebar).hide();
					$('.ui-lastpage-button',caches._pagebar).hide();
				}else{
					$('.ui-nextpage-button',caches._pagebar).show();
					$('.ui-lastpage-button',caches._pagebar).show();
				}
				if(target.options.showFooterActionBar){
					if(caches._page<2){
						$('.ui-firstpage-button',caches._pagebar_top).hide();
						$('.ui-prepage-button',caches._pagebar_top).hide();
					}else{
						$('.ui-prepage-button',caches._pagebar_top).show();
						$('.ui-firstpage-button',caches._pagebar_top).show();
					}
					if(caches._page>=caches._totalPage){
						$('.ui-nextpage-button',caches._pagebar_top).hide();
						$('.ui-lastpage-button',caches._pagebar_top).hide();
					}else{
						$('.ui-nextpage-button',caches._pagebar_top).show();
						$('.ui-lastpage-button',caches._pagebar_top).show();
					}
				}
			}
			if(caches._paged){
				$('.ui-page-info input',caches._pagebar).val(caches._page);
				if(target.options.showFooterActionBar){
					$('.ui-page-info input',caches._pagebar_top).val(caches._page);
				}
			}
		},
		/**加载列表数据*/
		_onlistsuccess : function (data){
			var xml=$(data);
			var instance=this, elem=this.element,caches=elem.data('_caches');
			if(instance._checksuccess(xml)){
				var response=xml.find('response');
				var body=caches._body;
				var html='<table class="ui-list">';
				if(instance.options.showListHeader){
					if(instance.options.selectable){
						if(caches._multiSelect){
							html=html+'<thead><tr><th style="width:50px;">'+
										'<div class="ui-select-box">'+
											'<input type="checkbox" id="ui-list-sel-all"/>'+
												'<a href="javascript:void(0)" class="ui-drop-button"></a>'+
													'<div class="ui-select-option">'+
														'<a id="option-select-all" href="javascript:void(0)">全选</a>'+
														'<a id="option-select-cancel" href="javascript:void(0)">不选</a>'+
													'</div></th>';
						}else{
							html=html+'<thead><tr><th style="width:50px;">&nbsp;</th>';
						}
					}else{
						html=html+'<thead><tr>';
					}
					response.find('head').find('field').each(function(){
						var t=$(this);
						html=html+'<th>'+t.attr('label')+'</th>';
					});
					if(instance.options.viewable){
						html=html+'<th></th>';
					}
					html=html+'</tr></thead>';
				}
				caches._rows=[];
				response.find('body').find('row').each(function(){
					var row=$(this);
					var rowData={};
					caches._rows.push(rowData);
					rowData._id=row.attr('id');
					if(instance.options.selectable){
						if(caches._multiSelect){
							html=html+'<tr class="row-'+row.attr('id')+'"><td><input type="checkbox" name="ui_checkbox" value="'+row.attr('id')+'" class="ui-check ui-checkbox"/></td>';
						}else{
							html=html+'<tr class="row-'+row.attr('id')+'"><td><input type="radio" name="ui_radio" value="'+row.attr('id')+'" class="ui-check ui-radio"/></td>';
						}
					}
					row.find('field').each(function(){
						var f=$(this);
						if(!!instance.options.viewLinkField&&instance.options.viewLinkField==f.attr('name')){
							html=html+'<td class="ui-list-column" name="'+f.attr('name')+'"><a rid="'+row.attr('id')+'" href="javascript:void(0);" class="ui-view-link">'+f.attr('value')+'</a></td>';
						}else{
							html=html+'<td class="ui-list-column" name="'+f.attr('name')+'">'+f.attr('value')+'</td>';
						}
						rowData[f.attr('name')]=f.attr('value');
					});
					if(instance.options.viewable&&!instance.options.viewLinkField){
						html=html+'<td><button class="action" rid="'+row.attr('id')+'">'+instance.options.i18n.viewLabel+'</button></td>';
					}
					html=html+'</tr>';
				});
				html=html+'</table>';
				caches._listtable=html;				
				body.html(html);
				$('input.ui-checkbox', body).click(function(){
					var c = $(this);
					if(c.attr('checked')){
						c.parent().parent().addClass('ui-list-selected');						
					}else{
						c.parent().parent().removeClass('ui-list-selected');						
					}
				});
				$('input.ui-radio', body).click(function(){
					var c = $(this);
					$('.ui-list-selected', c.parent().parent().parent() ).removeClass('ui-list-selected');
					c.parent().parent().addClass('ui-list-selected');					
				});
				function parseView(){
					instance._showBusy();
					var query=instance._generateQueryString(3);
					$.get(instance.options.url+'/view/'+instance._urlEncode($(this).attr('rid'))+query,function(data){instance._onviewsuccess(data);},'xml').error(function(){instance._onviewerr();});
				}
				var b=(!!instance.options.viewLinkField)?$('.ui-view-link',body):$('button',body);
				b.click(parseView);
				function parseSelectAll(){
					var sels=$('input.ui-checkbox',caches._body);
					var selallbthis=$(this);
					var select=selallbthis.attr("checked");
					if(!select){		
						sels.each(function(){
							$(this).removeAttr('checked');
							$(this).parent().parent().removeClass('ui-list-selected');	
						});
					}else{						
						sels.each(function(){
							$(this).attr('checked','checked');
							$(this).parent().parent().addClass('ui-list-selected');	
						});
					}
				}
				var al = $("#ui-list-sel-all",body);
				al.click(parseSelectAll);
				
				$(".ui-drop-button").click(function(event){
				    event.stopPropagation();
				    $('.ui-select-box').find(".ui-select-option").toggle();
				    $('.ui-select-box').parent().siblings().find(".ui-select-option").hide();
				});
				$('.ui-select-option > #option-select-all').click(function(){
					var sels=$('input.ui-checkbox',caches._body);
					var selallbthis=$("#ui-list-sel-all",body);
					selallbthis.attr('checked','checked');
					sels.each(function(){
						$(this).attr('checked','checked');
						$(this).parent().parent().addClass('ui-list-selected');	
					});
					$('.ui-select-option').hide();
				});
				$('.ui-select-option > #option-select-cancel').click(function(){
					var sels=$('input.ui-checkbox',caches._body);
					var selallbthis=$("#ui-list-sel-all",body);
					selallbthis.removeAttr('checked');
					sels.each(function(){
						$(this).removeAttr('checked');
						$(this).parent().parent().removeClass('ui-list-selected');	
					});
					$('.ui-select-option').hide();
				});
				$(document).click(function(event){
				    var eo=$(event.target);
				    if($(".ui-select-box").is(":visible") && eo.attr("class")!="ui-select-option" && !eo.parent(".ui-select-option").length)
				        $('.ui-select-option').hide();
				});
				
				instance._hideBusy();
				if($.isFunction(instance.options.listSuccessCallback)){
					instance.options.listSuccessCallback.apply(instance);
				}
			}else{
				instance._onlisterr(response?response.attr('errMsg'):undefined);
			}
		},
		/**错误提示*/
		_onlisterr : function (errMsg){
			if(errMsg==undefined){
				this._errparser();
			}else{
				this._showStatus('<p>'+errMsg+'</p>');
			}
		},
		_loadCount:function(){
			var target=this, elem=this.element,caches=elem.data('_caches');
			if(caches._isSearch){
				var form=$('form',caches._searchbar);
				var query=target._generateQueryString(2);
				$.post(target.options.url+'/search/count'+query,form.serialize(),function(data){target._onloadcountsuccess(data);},'xml').error(function(){target._onloadcounterr();});
			}else{
				var query=target._generateQueryString(3);
				$.get(target.options.url+'/count'+query,function(data){target._onloadcountsuccess(data);},'xml').error(function(){target._onloadcounterr();});
			}
		},
		_onloadcountsuccess: function(data){
			var xml=$(data);
			var instance=this, elem=this.element,caches=elem.data('_caches');
			if(instance._checksuccess(xml)){
				var response=xml.find('response');
				var count=parseInt(response.attr('count'));
				caches._count=count;
				caches._totalPage=parseInt((count%instance.options.countPerPage===0)?(count/instance.options.countPerPage):(count/instance.options.countPerPage+1));
				/* Generate page bar */
				if(caches._paged){
					var html=instance.options.i18n.pageInfoFunc(count);
					html=html+'<button class="ui-firstpage-button">'+instance.options.i18n.firstPageLabel+'</button>';
					html=html+'<button class="ui-prepage-button">'+instance.options.i18n.prePageLabel+'</button>';
					html=html+'<span class="ui-page-info"><input type="input" value="'+caches._page+'" style="width:30px;text-align:center;"/>/'+caches._totalPage+'</span>';
					html=html+'<button class="ui-nextpage-button">'+instance.options.i18n.nextPageLabel+'</button>';
					html=html+'<button class="ui-lastpage-button">'+instance.options.i18n.lastPageLabel+'</button>';
					caches._pagebar.html(html+'<span class="ui-per-page">每页共显示<input type="input" style="width:30px;text-align:center;" value="'+instance.options.countPerPage+'"/>条</span>');
					$('.ui-per-page input',caches._pagebar).change(function(){
						var t=$(this),val=t.val();
						if(!!val){
							instance.options.countPerPage=parseInt(val);
							instance._loadPage(1);
							instance._loadCount();
						}
					});
					var fb=$('.ui-firstpage-button',caches._pagebar);
					if($.fn.button){
						fb.button({disabled:(caches._page==1)});
					}else{
						if(caches._page==1){
							fb.hide();
						}else{
							fb.show();
						}
					}
					fb.click(function(){
						instance._loadPage(1);
					});
					var pb=$('.ui-prepage-button',caches._pagebar);
					if($.fn.button){
						pb.button({disabled:(caches._page<2)});
					}else{
						if(caches._page<2){
							pb.hide();
						}else{
							pb.show();
						}
					}
					pb.click(function(){
						instance._loadPage(caches._page-1);
					});
					$('.ui-page-info input').change(function(){
						try{
							var page=parseInt($(this).val());
							if(page>0&&page<=caches._totalPage){
								instance._loadPage(page);
							}
						}catch(err){
						}
					});
					var nb=$('.ui-nextpage-button',caches._pagebar);
					if($.fn.button){
						nb.button({disabled:(caches._page>=caches._totalPage)});
					}else{
						if(caches._page>=caches._totalPage){
							nb.hide();
						}else{
							nb.show();
						}
					}
					nb.click(function(){
						instance._loadPage(caches._page+1);
					});
					
					var lb=$('.ui-lastpage-button',caches._pagebar);
					if($.fn.button){
						lb.button({disabled:(caches._page==caches._totalPage)});
					}else{
						if(caches._page==caches._totalPage){
							lb.hide();
						}else{
							lb.show();
						}
					}
					lb.click(function(){
						instance._loadPage(caches._totalPage);
					});
					if(instance.options.showFooterActionBar){
						caches._pagebar_top.html(html);
						var fb=$('.ui-firstpage-button',caches._pagebar_top);
						if($.fn.button){
							fb.button({disabled:(caches._page==1)});
						}else{
							if(caches._page==1){
								fb.hide();
							}else{
								fb.show();
							}
						}
						fb.click(function(){
							instance._loadPage(1);
						});
						var pb=$('.ui-prepage-button',caches._pagebar_top);
						if($.fn.button){
							pb.button({disabled:(caches._page<2)});
						}else{
							if(caches._page<2){
								pb.hide();
							}else{
								pb.show();
							}
						}
						pb.click(function(){
							instance._loadPage(caches._page-1);
						});
						$('.ui-page-info input',caches._pagebar_top).change(function(){
							try{
								var page=parseInt($(this).val());
								if(page>0&&page<=caches._totalPage){
									instance._loadPage(page);
								}
							}catch(err){
							}
						});
						var nb=$('.ui-nextpage-button',caches._pagebar_top);
						if($.fn.button){
							nb.button({disabled:(caches._page>=caches._totalPage)});
						}else{
							if(caches._page>=caches._totalPage){
								nb.hide();
							}else{
								nb.show();
							}
						}
						nb.click(function(){
							instance._loadPage(caches._page+1);
						});
						var lb=$('.ui-lastpage-button',caches._pagebar_top);
						if($.fn.button){
							lb.button({disabled:(caches._page==caches._totalPage)});
						}else{
							if(caches._page==caches._totalPage){
								lb.hide();
							}else{
								lb.show();
							}
						}
						lb.click(function(){
							instance._loadPage(caches._totalPage);
						});
					}
					
				}
			}else{
				instance._onloadcounterr(response?response.attr('errMsg'):undefined);
			}
		},
		/**错误消息*/
		_onloadcounterr: function(errMsg){
			if(errMsg==undefined){
				this._errparser();
			}else{
				this._showStatus('<p>'+errMsg+'</p>');
			}
		},
		_loadEdit:function(action){
			var target=this, elem=this.element,caches=elem.data('_caches');
			target._showBusy();
			var url;
			if(action==='new'){
				var query=this._generateQueryString(1);
				url=this.options.url+'/create'+query;
			}else{
				var query=this._generateQueryString(1);
				url=this.options.url+'/edit/'+action+query;
			}
			$.get(url,function(data){target._onloadeditsuccess(data);},'xml').error(function(){target._onloadediterr();});
		},
		_onloadeditsuccess: function(data){
			var xml=$(data);
			var instance=this, elem=this.element,caches=elem.data('_caches');
			if(instance._checksuccess(xml)){
				var response=xml.find('response');
				instance._enterListView(true);
				/*Generate the form*/
				var html='<form method="post" class="column-'+instance.options.editListColumn+'">';
				var id=response.attr('id');
				if(id!=undefined){
					html=html+'<input type="hidden" name="'+response.attr('idField')+'" value="'+id+'"/>';
				}
				html=html+"<table><tr>"
				var interval=1;
				var fields=response.find('field');
				var requiredParams=[];
				fields.each(function(){
					var f=$(this);
					var val=f.attr('value');
					var name=f.attr('name');
					var readonly=f.attr('readonly');
					html=html+'<td style="text-align:right;width:60px;"><label class="ui-required-'+f.attr('required')+'">'+f.attr('label')+'</label></td><td class="ui-field-'+name+'" style="text-align:left;">';
					if(val===undefined){
						val='';
					}
					switch(f.attr('dataType')){
					case 'captcha':
						html=html+'<input class="ui-field ui-captcha" style="width:100px;" name="'+name+'"/><span><img src="'+instance.options.captchaImgUrl+'?'+(new Date()).getTime()+'" style="vertical-align: middle;width:120px;"/><button class="ui-button-update-captcha" type="button">看不清？</button></span>';
						break;
					case 'input':
						if(readonly){
							html=html+'<input type="hidden" name="'+name+'" value="'+val+'"/>'+val;
						}else{
							html=html+'<input class="ui-field" name="'+name+'" value="'+val+'"/>';
						}					
						break;
					case 'password':
						html=html+'<input type="password" class="ui-field" name="'+name+'" value="'+val+'"/>';
						break;
					case 'file':
						if(f.attr('isImage')=='true'&&f.attr('enableCutImage')=='true'){
							var tmpspanid='file_span_'+(new Date()).getTime()+'_'+name;
							if(val&&val!=''){
								__crud_image_data_cache[tmpspanid]=val;
							}
							html=html+'<span class="ui-file-cut" name="'+name+'"><span class="ui-file-cut-preview-span" id="'+tmpspanid+'"></span><input name="'+name+'" type="hidden" value="'+val+'"/><button type="button" class="ui-file-cut-btn" w="'+f.attr('cutImageWidth')+'" h="'+f.attr('cutImageHeight')+'">'+instance.options.i18n.selectValueLabel+'</button></span>'
						}else{
							html=html+'<span class="ui-file" name="'+name+'" value="'+val+'"/>';
						}
						break;
					case 'select':
						if(readonly){
							f.find('dictionary').find('option').each(function(){
								var tt=$(this);
								var valval=tt.attr('value');
								if(val===valval){
									html=html+'<input  type="hidden"  name="'+name+'" value="'+valval+'"/>'+tt.attr('label');
								}
							});
						}else{
							var ref=f.attr('ref');
							if(ref!=undefined){
								requiredParams.push({query:'select[name="'+ref+'"],input[name="'+ref+'"]',name:ref});
								html=html+'<select name="'+name+'" class="ui-field ref-'+ref+'">';
							}else{
								html=html+'<select name="'+name+'" class="ui-field">';
							}						
							f.find('dictionary').find('option').each(function(){
								var tt=$(this);
								var valval=tt.attr('value');
								if(val===valval){
									html=html+'<option selected="selected" value="'+valval+'">'+tt.attr('label')+'</option>';
								}else{
									html=html+'<option value="'+valval+'">'+tt.attr('label')+'</option>';
								}
							});
							html=html+'</select>';
						}						
						break;
					case 'textarea':
						if(readonly){
							html=html+'<input type="hidden" name="'+name+'" value="'+val+'"/>'+val;
						}else{
							html=html+'<textarea class="ui-field-text" name="'+name+'">'+val+'</textarea>';
						}
						break;
					case 'richtext':
						if(readonly){
							html=html+'<input type="hidden" name="'+name+'" value="'+val+'"/>'+val;
						}else{
							html=html+'<div class="ui-field-rich"><textarea class="ui-field-rich-text" style="width:100%;" name="'+name+'">'+val+'</textarea></div>';
						}
						break;
					case 'bigrichtext':
						if(readonly){
							html=html+'<input type="hidden" name="'+name+'" value="'+val+'"/>'+val;
						}else{
							html=html+'<div class="ui-field-big-rich"><textarea class="ui-field-big-rich-text" style="width:100%;height:400px;" name="'+name+'">'+val+'</textarea></div>';
						}
						break;
					case 'date':
						if(readonly){
							html=html+'<input type="hidden" name="'+name+'" value="'+val+'"/>'+val;
						}else{
							html=html+'<input class="ui-field ui-date" name="'+name+'" value="'+val+'"/>';
						}
						break;
					case 'time':
						if(readonly){
							html=html+'<input type="hidden" name="'+name+'" value="'+val+'"/>'+val;
						}else{
							html=html+'<input class="ui-field ui-time" name="'+name+'" value="'+val+'"  style="width:40px;"/>';
						}
						break;
					case 'datetime':
						if(readonly){
							html=html+'<input type="hidden" name="'+name+'" value="'+val+'"/>'+val;
						}else{
							html=html+'<input type="hidden" class="ui-field ui-date-time" name="'+name+'" value="'+val+'"/>';
							var date='',hour='',minute='';
							if(val!=''){
								var vals=val.split(' ');
								date=vals[0];time=vals[1].split(':'),hour=time[0],minute=time[1];
								if(hour.length<2){
									hour='0'+hour;
								}
								if(minute.length<2){
									minute='0'+minute;
								}
							}
							html=html+'<input class="ui-field ui-date ui-helper ui-helper-date" target="'+name+'" value="'+date+'" name="helper_date_'+name+'"/>';
							html=html+'<select class="ui-field ui-hour ui-helper ui-helper-hour" target="'+name+'" name="helper_hour_'+name+'">';
							for(var hi=0;hi<24;hi++){
								var hiv=hi>9?(hi+''):('0'+hi);
								html=html+'<option value="'+hiv+'" '+(hiv==hour?'selected="selected"':'')+'>'+hiv+'</option>';
							}
							html=html+'</select>';
							html=html+'<select class="ui-field ui-minute ui-helper ui-helper-minute" target="'+name+'" name="helper_minute_'+name+'">';
							for(var hi=0;hi<60;hi++){
								var hiv=hi>9?(hi+''):('0'+hi);
								html=html+'<option value="'+hiv+'" '+(hiv==minute?'selected="selected"':'')+'>'+hiv+'</option>';
							}
							html=html+'</select>';
						}
						break;
					case 'collect':
						var ref=f.attr('ref');
						if(ref!=undefined){
							html=html+'<span name="'+name+'" class="ref-'+ref+'">';
						}else{
							html=html+'<span>';
						}
						var map=[];
						if(val!=''){
							map=val.split(',');
						}
						f.find('dictionary').find('option').each(function(){
							var tt=$(this);
							var valval=tt.attr('value')+"";
							if($.inArray(valval,map)===-1){
								html=html+'<input style="width:20px;" type="checkbox" name="'+name+'" value="'+valval+'"/>'+tt.attr('label');
							}else{
								html=html+'<input style="width:20px;" type="checkbox" checked="checked" name="'+name+'" value="'+valval+'"/>'+tt.attr('label');
							}
						});
						html=html+"</span>";
						break;
					case 'url':
						var dic=f.find('dictionary');
						var ref=f.attr('ref');
						if(ref&&ref.length>0){requiredParams.push({query:'select[name="'+ref+'"],input[name="'+ref+'"]',name:ref});ref=' ref-'+ref;}else{ref='';}
						var multiSel=(dic.attr('multiSelect')=='true');
						var labelField=dic.attr('labelField');
						var idField=dic.attr('idField');
						var dtype=dic.attr('type');
						if(multiSel){
							html=html+'<span idField="'+idField+'" dicType="'+dtype+'" labelField="'+labelField+'" multiSel="'+multiSel+'" url="'+dic.attr('url')+'" class="dictionary_'+name+ref+'">';
							f.find('dictionary').find('option').each(function(){
								var tt=$(this);
								var valval=tt.attr('value');
								html=html+'<input style="width:20px;" type="checkbox" checked="checked" name="'+name+'" value="'+valval+'"/>'+tt.attr('label');
							});
							html=html+'</span><button type="button" class="ui-button-value-select select-type-'+dtype+'" field="'+name+'">'+instance.options.i18n.selectValueLabel+'</button>';
						}else{
							html=html+'<span idField="'+idField+'" dicType="'+dtype+'" labelField="'+labelField+'" multiSel="'+multiSel+'" url="'+dic.attr('url')+'" class="dictionary_'+name+ref+'">';
							if(dic.attr('idValue')){
								html=html+'<input style="width:20px;" type="radio" readonly="readonly" class="ui-field" checked="checked" name="'+name+'" value="'+dic.attr('idValue')+'"/>'+dic.attr('labelValue');
							}
							html=html+'</span><button type="button" class="ui-button-value-select select-type-'+dtype+'" field="'+name+'">'+instance.options.i18n.selectValueLabel+'</button>';
						}
						break;
					default:
						html=html+'<input class="ui-field" name="'+name+'" value="'+val+'"/>';
					}
					html=html+'<span class="ui-field-desc">'+f.attr('description')+'</span><span class="ui-error-msg err_msg_'+name+'"></span></td>';
					var c=interval%instance.options.editListColumn;
					if(c==0){html=html+'</tr><tr>';}
					interval++;
				});
				var c=fields.length%instance.options.editListColumn;
				var left=instance.options.editListColumn-c;
				if(c!=0){
					for(var i=0;i<left;i++){
						html=html+'<td>&nbsp;</td>';
					}
					html=html+'</tr>';
				}
				html=html+'<tr><td><label></label></td><td style="text-align:left;"><button type="button" class="ui-button-return">'+instance.options.i18n.returnLabel+'</button><button type="button" class="ui-button-submit">'+instance.options.i18n.submitLabel+'</button></td>'
				if(c!=0){
					for(var i=0;i<left;i++){
						html=html+'<td>&nbsp;</td>';
					}
				}
				html=html+'</tr></table></form>';
				caches._body.html(html);
				$('.ui-file-cut-preview-span',caches._body).each(function(){
					var imgViewSpan=$(this),spanid=imgViewSpan.attr('id');
					imgViewSpan.flash({width:114,height:156,encodeParams:true,swf:(instance.options.imageViewSwf+'?_='+(new Date()).getTime()),flashvars:{id:spanid}});
				});
				if($.datepicker){
					var now=new Date();
					var yearrange = (now.getFullYear()-60)+":"+(now.getFullYear()+7);
					$('.ui-date',caches._body).datepicker({
						changeMonth: true,changeYear: true,
						dateFormat:instance.options.dateFormat,
						yearRange:yearrange
					});
				}
				$('.ui-button-update-captcha',caches._body).click(function(){
					var t=$(this);
					var img=$('img',t.parent());
					img.attr('src',instance.options.captchaImgUrl+'?_'+(new Date()).getTime());
				});
				$('.ui-helper',caches._body).change(function(){
					var h=$(this);
					var tf=h.attr('target');
					var tar=$('.ui-field[name="'+tf+'"]',caches._body);
					if(tar&&tar.length==1){
						if(tar.hasClass('ui-date-time')){
							var date=$('.ui-helper[name="helper_date_'+tf+'"]',caches._body);
							var hour=$('.ui-helper[name="helper_hour_'+tf+'"]',caches._body);
							var minute=$('.ui-helper[name="helper_minute_'+tf+'"]',caches._body);
							tar.val(date.val()+' '+hour.val()+':'+minute.val());
						}
					}
				});
				$('.ui-file-cut-btn',caches._body).click(function(){
					var t=$(this);
					var id=(new Date()).getTime();
					var field=t.parent().attr('name');
					var dialog=$('<div></div>');
					dialog.append('<p>您的浏览器没有安装flash插件，请在本网站的“资料下载”处下载对应flash插件并安装。</p>');
					dialog.append('<p>若本网站提供的flash插件不适用于您的浏览器，请登录官方网站下载，下载地址为：<a href="http://get.adobe.com/cn/flashplayer/" target="_blank">http://get.adobe.com/cn/flashplayer/</a>。</p>');
					if(t.data('_id')){
						id=t.data('_id');
					}else{
						t.data('_id',id);
					}
					__crud_image_cut_callback_cache['crud_'+id+'_'+field]={callback:function(field,base64){
						var input=$('input[name="'+field+'"]',caches._body);
						$(this).dialog('destroy');
						input.val(base64);
						var span=$('span.ui-file-cut-preview-span',input.parent()),tmpid=span.attr('id');
						__crud_image_data_cache[tmpid]=base64;
						span.flash({width:114,height:156,encodeParams:true,swf:(instance.options.imageViewSwf+'?_='+(new Date()).getTime()),flashvars:{id:tmpid}});
					},target:dialog};
					
					dialog.dialog({title:'选择和裁剪图片',width:550,height:500,
						close:function(){
							$(this).dialog('destroy');;
						},
						buttons:{'关闭':function(){$(this).dialog('destroy');}}});
					dialog.flash({
							swf:instance.options.imageCutSwf,
							width:480,
							height:380,
							flashvars:{targetID:id+'',fieldName:field,width:t.attr('w'),height:t.attr('h')}
						});
				});
				if($.fn.button){
					$('button',caches._body).button();
				}
				for(var j=0;j<requiredParams.length;j++){
					var val=$(requiredParams[j].query,caches._body).val();
					if(val&&val.length>0){
						instance.options.editParams['__field_'+requiredParams[j].name]=val;
					}
				}
				$('.ui-button-value-select',caches._body).click(function(){
					var t=$(this),fieldName=t.attr('field'),span=$('.dictionary_'+fieldName,caches._body);
					function callback(sels){
						if(sels){
							if(span.attr('multiSel')=='true'){
								var map=[],len=sels.length;
								$('input[type="checkbox"]',span).each(function(){
									var cb=$(this);
									map.push(cb.val());
								});
								for(var i=0;i<len;i++){
									var sel=sels[i],v=sel.value,l=sel.label;
									if($.inArray(v,map)===-1){
										var tmpInput=$('<input style="width:20px;" type="checkbox" checked="checked" name="'+fieldName+'" value="'+v+'"/>');
										span.append(tmpInput);
										span.append('<span class="ui-label">'+l+'</span>');
									}
								}
							}else{
								var v=sels.value,l=sels.label,
								tmpInput=$('<input style="width:20px;" type="checkbox" readonly="readonly" class="ui-field" checked="checked" name="'+fieldName+'" value="'+v+'"/>');
								span.html(tmpInput);
								span.append('<span class="ui-label">'+l+'</span>');
								instance._onselectchange(tmpInput,1);
							}
						}
					}
					if(!instance._trigger('onSelectValue',null,{field:fieldName,span:span,callback:callback})){
						return;
					}
					if($.fn.dialog){
						var sfield=$('.dictionary_'+t.attr('field'),caches._body);
						var dialog=$('<div field="'+t.attr('field')+'"></div>');
						dialog.dialog({
							width:600,height:500,title:instance.options.i18n.selectValueLabel,modal:true,
							dialogClass:'ui-dialog-buttonleft',
							buttons:[{
								text:instance.options.i18n.selectValueConfirmLabel,
								click:function(){
									var tt=$(this);
									var sels=null;
									if(sfield.attr('dicType')=='TREE'){
										sels=tt.tree('selected');
									}else{
										sels=tt.crud('selected');
									}
									if(sels){
										var span=$('.dictionary_'+tt.attr('field'),caches._body);
										if(span.attr('multiSel')=='true'){
											var map=[];
											$('input[type="checkbox"]',span).each(function(){
												var cb=$(this);
												map.push(cb.val());
											});
											sels.each(function(){
												var sel=$(this);
												if($.inArray(sel.val(),map)===-1){
													var label=sel.data(span.attr('labelField'));
													if(!label){
														label=sel.data('label');
													}
													span.append('<input style="width:20px;" type="checkbox" checked="checked" name="'+tt.attr('field')+'" value="'+sel.val()+'"/>'+label);
												}
											});
										}else{
											var label=sels.data(span.attr('labelField'));
											if(!label){
												label=sels.data('label');
											}
											var input=$('<input style="width:20px;" class="ui-field" type="radio" readonly="readonly" checked="checked" name="'+tt.attr('field')+'" value="'+sels.val()+'"/>');
											span.html(input);
											span.append('<span class="ui-label">'+label+'</span>');
											instance._onselectchange(input,1);
										}
									}
									tt.dialog('destroy');
									tt.crud('destroy');
								}
							},
							{
								text:instance.options.i18n.cancelLabel,
								click:function(){
									var tt=$(this);tt.dialog('destroy');tt.crud('destroy');
								}
							}],
							close:function(){var tt=$(this);tt.dialog('destroy');tt.crud('destroy');}
						});
						if(t.hasClass('select-type-URL')){
							dialog.crud({
								url:sfield.attr('url'),viewable:false,
								showheader:false,exportable:false,multiselect:(sfield.attr('multiSel')=='true')
								,params:$.extend(true,{},instance.options.params,instance.options.editParams)
							});
						}else if(t.hasClass('select-type-TREE')&&$.fn.tree){
							dialog.tree({url:sfield.attr('url'),showheader:false,showfooter:false,params:$.extend(true,{},instance.options.params,instance.options.editParams)});
						}
					}else{
						instance._errparser();
					}
				});
				var returnBtn=$('.ui-button-return',caches._body);
				if(instance.options.initAction=='list'){
					returnBtn.click(function(){
						instance._enterListView(false);
						caches._body.html(caches._listtable);
						$('button',caches._body).click(function(){
							instance._showBusy();
							var query=instance._generateQueryString(3);
							$.get(instance.options.url+'/view/'+instance._urlEncode($(this).attr('rid'))+query,function(data){instance._onviewsuccess(data);},'xml').error(function(){instance._onviewerr();});
						});
					});
				}else if(instance.options.initAction=='view'){
					returnBtn.click(function(){
						instance._showBusy();
						var query=instance._generateQueryString(3);
						$.get(instance.options.url+'/view/'+instance._urlEncode(instance.options.viewTarget)+query,function(data){instance._onviewsuccess(data);},'xml').error(function(){instance._onviewerr();});
					});
				}
				if(instance.options.initAction=='edit'){/*在编辑模式的时候不需要返回按钮*/
					returnBtn.hide();
				}
				$('.ui-button-submit',caches._body).click(function(){
					var form=$('form',caches._body);
					var query=instance._generateQueryString(1);
					instance._showBusy();
					$.post(instance.options.url+'/save'+query,form.serialize(),function(data){instance._onsavesuccess(data);},'xml').error(function(){instance._onsaveerr();});
				});
				
				$('.ui-field',caches._body).change(function(){instance._onselectchange($(this),1);});
				if($.fn.upload){
					$('.ui-file',caches._body).upload({params:instance.options.params,url:instance.options.url+'/upload'});
				}
				
				if($.fn.tinymce){
					$('.ui-field-rich-text',caches._body).tinymce({
						script_url:instance.options.tinymceUrl,theme:'advanced',
						theme_advanced_buttons1 : 'undo,redo,separator,bold,italic,underline,strikethrough,separator,fontselect,fontsizeselect,separator,link,unlink,separator,forecolor,backcolor',
						theme_advanced_buttons2 : '',
					    theme_advanced_buttons3 : '',
						theme_advanced_toolbar_location : 'top',
				        theme_advanced_toolbar_align : 'left',
				        language:'zh-cn',
				        theme_advanced_fonts : '宋体="宋体","华文宋体",arial;黑体="黑体","华文细黑",arial;楷体="楷体","华文楷体",arial;微软雅黑="微软雅黑","华文细黑",arial'
					});
					$('.ui-field-big-rich-text',caches._body).tinymce({
						script_url:instance.options.tinymceUrl,theme:'advanced',
						plugins : 'insertdatetime,table,advimage,media',
						theme_advanced_buttons1 : 'undo,redo,separator,sub,sup,separator,bold,italic,underline,strikethrough,separator,fontselect,fontsizeselect,separator,link,unlink,anchor,code,separator,forecolor,backcolor',
						theme_advanced_buttons2 : 'inserttime,insertdate,separator,tablecontrols,separator,image,media,separator,justifyleft,justifycenter,justifyright,outdent,indent',
					    theme_advanced_buttons3 : '',
						theme_advanced_toolbar_location : 'top',
				        theme_advanced_toolbar_align : 'left',
				        convert_urls : false,
				        language :'zh-cn',
				        file_browser_callback:function(field_name, url, type, win){instance._openFileBrowser(field_name, url, type, win);},
				        theme_advanced_fonts : '宋体="宋体","华文宋体",arial;黑体="黑体","华文细黑",arial;楷体="楷体","华文楷体",arial;微软雅黑="微软雅黑","华文细黑",arial'
					});
				}
				instance._hideBusy();
				if( instance.options.initEditCallback && $.isFunction(instance.options.initEditCallback) ){
					instance.options.initEditCallback.apply(elem);
				}
			}else{
				instance._onloadediterr(response?response.attr('errMsg'):undefined);
			}
		},
		_onloadediterr: function(errMsg){
			if(errMsg==undefined){
				this._errparser();
			}else{
				this._showStatus('<p>'+errMsg+'</p>');
			}
		},
		_onsavesuccess:function(data){
			var xml=$(data);
			var instance=this, elem=this.element,caches=elem.data('_caches');
			instance._hideBusy();
			var response=xml.find('response');
			if(instance._checksuccess(xml)){
				instance.options.editTarget=response.attr('entityID');
				if(instance.options.editSuccessCallback&&$.isFunction(instance.options.editSuccessCallback)){
					instance.options.editSuccessCallback.apply(elem,[response]);
				}else if(instance.options.initAction=='list'){/*return to the list*/
					{
						instance._enterListView(false);
						if(instance.options.autoRefresh){
							instance._loadPage(caches._page);
							instance._loadCount();
						}else{
							caches._body.html(caches._listtable);
							$('button',caches._body).click(function(){
								instance._showBusy();
								var query=instance._generateQueryString(3);
								$.get(instance.options.url+'/view/'+instance._urlEncode($(this).attr('rid'))+query,function(data){instance._onviewsuccess(data);},'xml').error(function(){instance._onviewerr();});
							});
						}
					}
				}else if(instance.options.initAction=='edit'||instance.options.initAction=='view'){
					if(instance.options.editSuccessCallback&&$.isFunction(instance.options.editSuccessCallback)){
						instance.options.editSuccessCallback.apply(elem,[response]);
					}else{
						var query=instance._generateQueryString(3);
						$.get(instance.options.url+'/view/'+instance._urlEncode(instance.options.editTarget)+query,function(data){instance._onviewsuccess(data);},'xml').error(function(){instance._onviewerr();});
					}
				}
				$('.ui-state-error',caches._body).remove();
			}else{
				$('.ui-button-update-captcha',caches._body).trigger('click');
				if(response!=undefined){
					/*validation fail*/
					var err=$('.ui-state-error',caches._body);
					if(err.length<=0){
						var html='<div class="ui-state-error ui-corner-all">';
						html=html+'<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: 0.3em;"></span>'+instance.options.i18n.saveFailInfo+'</p></div>';
						caches._body.prepend(html);
					}
					$('.ui-error-msg',caches._body).html('');
					response.find('message').each(function(){
						/*append validation result to fields.*/
						var msg=$(this);
						$('.err_msg_'+msg.attr('field'),caches._body).append('<span class="ui-state-error ui-corner-all">'+msg.attr('content')+'</span>');
					});
					instance._hideBusy();
				}else{
					instance._onsaveerr(response?response.attr('errMsg'):undefined);
				}
			}
		},
		_onsaveerr:function(errMsg){
			if(errMsg==undefined){
				this._errparser();
			}else{
				this._showStatus('<p>'+errMsg+'</p>');
			}
		},
		_onviewsuccess:function(data){
			var xml=$(data);
			var instance=this, elem=this.element,caches=elem.data('_caches');
			var response=xml.find('response');
			if(instance._checksuccess(xml)){
				instance._enterListView(true);
				var html='<form><table class="ui-view"><tr>';
				var interval=1;
				var fields=response.find('field');
				fields.each(function(){
					var f=$(this);
					var fname=f.attr('name');
					if(f.attr('file')=='true'){
						if(f.attr('isImage')=='true'){
							if(f.attr('enableCutImage')=='true'){
								var tmpvalue=f.attr('value'),tmpid='img_view_'+(new Date()).getTime()+'_'+fname;
								if(tmpvalue&&tmpvalue!=''){
									__crud_image_data_cache[tmpid]=tmpvalue;
								}
								html=html+'<td class="ui-field-label field-'+fname+'"><label>'+f.attr('label')+'</label></td><td class="field-'+fname+' field-value"><span id="'+tmpid+'" class="ui-file-cut-preview-span"></span></td>';
							}else{
								html=html+'<td class="ui-field-label field-'+fname+'"><label>'+f.attr('label')+'</label></td><td class="field-'+fname+' field-value"><img src="'+instance.options.url+'/viewfile/'+response.attr('id')+'/'+f.attr('name')+'"/></td>';
							}
						}else{
							html=html+'<td class="ui-field-label field-'+fname+'"><label>'+f.attr('label')+'</label></td><td class="field-'+fname+' field-value">'+f.attr('value')+'<a href="'+instance.options.url+'/viewfile/'+response.attr('id')+'/'+f.attr('name')+'?inline=yes" target="crud_file">'+instance.options.i18n.viewFileLabel+'</a> | <a href="'+instance.options.url+'/viewfile/'+response.attr('id')+'/'+f.attr('name')+'" target="crud_file">'+instance.options.i18n.downloadFileLabel+'</a></td>';
						}
					}else{
						html=html+'<td class="ui-field-label field-'+fname+'"><label>'+f.attr('label')+'</label></td><td class="field-'+fname+' field-value">'+f.attr('value')+'</td>';
					}
					var c=interval%instance.options.viewListColumn;
					if(c==0){
						html=html+'</tr><tr>';
					}
					interval++;
				});
				var c=fields.length%instance.options.viewListColumn;
				var left=instance.options.viewListColumn-c;
				if(c!=0){
					for(var i=0;i<left;i++){
						html=html+'<td>&nbsp;</td>';
					}
					html=html+'</tr>';
				}
				html=html+'<tr><td><label></label></td><td style="text-align:left;"><button type="button" class="ui-button-return">'+instance.options.i18n.returnLabel+'</button></td>';
				if(c!=0){
					for(var i=0;i<left;i++){
						html=html+'<td>&nbsp;</td>';
					}
				}
				html=html+'</tr></table></form>';
				caches._body.html(html);
				$('.ui-file-cut-preview-span',caches._body).each(function(){
					var imgViewSpan=$(this),tmpid=imgViewSpan.attr('id');
					imgViewSpan.flash({width:114,height:156,encodeParams:true,swf:(instance.options.imageViewSwf+'?_='+(new Date()).getTime()),flashvars:{id:tmpid}});
				});
				var rb=$('.ui-button-return',caches._body);
				if($.fn.button){
					rb.button();
				}
				rb.click(function(){
					instance._enterListView(false);
					if(instance.options.autoRefresh){
						instance._loadPage(caches._page);
						instance._loadCount();
					}else{
						caches._body.html(caches._listtable);
						$('button',caches._body).click(function(){
							instance._showBusy();
							var query=instance._generateQueryString(3);
							$.get(instance.options.url+'/view/'+instance._urlEncode($(this).attr('rid'))+query,function(data){instance._onviewsuccess(data);},'xml').error(function(){instance._onviewerr();});
						});
					}
				});
				if(instance.options.initAction!='list'){
					rb.hide();
				}
				instance._hideBusy();
			}else{
				instance._onviewerr(response?response.attr('errMsg'):undefined);
			}
		},
		_onviewerr:function(errMsg){
			if(errMsg==undefined){
				this._errparser();
			}else{
				this._showStatus('<p>'+errMsg+'</p>');
			}
		},
		_delete:function(event){
			var instance=this, elem=this.element,caches=elem.data('_caches');
			var sels=$('input.ui-check:checked',caches._body);
			caches._deleteCount=sels.length;
			if(caches._deleteCount>0){
				var status=instance._showStatus('<p>您确认要删除吗？<br/><button type="button" class="ui-btn-confirm">确认</button><button type="button"  class="ui-btn-cancel">取消</button></p>',event);
				$('.ui-btn-confirm',status).click(function(){
					instance._showBusy();
					sels.each(function(){
						var c=$(this);
						var query=instance._generateQueryString(1);
						$.get(instance.options.url+'/del/'+instance._urlEncode(c.val())+query,function(data){instance._ondeletesucess(data);},'xml').error(function(){instance._ondeleteerr();});
					});
				});
				$('.ui-btn-cancel',status).click(function(){
					instance._hideStatus();
				});
			}
		},
		_ondeletesucess:function(data){
			var xml=$(data);
			var instance=this, elem=this.element,caches=elem.data('_caches');
			var response=xml.find('response');
			if(instance._checksuccess(xml)){
				caches._deleteCount=caches._deleteCount-1;
				/*disable the row the the current table*/
				var id=response.attr('id');
				$('.row-'+id,caches._body).addClass('ui-state-disabled');
				if(instance.options.autoRefresh){
					if(caches._deleteCount<=0){
						/*refresh the list*/
						instance._loadPage(caches._page);
						instance._loadCount();
					}
				}
				instance._hideBusy();
			}else{
				if(response!=undefined){
					var id=response.attr('id');
					$('.row-'+id,caches._body).addClass('ui-state-error');
				}
				instance._ondeleteerr(response?response.attr('errMsg'):undefined);
			}
		},
		_ondeleteerr:function(errMsg){
			var elem=this.element,caches=elem.data('_caches');
			caches._deleteCount=caches._deleteCount-1;
			if(errMsg==undefined){
				this._errparser();
			}else{
				this._showStatus('<p>'+errMsg+'</p>');
			}
		},
		_onselectchange:function(target,where){
			var instance=this,elem=this.element,caches=elem.data('_caches');
			/*find reference of this select*/
			var name=target.attr('name');
			var container=(where==1)?caches._body:caches._searchbar;
			var params=(where==1)?instance.options.editParams:instance.options.searchParams;
			var gqst=(where==1)?1:2;
			var refs=$('.ref-'+name,container);
			if(refs&&refs.length>0){
				params['__field_'+name]=target.val();
			}else{
				params['__field_'+name]=undefined;
			}
			refs.each(function(){
				var t=$(this);
				var dicType=t.attr('dicType');
				if(dicType&&(dicType=='TREE'||dicType=='URL')){
					t.html('');
				}else{
					var query=instance._generateQueryString(gqst);
					query=(query==''?'?':query+'&');
					$.get(instance.options.url+'/dictionary/'+instance._urlEncode(name)+'/'+instance._urlEncode(t.attr('name'))+query+'__id='+instance._urlEncode(target.val()),function(data){instance._ondictionarysuccess(data,where);},'xml').error(function(){instance._ondictionaryerr();});
				}
			});
			/*field change callback*/
			if(where==1&&instance.options.editFieldChangeCallback&&$.isFunction(instance.options.editFieldChangeCallback)){
				var args=[];args.push(target);
				instance.options.editFieldChangeCallback.apply(instance,args);
			}else if(where!=1&&instance.options.searchFieldChangeCallback&&$.isFunction(instance.options.searchFieldChangeCallback)){
				var args=[];args.push(target);
				instance.options.searchFieldChangeCallback.apply(instance,args);
			}
		},
		_ondictionarysuccess:function(data,where){
			var xml=$(data);
			var instance=this,elem=this.element,caches=elem.data('_caches');
			var container=(where==1)?caches._body:caches._searchbar;
			var response=xml.find('response');
			if(instance._checksuccess(xml)){
				response.find('field').each(function(){
					var f=$(this);
					var n=f.attr('name');
					var t=f.attr('dataType');
					switch(t){
					case 'select':
						var html='';
						var oldVal = $('select[name="'+n+'"]',container).val();
						f.find('dictionary').find('option').each(function(){
							var tt=$(this);
							html=html+'<option value="'+tt.attr('value')+'">'+tt.attr('label')+'</option>';
						});
						var select =$('select[name="'+n+'"]',container);
						select.html(html);
						select.val(oldVal);
						select.trigger('change');
						break;
					case 'collect':
						var html='';
						f.find('dictionary').find('option').each(function(){
							var tt=$(this);
							html=html+'<input style="width:20px;" type="checkbox" name="'+n+'" value="'+tt.attr('value')+'"/>'+tt.attr('label');
						});
						$('span[name="'+n+'"]',container).html(html);
						break;
					}
				});
			}else{
				instance._ondictionaryerr(response?response.attr('errMsg'):undefined);
			}
		},
		_ondictionaryerr:function(errMsg){
			if(errMsg==undefined){
				this._errparser();
			}else{
				this._showStatus('<p>'+errMsg+'</p>');
			}
		},
		_checksuccess : function(data){
			return (data.find('response[success="true"]').length>0);
		},
		_errparser : function () {
			this._showStatus(this.options.i18n.errorInfo);
		},
		_enterListView:function(hide){
			if(this.options.initAction=='list'){
				var elem=this.element,caches=elem.data('_caches');
				if(hide){
					caches._actionbar.hide('slow');
					caches._pagebar.hide('slow');
					if(this.options.showFooterActionBar){
						caches._actionbar_bottom.hide('slow');
						caches._pagebar_top.hide('slow');
					}
					caches._searchbar.hide('slow');
				}else{	
					caches._actionbar.show('slow');
					if(this.options.showFooterActionBar){
						caches._actionbar_bottom.show('slow');
					}
					if(caches._paged){
						caches._pagebar.show('slow');
						if(this.options.showFooterActionBar){
							caches._pagebar_top.show('slow');
						}
					}					
					if(caches._searchable){
						caches._searchbar.show('slow');
					}
				}
			}
		},
		_showBusy:function(){
			this._showStatus('<p>'+this.options.i18n.busyInfo+'</p>');
		},
		_hideBusy:function(){
			this._hideStatus();
		},
		_showStatus:function(html){
			var instance=this, elem=this.element,caches=elem.data('_caches');
			var status=caches.status;
			if(!status){
				status=$('<div class="ui-status ui-state-highlight ui-corner-all"></div>');
				caches.status=status;
				elem.append(status);
			}
			status.html(html);
			var left=((elem.width()-status.width())/2);
			var top=((elem.height()-status.height())/2);
			if(arguments.length>1){
				top=(arguments[1].pageY-elem.offset().top);
			}
			if(elem.css('position')+''!='relative'){
				var off=elem.offset();
				left=left+off.left;
				top=top+off.top;
			}
			if(left<0){left=0;}
			status.css({'left':left,'top':top});
			status.show();
			return status;
		},
		_hideStatus:function(){
			var instance=this, elem=this.element,caches=elem.data('_caches');
			var status=caches.status;
			status.hide();
		},
		_clearCaches:function(){
			/* clear current state */
			var elem=this.element,caches=elem.data('_caches');
			caches._header.text('');
			caches._body.html('<span class="ui-loading">Loading...</span>');
			caches._searchbar.hide('slow');
			
			caches._actionbar.html('');
			if(!!caches._actionbar_bottom){
				caches._actionbar_bottom.html('');
			}
			caches._pagebar.html('');
			if(!!caches._pagebar_top){
				caches._pagebar_top.html('');
			}
			caches._page=1;
			caches._listtable=null;
			caches._totalPage=1;
			caches._multiSelect=false;
			caches._paged=false;
			caches._searchable=false;
			caches._isSearch=false;
			if(caches._description){
				caches._description.remove();
				caches._description=null;
			}
		},
		doList:function(func){
			var elem=this.element,caches=elem.data('_caches');
			var params=[caches._isSearch,$('form',caches._searchbar),this._generateQueryString(caches._isSearch?2:3),caches._page];
			func.apply(elem,params);
		},
		showBusy:function(){this._showBusy();},
		hideBusy:function(){this._hideBusy();},
		selected:function(){
			var elem=this.element,caches=elem.data('_caches'),sels=$('input.ui-check:checked',caches._body);
			sels.each(function(){
				var t=$(this),p=t.parent().parent();
				$('.ui-list-column',p).each(function(){
					var tt=$(this);
					if(tt.attr('name')){
						t.data(tt.attr('name'),tt.text());
					}
				});
			});
			if(sels&&sels.length==0){
				return null;
			}
			return sels;
		},
		rows:function(){
			var elem=this.element,caches=elem.data('_caches');
			return caches._rows;
		},
		reset:function(){
			/*reset the default */
			if(this.options.cssClass){
				this.element.removeClass(this.options.cssClass);
			}
			this.options=$.extend(true,{},$.fn.crudOptions);
		},
		refresh:function(){
			var elem=this.element,caches=elem.data('_caches');
			this._loadPage(caches._page);
			this._loadCount();
		},
		params:function(key){return this.options.params[key];},
		setParams:function(key,value){this.options.params[key]=value;},
		opts:function(key){return this.options[key];},
		searchVal:function(field){
			var target=this, elem=this.element,caches=elem.data('_caches');			
			var form=$('form',caches._searchbar),vals=form.serializeArray(),val=null;
			$.each(vals,function(i,f){if(f.name==field){val=f.value;}});
			return val;			
		},
		editFieldVal:function(field){
			var target=this, elem=this.element,caches=elem.data('_caches'),form=$('form',caches._body),vals=form.serializeArray(),val=null;
			$.each(vals,function(i,f){if(f.name==field){val=f.value;}});
			return val;
		},
		setEditFieldVal:function(field,val){
			var target=this, elem=this.element,caches=elem.data('_caches'),form=$('form',caches._body);
			$('[name="'+field+'"]',form).val(val);
		},
		pageInfo:function(){
			var target=this, elem=this.element,caches=elem.data('_caches');
			return {page:caches._page,totalPage:caches._totalPage,totalCount:caches._count};
		},
		didEdit:function(id,params){
			$.extend(true,this.options.params,params);
			this._loadEdit(id);
		},
		didCreate:function(params){
			$.extend(true,this.options.params,params);
			this._loadEdit('new');
		},
		didSubmit:function(){
			var sb=$('.ui-button-submit',this.element.data('_caches')._body);
			if(sb&&sb.length>0){
				sb.trigger('click');
			}
		},
		didSearch:function(){
			var target=this, elem=this.element,caches=elem.data('_caches');
			caches._isSearch=true;
			target._loadPage(1);
			target._loadCount();
		},
		count:function(){
			var elem=this.element,caches=elem.data('_caches');
			return caches._count;
		},
		_generateQueryString:function(where){/*1,edit;2,search;other:normal*/
			var query='',params={};
			switch(where){
			case 1:
				params=$.extend(true,this.options.params,this.options.editParams);
				break;
			case 2:
				params=$.extend(true,this.options.params,this.options.searchParams);
				break;
			default:
				params=this.options.params;
			}
			query=$.param(params);
			query=(query==''?'':('?'+query));
			return query;
		},
		_openFileBrowser:function(field_name, url, type, win){
			var instance=this;
			tinyMCE.activeEditor.windowManager.open({
		        file :instance.options.filemanagerUrl,
		        title : '文件浏览',
		        width : 800,
		        height : 500,
		        resizable : "yes",
		        inline : "yes",
		        close_previous : "no"
		    },{
		        window : win,
		        input : field_name
		    });
		    return false;
		},
		_urlEncode:function(url){
			return escape(url);
		}
	});
	
})(jQuery);
