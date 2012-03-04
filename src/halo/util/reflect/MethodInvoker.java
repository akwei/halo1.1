package halo.util.reflect;


public class MethodInvoker {

//	private Object target;
//
//	private Class<?> targetClass;
//
//	private Method method;
//
//	private String methodName;
//
//	private Object[] arguments;
//
//	public Object invoke() {
//		return null;
//	}
//
//	public void prepare() throws NoSuchMethodException {
//		this.targetClass = target.getClass();
//		Class<?>[] argTypes = new Class[arguments.length];
//		for (int i = 0; i < arguments.length; ++i) {
//			argTypes[i] = (arguments[i] != null ? arguments[i].getClass()
//					: Object.class);
//		}
//		try {
//			this.method = targetClass.getMethod(this.methodName, argTypes);
//		}
//		catch (NoSuchMethodException e) {
//			// Just rethrow exception if we can't get any match.
//			this.method = findMatchingMethod();
//			if (this.method == null) {
//				throw e;
//			}
//		}
//	}
//
//	protected Method findMatchingMethod() {
//		int argCount = arguments.length;
//		Method[] candidates = ReflectionUtils
//				.getAllDeclaredMethods(this.targetClass);
//		int minTypeDiffWeight = Integer.MAX_VALUE;
//		Method matchingMethod = null;
//		for (Method candidate : candidates) {
//			if (candidate.getName().equals(this.methodName)) {
//				Class<?>[] paramTypes = candidate.getParameterTypes();
//				if (paramTypes.length == argCount) {
//					int typeDiffWeight = getTypeDifferenceWeight(paramTypes,
//							arguments);
//					if (typeDiffWeight < minTypeDiffWeight) {
//						minTypeDiffWeight = typeDiffWeight;
//						matchingMethod = candidate;
//					}
//				}
//			}
//		}
//		return matchingMethod;
//	}
//
//	public Object invoke(String s) {
//		return null;
//	}
}
