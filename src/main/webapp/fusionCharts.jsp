<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="static/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="static/flash/js/FusionCharts.js"></script>

<title>FunsionCharts demo</title>

<script type="text/javascript">
$(function(){
	/**
	 *  Para1：表示的是SWF文件的地址
	 *	Para2：该图形的ID，这个可以随便命名，但是需要保证它的唯一性
	 *	para3：图形的宽度。
	 *	Para4：图形的高度。
	 */
	 //建立一个FusionCharts对象
	 var myChart = new FusionCharts("flash/Doughnut3D.swf", "myChartId_04", "600", "500","0","1");
	 //设置数据文件
	 myChart.setDataURL("data.xml"); 
	 //指定图形渲染的位置。即div的id
	 myChart.render("chartdiv_01");  
	 
	 var dataXml = "<?xml version='1.0' encoding='GBK' ?><chart  xAxisName='日期'  yAxisName='kWh'  numdivlines='9'"+
			 "lineThickness='3' showValues='0' anchorRadius='3'  anchorBgAlpha='50' showAlternateVGridColor='1' "+
			 "numVisiblePlot='12' animation='0'  clickURL='JavaScript:suspensionClick(\"ydqsDay\");' ><categories>"+
			 "</categories><dataset color='FF0080' anchorBorderColor='FF0080'></dataset></chart>";
	//dataXml = "{}"; 当传回的数据 为{}时，默认显示 No data to display
	var dataChart = new FusionCharts("flash/ScrollLine2D.swf","dataChartXml","1000","800");
	//可以通过这种方式接收后台传送的xml数据
	dataChart.setDataXML(dataXml);
	dataChart.render("chartdiv_02");
});

 
</script>

</head>
<body>
	<h1>FusionCharts Demo</h1>
	<p>
		FusionCharts是InfoSoft Global公司的一个产品,而FusionCharts Free则是FusionCharts提供的一个免费版本，虽然免费，功能依然强大，图形类型依然丰富。这里介绍了都是基于FusionCharts Free的。
       FusionCharts free 是一个跨平台，跨浏览器的flash图表组件解决方案，能够被 ASP、.NET, PHP, JSP, ColdFusion, Ruby on Rails, 简单 HTML 页面甚至PPT调用。我们不需要知道任何Flash的知识，只需要了解你所用的编程语言而已。
       FusionCharts功能强大，那么它到底能够做什么呢？下面一一展示。
       </p>
	
	<div id="chartdiv_01" align="center" >fdsafdsa</div> 
	<div id="chartdiv_02" align="center" ></div> 


</body>
</html>