package cc.hao.reflect;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



public class DynamicLoad {

	
	public String loadProp(String head) throws IOException{
		String result = null;
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream("emp.properties");
		prop.load(fis);
		result = prop.getProperty(head);
		fis.close();
		return result;
		
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String loadClass(String head,String content) throws Exception{
		String result = null;
		String s = "cc.hao.reflect." + this.loadProp(head);
		Class className = Class.forName(s);
		Action action = (Action) className.newInstance();
		Class[] params = new Class[1];
		params[0] = Class.forName("java.util.List");
		Method method = className.getMethod("act",params);
		Object[] args = new Object[1];
		List<String> array = new ArrayList<>();
		array.add(content);
		args[0] = array;
		Object returnObject = method.invoke(action, args);
		System.out.println(returnObject);
		return result;
	}
	
	
	public static void main(String[] args) throws Exception {
		DynamicLoad reflect = new DynamicLoad();
		reflect.loadClass(args[0], "");
	}
	
}
