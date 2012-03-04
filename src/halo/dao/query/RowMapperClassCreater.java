package halo.dao.query;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.springframework.jdbc.core.RowMapper;

/**
 * 自动创建数据对象对应的rowmapper对象 暂时只支持
 * long,int,byte,short,float,char,double,String,java.util.Date类型的值
 * 
 * @author akwei
 */
public class RowMapperClassCreater extends ClassLoader implements Opcodes {

	private static final String TYPE_LONG = "long";

	private static final String TYPE_INT = "int";

	private static final String TYPE_BYTE = "byte";

	private static final String TYPE_SHORT = "short";

	private static final String TYPE_FLOAT = "float";

	private static final String TYPE_DOUBLE = "double";

	private static final String TYPE_STRING = String.class.getName();

	private static final String TYPE_DATE = Date.class.getName();

	public RowMapperClassCreater() {
		super(Thread.currentThread().getContextClassLoader());
	}

	@SuppressWarnings("unchecked")
	protected <T> Class<RowMapper<T>> createRowMapperClass(
			ResultSetDataInfo<T> resultSetDataInfo) {
		ClassWriter classWriter = new ClassWriter(0);
		String mapperName = createMapperClassName(resultSetDataInfo.getClazz());
		String signName = mapperName.replaceAll("\\.", "/");
		classWriter.visit(V1_5, ACC_PUBLIC, signName, null, "java/lang/Object",
				new String[] { Type.getInternalName(RowMapper.class) });
		// 构造方法
		MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC,
				"<init>", "()V", null, null);
		methodVisitor.visitMaxs(1, 1);
		methodVisitor.visitVarInsn(ALOAD, 0);
		methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object",
				"<init>", "()V");
		methodVisitor.visitInsn(RETURN);
		methodVisitor.visitEnd();
		methodVisitor = classWriter
				.visitMethod(
						ACC_PUBLIC,
						"mapRow",
						"(Ljava/sql/ResultSet;I)"
								+ Type.getDescriptor(Object.class),
						null,
						new String[] { Type.getInternalName(SQLException.class) });
		methodVisitor.visitMaxs(3, 4);
		methodVisitor.visitTypeInsn(NEW, Type.getInternalName(resultSetDataInfo
				.getClazz()));
		methodVisitor.visitInsn(DUP);
		methodVisitor
				.visitMethodInsn(INVOKESPECIAL, Type
						.getInternalName(resultSetDataInfo.getClazz()),
						"<init>", "()V");
		methodVisitor.visitVarInsn(ASTORE, 3);
		methodVisitor.visitVarInsn(ALOAD, 3);
		for (Field field : resultSetDataInfo.getFieldList()) {
			createResultSetGetValue(methodVisitor, resultSetDataInfo, field);
		}
		methodVisitor.visitInsn(ARETURN);
		methodVisitor.visitEnd();
		byte[] code = classWriter.toByteArray();
		try {
			this.loadClass(RowMapper.class.getName());
			Class<RowMapper<T>> mapperClass = (Class<RowMapper<T>>) this.defineClass(
					mapperName, code, 0, code.length);
			return mapperClass;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private <T> void createResultSetGetValue(MethodVisitor methodVisitor,
			ResultSetDataInfo<T> resultSetDataInfo, Field field) {
		methodVisitor.visitVarInsn(ALOAD, 1);
		methodVisitor.visitLdcInsn(resultSetDataInfo.getFullColumn(field
				.getName()));
		MethodInfo resultSetMethodInfo = this.createResultSetMethodInfo(field);
		methodVisitor.visitMethodInsn(INVOKEINTERFACE, Type
				.getInternalName(ResultSet.class), resultSetMethodInfo
				.getMethodName(), resultSetMethodInfo.getMethodDescr());
		MethodInfo setterMethodInfo = this.createSetterMethodInfo(field);
		methodVisitor.visitMethodInsn(INVOKEVIRTUAL, Type
				.getInternalName(resultSetDataInfo.getClazz()),
				setterMethodInfo.getMethodName(), setterMethodInfo
						.getMethodDescr());
		methodVisitor.visitVarInsn(ALOAD, 3);
	}

	private MethodInfo createSetterMethodInfo(Field field) {
		FieldTypeUtil.checkFieldType(field);
		String type = field.getType().getName();
		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setMethodName(this.createSetMethodString(field.getName()));
		if (type.equals(TYPE_INT)) {
			methodInfo.setMethodDescr("(I)V");
		}
		if (type.equals(TYPE_SHORT)) {
			methodInfo.setMethodDescr("(S)V");
		}
		if (type.equals(TYPE_BYTE)) {
			methodInfo.setMethodDescr("(B)V");
		}
		if (type.equals(TYPE_LONG)) {
			methodInfo.setMethodDescr("(J)V");
		}
		if (type.equals(TYPE_FLOAT)) {
			methodInfo.setMethodDescr("(F)V");
		}
		if (type.equals(TYPE_DOUBLE)) {
			methodInfo.setMethodDescr("(D)V");
		}
		if (type.equals(TYPE_STRING)) {
			methodInfo.setMethodDescr("(Ljava/lang/String;)V");
		}
		if (type.equals(TYPE_DATE)) {
			methodInfo.setMethodDescr("(" + Type.getDescriptor(Date.class)
					+ ")V");
		}
		return methodInfo;
	}

	private MethodInfo createResultSetMethodInfo(Field field) {
		FieldTypeUtil.checkFieldType(field);
		MethodInfo methodInfo = new MethodInfo();
		String type = field.getType().getName();
		if (type.equals(TYPE_INT)) {
			methodInfo.setMethodName("getInt");
			methodInfo.setMethodDescr("(Ljava/lang/String;)I");
		}
		if (type.equals(TYPE_SHORT)) {
			methodInfo.setMethodName("getShort");
			methodInfo.setMethodDescr("(Ljava/lang/String;)S");
		}
		if (type.equals(TYPE_BYTE)) {
			methodInfo.setMethodName("getByte");
			methodInfo.setMethodDescr("(Ljava/lang/String;)B");
		}
		if (type.equals(TYPE_LONG)) {
			methodInfo.setMethodName("getLong");
			methodInfo.setMethodDescr("(Ljava/lang/String;)J");
		}
		if (type.equals(TYPE_FLOAT)) {
			methodInfo.setMethodName("getFloat");
			methodInfo.setMethodDescr("(Ljava/lang/String;)F");
		}
		if (type.equals(TYPE_DOUBLE)) {
			methodInfo.setMethodName("getDouble");
			methodInfo.setMethodDescr("(Ljava/lang/String;)D");
		}
		if (type.equals(TYPE_STRING)) {
			methodInfo.setMethodName("getString");
			methodInfo.setMethodDescr("(Ljava/lang/String;)Ljava/lang/String;");
		}
		if (type.equals(TYPE_DATE)) {
			methodInfo.setMethodName("getTimestamp");
			methodInfo
					.setMethodDescr("(Ljava/lang/String;)Ljava/sql/Timestamp;");
		}
		return methodInfo;
	}

	private String createSetMethodString(String fieldName) {
		return "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
	}

	private String createMapperClassName(Class<?> clazz) {
		int idx = clazz.getName().lastIndexOf(".");
		String shortName = clazz.getName().substring(idx + 1);
		String pkgName = clazz.getName().substring(0, idx);
		return pkgName + "." + shortName + "AsmMapper";
	}

	private static class MethodInfo {

		private String methodName;

		private String methodDescr;

		public void setMethodDescr(String methodDescr) {
			this.methodDescr = methodDescr;
		}

		public String getMethodDescr() {
			return methodDescr;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public String getMethodName() {
			return methodName;
		}
	}
}
