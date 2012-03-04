package halo.dao.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.springframework.jdbc.core.RowMapper;

/**
 * 创建对象所对应的sql，本类使用了asm字节码处理，生成class
 * insert,update,delete所需要的信息,目前对象只支持long,int,byte,short,float,char,double
 * ,String,java.util.Date类型的值,id类型只支持long,int,String类型
 * 
 * @author akwei
 */
public class SqlUpdateMapperCreater extends ClassLoader implements Opcodes {

	protected SqlUpdateMapperCreater(ClassLoader parent) {
		super(parent);
	}

	private String createMapperClassName(Class<?> clazz) {
		int idx = clazz.getName().lastIndexOf(".");
		String shortName = clazz.getName().substring(idx + 1);
		String pkgName = clazz.getName().substring(0, idx);
		return pkgName + "." + shortName + "AsmSqlUpdateMapper";
	}

	@SuppressWarnings("unchecked")
	public <T> Class<SqlUpdateMapper<T>> createSqlUpdateMapperClass(
			ObjectSqlInfo<T> objectSqlInfo) {
		ClassWriter classWriter = new ClassWriter(0);
		String mapperName = createMapperClassName(objectSqlInfo.getClazz());
		String mapperClassName = mapperName.replaceAll("\\.", "/");
		String signature = Type.getDescriptor(Object.class)
				+ Type.getInternalName(SqlUpdateMapper.class) + "<"
				+ Type.getDescriptor(objectSqlInfo.getClazz()) + ">;";
		classWriter.visit(V1_5, ACC_PUBLIC, mapperClassName, signature,
				"java/lang/Object",
				new String[] { Type.getInternalName(SqlUpdateMapper.class) });
		// 构造方法
		MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC,
				"<init>", "()V", null, null);
		methodVisitor.visitMaxs(1, 1);
		methodVisitor.visitVarInsn(ALOAD, 0);
		methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object",
				"<init>", "()V");
		methodVisitor.visitInsn(RETURN);
		methodVisitor.visitEnd();
		visitGetIdParam(classWriter, methodVisitor, objectSqlInfo.getIdField(),
				objectSqlInfo);
		visitGetParamsForInsert(classWriter, methodVisitor, objectSqlInfo);
		visitGetParamsForUpdate(classWriter, methodVisitor, objectSqlInfo);
		visitBridgeGetIdParam(classWriter, methodVisitor, mapperClassName,
				objectSqlInfo);
		visitBridgeGetParamsForInsert(classWriter, methodVisitor,
				mapperClassName, objectSqlInfo);
		visitBridgeGetParamsForUpdate(classWriter, methodVisitor,
				mapperClassName, objectSqlInfo);
		byte[] code = classWriter.toByteArray();
		try {
			this.loadClass(RowMapper.class.getName());
			Class<SqlUpdateMapper<T>> mapperClass = (Class<SqlUpdateMapper<T>>) this
					.defineClass(mapperName, code, 0, code.length);
			return mapperClass;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private <T> void visitGetIdParam(ClassWriter classWriter,
			MethodVisitor methodVisitor, Field field,
			ObjectSqlInfo<T> objectSqlInfo) {
		if (field == null) {
			throw new RuntimeException("no id field in "
					+ objectSqlInfo.getClazz().getName());
		}
		MethodVisitor _methodVisitor = methodVisitor;
		_methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "getIdParam", "("
				+ Type.getDescriptor(objectSqlInfo.getClazz())
				+ ")Ljava/lang/Object;", null, null);
		_methodVisitor.visitMaxs(2, 2);
		_methodVisitor.visitVarInsn(ALOAD, 1);
		visitGetIdParamInvokeForField(_methodVisitor, field, objectSqlInfo);
		_methodVisitor.visitInsn(ARETURN);
		_methodVisitor.visitEnd();
	}

	private String getGetMethodName(Field field) {
		String fieldName = field.getName();
		return "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
	}

	/**
	 * id目前只支持int long String
	 * 
	 * @param <T>
	 * @param methodVisitor
	 * @param field
	 * @param objectSqlInfo
	 */
	private <T> void visitGetIdParamInvokeForField(MethodVisitor methodVisitor,
			Field field, ObjectSqlInfo<T> objectSqlInfo) {
		String type = getFieldReturnType(field);
		String methodName = getGetMethodName(objectSqlInfo.getIdField());
		methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
				Type.getInternalName(objectSqlInfo.getClazz()), methodName,
				"()" + Type.getDescriptor(field.getType()));
		FieldTypeUtil.checkIdFieldType(field);
		if (type.equals("int")) {
			methodVisitor.visitMethodInsn(INVOKESTATIC,
					Type.getInternalName(ParamListUtil.class), "toObject",
					"(I)Ljava/lang/Object;");
		}
		else if (type.equals("long")) {
			methodVisitor.visitMethodInsn(INVOKESTATIC,
					Type.getInternalName(ParamListUtil.class), "toObject",
					"(J)Ljava/lang/Object;");
		}
		else if (type.equals("java.lang.String")) {
			methodVisitor.visitMethodInsn(INVOKESTATIC,
					Type.getInternalName(ParamListUtil.class), "toObject",
					"(Ljava/lang/String;)Ljava/lang/Object;");
		}
	}

	private <T> void visitGetParamsForInsert(ClassWriter classWriter,
			MethodVisitor methodVisitor, ObjectSqlInfo<T> objectSqlInfo) {
		MethodVisitor _methodVisitor = methodVisitor;
		_methodVisitor = classWriter.visitMethod(
				ACC_PUBLIC,
				"getParamsForInsert",
				"(" + Type.getDescriptor(objectSqlInfo.getClazz()) + ")"
						+ Type.getDescriptor(Object[].class), null, null);
		_methodVisitor.visitMaxs(3, 3);
		_methodVisitor.visitTypeInsn(NEW,
				Type.getInternalName(ParamListUtil.class));
		_methodVisitor.visitInsn(DUP);
		_methodVisitor.visitMethodInsn(INVOKESPECIAL,
				Type.getInternalName(ParamListUtil.class), "<init>", "()V");
		_methodVisitor.visitVarInsn(ASTORE, 2);
		_methodVisitor.visitVarInsn(ALOAD, 2);
		for (Field f : objectSqlInfo.getAllfieldList()) {
			visitGetParamsForInsertAndUpdate(_methodVisitor, f, objectSqlInfo);
		}
		_methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
				Type.getInternalName(ParamListUtil.class), "toObjects",
				"()[Ljava/lang/Object;");
		_methodVisitor.visitInsn(ARETURN);
		_methodVisitor.visitEnd();
	}

	private <T> void visitGetParamsForUpdate(ClassWriter classWriter,
			MethodVisitor methodVisitor, ObjectSqlInfo<T> objectSqlInfo) {
		MethodVisitor _methodVisitor = methodVisitor;
		_methodVisitor = classWriter.visitMethod(
				ACC_PUBLIC,
				"getParamsForUpdate",
				"(" + Type.getDescriptor(objectSqlInfo.getClazz()) + ")"
						+ Type.getDescriptor(Object[].class), null, null);
		_methodVisitor.visitMaxs(3, 3);
		_methodVisitor.visitTypeInsn(NEW,
				Type.getInternalName(ParamListUtil.class));
		_methodVisitor.visitInsn(DUP);
		_methodVisitor.visitMethodInsn(INVOKESPECIAL,
				Type.getInternalName(ParamListUtil.class), "<init>", "()V");
		_methodVisitor.visitVarInsn(ASTORE, 2);
		_methodVisitor.visitVarInsn(ALOAD, 2);
		List<Field> list = new ArrayList<Field>(
				objectSqlInfo.getFieldIgnoreIdList());
		list.add(objectSqlInfo.getIdField());
		for (Field f : list) {
			visitGetParamsForInsertAndUpdate(_methodVisitor, f, objectSqlInfo);
		}
		_methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
				Type.getInternalName(ParamListUtil.class), "toObjects",
				"()[Ljava/lang/Object;");
		_methodVisitor.visitInsn(ARETURN);
		_methodVisitor.visitEnd();
	}

	private <T> void visitGetParamsForInsertAndUpdate(
			MethodVisitor methodVisitor, Field field,
			ObjectSqlInfo<T> objectSqlInfo) {
		MethodVisitor _methodVisitor = methodVisitor;
		_methodVisitor.visitVarInsn(ALOAD, 1);
		String type = getFieldReturnType(field);
		_methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
				Type.getInternalName(objectSqlInfo.getClazz()),
				getGetMethodName(field),
				"()" + Type.getDescriptor(field.getType()));
		FieldTypeUtil.checkFieldType(field);
		if (type.equals("int")) {
			_methodVisitor
					.visitMethodInsn(INVOKEVIRTUAL,
							Type.getInternalName(ParamListUtil.class),
							"addInt", "(I)V");
		}
		else if (type.equals("long")) {
			_methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
					Type.getInternalName(ParamListUtil.class), "addLong",
					"(J)V");
		}
		else if (type.equals("short")) {
			_methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
					Type.getInternalName(ParamListUtil.class), "addShort",
					"(S)V");
		}
		else if (type.equals("char")) {
			_methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
					Type.getInternalName(ParamListUtil.class), "addChar",
					"(C)V");
		}
		else if (type.equals("byte")) {
			_methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
					Type.getInternalName(ParamListUtil.class), "addByte",
					"(B)V");
		}
		else if (type.equals("float")) {
			_methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
					Type.getInternalName(ParamListUtil.class), "addFloat",
					"(F)V");
		}
		else if (type.equals("double")) {
			_methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
					Type.getInternalName(ParamListUtil.class), "addDouble",
					"(D)V");
		}
		else if (type.equals("java.lang.String")) {
			_methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
					Type.getInternalName(ParamListUtil.class), "addString",
					"(Ljava/lang/String;)V");
		}
		else if (type.equals("java.util.Date")) {
			_methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
					Type.getInternalName(ParamListUtil.class), "addDate",
					"(Ljava/util/Date;)V");
		}
		_methodVisitor.visitVarInsn(ALOAD, 2);
	}

	private <T> void visitBridgeGetIdParam(ClassWriter classWriter,
			MethodVisitor methodVisitor, String mapperClassName,
			ObjectSqlInfo<T> objectSqlInfo) {
		MethodVisitor _methodVisitor = methodVisitor;
		_methodVisitor = classWriter.visitMethod(ACC_PUBLIC + ACC_BRIDGE
				+ ACC_SYNTHETIC, "getIdParam",
				"(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
		_methodVisitor.visitMaxs(2, 2);
		_methodVisitor.visitVarInsn(ALOAD, 0);
		_methodVisitor.visitVarInsn(ALOAD, 1);
		_methodVisitor.visitTypeInsn(CHECKCAST,
				Type.getInternalName(objectSqlInfo.getClazz()));
		_methodVisitor.visitMethodInsn(INVOKEVIRTUAL, mapperClassName,
				"getIdParam",
				"(" + Type.getDescriptor(objectSqlInfo.getClazz())
						+ ")Ljava/lang/Object;");
		_methodVisitor.visitInsn(ARETURN);
	}

	private <T> void visitBridgeGetParamsForUpdate(ClassWriter classWriter,
			MethodVisitor methodVisitor, String mapperClassName,
			ObjectSqlInfo<T> objectSqlInfo) {
		MethodVisitor _methodVisitor = methodVisitor;
		_methodVisitor = classWriter.visitMethod(ACC_PUBLIC + ACC_BRIDGE
				+ ACC_SYNTHETIC, "getParamsForUpdate",
				"(Ljava/lang/Object;)[Ljava/lang/Object;", null, null);
		_methodVisitor.visitMaxs(2, 2);
		_methodVisitor.visitVarInsn(ALOAD, 0);
		_methodVisitor.visitVarInsn(ALOAD, 1);
		_methodVisitor.visitTypeInsn(CHECKCAST,
				Type.getInternalName(objectSqlInfo.getClazz()));
		_methodVisitor.visitMethodInsn(INVOKEVIRTUAL, mapperClassName,
				"getParamsForUpdate",
				"(" + Type.getDescriptor(objectSqlInfo.getClazz())
						+ ")[Ljava/lang/Object;");
		_methodVisitor.visitInsn(ARETURN);
	}

	private <T> void visitBridgeGetParamsForInsert(ClassWriter classWriter,
			MethodVisitor methodVisitor, String mapperClassName,
			ObjectSqlInfo<T> objectSqlInfo) {
		MethodVisitor _methodVisitor = methodVisitor;
		_methodVisitor = classWriter.visitMethod(ACC_PUBLIC + ACC_BRIDGE
				+ ACC_SYNTHETIC, "getParamsForInsert",
				"(Ljava/lang/Object;)[Ljava/lang/Object;", null, null);
		_methodVisitor.visitMaxs(2, 2);
		_methodVisitor.visitVarInsn(ALOAD, 0);
		_methodVisitor.visitVarInsn(ALOAD, 1);
		_methodVisitor.visitTypeInsn(CHECKCAST,
				Type.getInternalName(objectSqlInfo.getClazz()));
		_methodVisitor.visitMethodInsn(INVOKEVIRTUAL, mapperClassName,
				"getParamsForInsert",
				"(" + Type.getDescriptor(objectSqlInfo.getClazz())
						+ ")[Ljava/lang/Object;");
		_methodVisitor.visitInsn(ARETURN);
	}

	private String getFieldReturnType(Field field) {
		return field.getType().getName();
	}
}
