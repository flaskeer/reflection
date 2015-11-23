# reflection

  

>  
> Java中，反射是一种强大的工具，它允许运行中的java程序对自身进行检查，并能直接操作程序的内部属性。反射允许我们执行的时候使代码直接装载到JVM中类的内部信息。所以想要构建灵活的应用，反射必不可少。
>      这里我将根据面向对象设计的方式来写一个小demo，根据反射来载入不同的类，使系统具有比较好的扩展性。

首先设计一个接口:

    package cc.hao.reflect;
    
    import java.util.List;
    
    public interface Action {
    
    	public List<String> act(List<String> params);
    	
    }


接下来为不同的功能编写不同的类，继承Action接口，针对接口编程。

    //Store类
    package cc.hao.reflect;
    
    import java.util.ArrayList;
    import java.util.List;
    
    public class Store implements Action{
    
    	@Override
    	public List<String> act(List<String> params) {
    		List<String> result = new ArrayList<>();
    		result.add("this is store");
    		return result;
    	}
    }
    //Load类
    package cc.hao.reflect;
    
    import java.util.ArrayList;
    import java.util.List;
    
    public class Load implements Action{
    
    	@Override
    	public List<String> act(List<String> params) {
    		List<String> result = new ArrayList<>();
    		result.add("this is load");
    		return result;
    	}
    
    }

... 我们会需要编写很多的类，每次具体化哪个类呢？如果向程序传递一个参数，然后让它去自行实例化，执行它的act()方法，那就可以避免以后的麻烦了。

利用反射，一切都迎刃而解。

首先编写配置文件：emp.properties

    100=Load
    200=Search
    300=Store


然后我们利用反射来进行动态加载调用

    package cc.hao.reflect;
    
    import java.io.FileInputStream;
    import java.io.IOException;
    import java.lang.reflect.Method;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Properties;
    
    public class DynamicLoad {
     
       //这段代码用来加载配置文件，查询出需要的类名
    	Public String loadProp(String head) throws IOException{
    		String result = null;
    		Properties prop = new Properties();
    FileInputStream fis = new FileInputStream("emp.properties");
    		prop.load(fis);
    		result = prop.getProperty(head);
    		fis.close();
    		return result;
    		
    	}
    	
    	//用反射找出需要调用的类,发挥了面向接口编程的用处
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
    
    测试结果：
    Java DynamicLoad 100  | [this is load]
    Java DynamicLoad 200  | [this is search]

接下来我们再来看一段代码：

    package cc.hao.reflection;
    
    import java.lang.reflect.Field;
    import java.lang.reflect.InvocationTargetException;
    import java.lang.reflect.Method;
    
    public class TestReflect {
    
    	public Object copy(Object obj) throws Exception{
    		
    		Class<?> clazz = obj.getClass();
    		System.out.println("Class:" + clazz.getName());
    		
    		Object objCopy = clazz.getConstructor(new Class[]{}).newInstance();
    		Field[] fields = clazz.getDeclaredFields();
    	}
    		for (int i = 0; i < fields.length; i++) {
    			Field field = fields[i];
    			String fieldName = field.getName();
    			String firstLetter = fieldName.substring(0,1).toUpperCase();
    			String getMethodName = "get" + firstLetter + fieldName.substring(1);
    			String setMethodName = "set" + firstLetter + fieldName.substring(1);
    			Method getMethod = clazz.getMethod(getMethodName,new Class[]{} );
    			Method setMethod = clazz.getMethod(setMethodName, new Class[]{field.getType()});
    			Object value = getMethod.invoke(obj, new Object[]{});
    			System.out.println(fieldName + ":" + value);
    			setMethod.invoke(objCopy, new Object[]{value});
    			
    		}
    		
    		return objCopy;
    		
    	}
    	
    	public static void main(String[] args) throws Exception {
    		Customer customer = new Customer("liuyu",20);
    		customer.setId(30);
    		Customer customerCopy = (Customer) new TestReflect().copy(customer);
    		System.out.println(customerCopy);
    	}
    	
    }
    
    class Customer{
    	
    	private int id;
    	private String name;
    	private int age;
    	
    	public Customer() {
    		
    	}
    	public Customer(String name, int age) {
    		super();
    		this.name = name;
    		this.age = age;
    	}
    	public int getId() {
    		return id;
    	}
    	public void setId(int id) {
    		this.id = id;
    	}
    	public String getName() {
    		return name;
    	}
    	public void setName(String name) {
    		this.name = name;
    	}
    	public int getAge() {
    		return age;
    	}
    	public void setAge(int age) {
    		this.age = age;
    	}
    	@Override
    	public String toString() {
    		return "Customer [id=" + id + ", name=" + name + ", age=" + age + "]";
    	}
    	
    }

这里我们通过反射来复制一个Customer对象。我建议您认真阅读关于涉及到的API的文档，着重理解

    Object objCopy = clazz.getConstructor(new Class[]{}).newInstance();
    Method setMethod = clazz.getMethod(setMethodName, new Class[]{field.getType()});

里的参数含义，这里就不在多说了。

对于反射一直是很多人不甚理解的地方，参考了一些网上的文章，希望通过一些简单的例子引导您加深对反射的认识，写的比较肤浅，不对之处还请您指正。
 
