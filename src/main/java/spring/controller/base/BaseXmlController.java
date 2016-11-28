package spring.controller.base;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *@author baozhichao
 *2014-1-13 ÏÂÎç2:07:26
 */
public class BaseXmlController extends BaseController implements ApplicationContextAware{

	Logger logger = LoggerFactory.getLogger(BaseXmlController.class);
	
	ApplicationContext appContext;
	
	public static DocumentBuilder db = null;
	
	public ApplicationContext getAppContext(){
		return appContext;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appContext = applicationContext;
	}
	
	static{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public Document getDocument(){
		Document doc = db.newDocument();
		Element ele = doc.createElement("start");
		ele.setAttribute("success", "true");
		doc.appendChild(ele);
		return doc;
		
	}

	public void parseException(Exception ex ,Element ele){
		logger.error(ex.getMessage(), ex);
		ele.setAttribute("exception", ex.getMessage());
		ele.setAttribute("success", "false");
	}
	
}

