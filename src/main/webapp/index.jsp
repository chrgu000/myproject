<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>index12 jsp</title>
<link rel="stylesheet" href="http://localhost/spring/statics/jquery-ui/themes/base/jquery.ui.all.css" type="text/css">
<link rel="stylesheet" href="http://www.jszg.cn/statics/styles/redmond/jquery-ui-1.8.11.custom.css" type="text/css">
<link rel="stylesheet" href="statics/demos.css" type="text/css">
<link rel="stylesheet" href="statics/jquery-ui/demos/demos.css" type="text/css">
<link rel="stylesheet" href="statics/jquery-ui/themes/base/jquery-ui.css" type="text/css">

<script type="text/javascript" src="statics/jquery-1.8.3.js"></script>
<script type="text/javascript" src="statics/jquery-ui/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="statics/jquery-ui/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="statics/jquery-ui/ui/jquery.ui.button.js"></script>
<script type="text/javascript" src="statics/jquery-ui/ui/jquery.ui.dialog.js"></script>

<script type="text/javascript">
$(function(){
	function _test(){
		alert(1);
	}
	$("button").button();
;(function($){
	$.widget('javaee.crud', {
		options:{title:'widget test',name:"Mr.bao",time:"03/20",params:{}},
		_create : function(){
			//alert(this.options.params.name);
			var params = $.param(this.options.params);//将表单元素数组或者对象序列化,返回参数的字符串:name=？&age=?
			params = params==""?"":"?"+params;
			this.options.title;
			this.element.attr("id");
			//this.element.empty();
			this._showStatus("<p>正在操作，请稍等.....</p>");
			
		},
		_init:function(){
			//alert(2);
			
			
		},
		_showStatus:function(html){
			var elem=this.element;
			var status;
				status=$('<div class="ui-status ui-state-highlight ui-corner-all"></div>');
				elem.append(status);
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
		destroy:function(){
			alert(3);
		},
		_setOption: function( key, value ) { 
		 	alert(key);
	 	}
	});	
})(jQuery);	

var div = $("<div></div>");
div.dialog({title:"测试"}).crud();

/*var div = $("<div class='ui-state-highlight ui-corner-all' style='position:absolute;z-index:100;width:300px;height:40px;text-align:center;padding:10px;overflow:auto;'>出现错误！</div>");
$("button").after(div);
div.show();*/

var _rows=[];
var _data={};
_rows.push(_data);
_data._id="123";
//alert(_rows.length+"\n"+_rows[0]._id);

/*var test = "";//null;
alert(!!test);
alert(!test);
alert(test?1:2);
	if(test){alert(0);}*/
	
$("button").button({disabled:true});
});
</script>
</head>
<body>
<a href="rongcloud/login.html">融云测试</a>
<h1>登录页面</h1><button id="b">UI测试按钮</button><br>fdsafdsafds
</body>
</html>
