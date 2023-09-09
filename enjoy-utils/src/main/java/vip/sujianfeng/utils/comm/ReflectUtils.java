package vip.sujianfeng.utils.comm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author sujianfeng
 * Class Reflection Tool
 */
public class ReflectUtils {

	public static boolean isContainMethod(Class<?> c, String method){
		boolean result = false;		
		for (int i = 0; i < c.getMethods().length; i++) {
			if (StringUtilsEx.sameText(method, c.getMethods()[i].getName())){
				result = true;
				break;
			}
		}
		return result;
	}	

	public static Object getObject(String className) throws Exception{
		return Class.forName(className).newInstance();
	}

	public static Object getObject(String className, Class<?>[] parameterTypes, Object[] params) throws Exception{
		//Obtain a Class object based on the class name
		Class<?> c = Class.forName(className); 
		//Parameter type array
		//Class<?>[] parameterTypes = {Object.class};
		//Obtain the corresponding constructor based on the parameter type
		Constructor<?> constructor = c.getConstructor(parameterTypes);		
		return constructor.newInstance(params);
	}

	public static Method getMethod(String className, String methodName) throws Exception{
		Object obj = getObject(className);
		return getMethod(obj, methodName);
	}

	public static Method getMethod(Object obj, String method, Object... args) throws Exception{		
		Method result = null;		
		for (int i = 0; i < obj.getClass().getMethods().length; i++) {
			Method methodObj = obj.getClass().getMethods()[i];
			if (StringUtilsEx.sameText(method, methodObj.getName())){
				boolean paramCheck = true;
				if (args.length > 0){
					Class<?>[] parameterTypes = methodObj.getParameterTypes();
					if (args.length == parameterTypes.length){
						for (int j = 0; j < args.length; j++) {
							if (args[j].getClass() != parameterTypes[j]){
								paramCheck = false;
								break;
							}
						}	
					}else{
						paramCheck = false;
					}
				}
				if (paramCheck){
					result = obj.getClass().getMethods()[i];
					break;
				}
			}
		}
		return result;		
		//return obj.getClass().getMethod(methodName); 
	}
	
	private static Object invoke(Method method, Object obj, Object...  args) throws Exception{
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			if (e instanceof InvocationTargetException) {
				InvocationTargetException et = (InvocationTargetException) e;
				throw new Exception(et.getTargetException());
			} else {
				throw new Exception(e);
			}
		}
	}
	

	public static Object callMethod(String className, String methodName, Object... args) throws Exception{	
			Object obj = getObject(className);
			Method method = getMethod(obj, methodName, args);
			return invoke(method, obj, args);
	}	
	

	public static Object callMethod(Object obj, String methodName, Object... args) throws Exception{
		Method method = getMethod(obj, methodName, args);
		if (method == null){ 
			StringBuilder msg = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				if (msg.length() > 0){ 
					msg.append(",");
				}
				msg.append(args[i].getClass().toString());
			}  
			String err = String.format("Class [%s] cannot find the specified method [%s (parameter:%s)]!", obj.getClass().toString(), methodName, msg.toString());
			throw new Exception(err);
		}
		return invoke(method, obj, args);		
	}

	public static Field getDeclaredField(Class<?> tmpClass, String fieldName){
		if (tmpClass == null){
			return null;
		}
		Field[] fields = tmpClass.getDeclaredFields();
		for (Field field : fields){
			if (field.getName().equals(fieldName)){
				return field;
			}
		}
		return getDeclaredField(tmpClass.getSuperclass(), fieldName);
	}

	public static Object getFieldValue(Object obj, String fieldName) throws IllegalAccessException {
		Field f = getDeclaredField(obj.getClass(), fieldName);
		//Assert.assertNotNull( obj.getClass().getName() + "不存在属性：" + field, f);
		f.setAccessible(true);
		return f.get(obj);
	}

	public static Object getFieldValue(Object obj, Field field) throws IllegalAccessException {
		field.setAccessible(true);
		return field.get(obj);
	}

	public static void setFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException {
		Field f = getDeclaredField(obj.getClass(), fieldName);
		f.setAccessible(true);
		f.set(obj, value);
	}

	public static void setFieldValue(Object obj, Field field, Object value) throws IllegalAccessException {
		field.setAccessible(true);
		field.set(obj, value);
	}
}

