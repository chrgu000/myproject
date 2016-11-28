package spring.controller.base;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.dom.DOMSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import spring.annotation.FieldMeta;
import spring.annotation.FieldSortCom;
import spring.annotation.SortableField;

/**
 * @author baozhichao 2014-1-13 下午2:44:19
 */
public class BaseCRUDController<T, ID extends java.io.Serializable> extends
		BaseXmlController implements InitializingBean {

	String path = "spring";
	Class<T> entity = null;
	List<SortableField> sortableField = null;
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public DOMSource index(HttpServletRequest request,Model model){
		Document doc = getDocument();
		Element ele = doc.getDocumentElement();
		
		
		for(SortableField sf :sortableField){
			Element field = doc.createElement("field");
			field.setAttribute("name", sf.getName());
			field.setAttribute("label", sf.getMeta().name());
			ele.appendChild(field);
		}
		return new DOMSource(doc);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		initField();
	}

	@SuppressWarnings("unchecked")
	public void initField() {
		if (sortableField == null) {
			List<SortableField> list = new ArrayList<SortableField>();
			entity = (Class<T>) ((ParameterizedType) this.getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];

			// FieldMeta filed = entity.getAnnotation(FieldMeta.class);

			if (this.entity != null) {

				/**
				 * 返回类中所有字段，包括公共、保护、默认（包）访问和私有字段，但不包括继承的字段
				 * entity.getFields();只返回对象所表示的类或接口的所有可访问公共字段
				 * 在class中getDeclared**()方法返回的都是所有访问权限的字段、方法等；
				 * */
				Field[] fields = entity.getDeclaredFields();
				//
				for (Field f : fields) {
					// 获取字段中包含fieldMeta的注解
					FieldMeta meta = f.getAnnotation(FieldMeta.class);
					if (meta != null) {
						SortableField sf = new SortableField(meta, f);
						list.add(sf);
					}
				}

				// 返回对象所表示的类或接口的所有可访问公共方法
				Method[] methods = entity.getMethods();

				for (Method m : methods) {
					FieldMeta meta = m.getAnnotation(FieldMeta.class);
					if (meta != null) {
						SortableField sf = new SortableField(meta, m.getName(),
								m.getReturnType());
						list.add(sf);
					}
				}

				Collections.sort(list, new FieldSortCom());
			}
			sortableField = list;
		}

	}

}
