package GenerateRule;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import kodkod.ast.Formula;
/**
 *Generate rule basic
 **/
public class GenerateRuleBasic {
	public void execute(String path) {
		
		File destinationFolder; // where Model is written to
		destinationFolder = new File(path);
		
		//builder class
		Builder classBuilder = TypeSpec.classBuilder("Rule").addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT);
		
		//field
		TypeName gameObjectClass = ClassName.get("Controller","GameObject");
		TypeName mapOfStringAndClassOfAny = ParameterizedTypeName.get(ClassName.get(List.class), gameObjectClass);
	    FieldSpec fieldGameOb = FieldSpec.builder(mapOfStringAndClassOfAny, "gameObjects")
	            .addModifiers(Modifier.PRIVATE)
	            .build();

		// Getter
//		MethodSpec sigGetter = MethodSpec.methodBuilder("getGameObject").addModifiers(Modifier.PUBLIC)
//				.returns(Relation.class).addStatement("return this.$N", "gameObject").build();
//		classBuilder.addMethod(sigGetter);
//		// Setter
//		MethodSpec sigSetter = MethodSpec.methodBuilder("setGameObject").addModifiers(Modifier.PUBLIC)
//				.addParameter(List.class, "gameObject").addStatement("this.$N = $N", "gameObject", "gameObject").build();
//		classBuilder.addMethod(sigSetter);
		
		//method execute
		MethodSpec execute = MethodSpec.methodBuilder("execute").addModifiers(Modifier.ABSTRACT)
				.returns(Formula.class).build();
	
		//method addObject
		MethodSpec addObject = MethodSpec.methodBuilder("addObject").addModifiers(Modifier.ABSTRACT)
				.returns(TypeName.VOID)
				.addParameter(gameObjectClass, "gameObject")
				.build();
		
		classBuilder.addField(fieldGameOb)
		.addMethod(execute).addMethod(addObject);
		
		JavaFile javaFile = JavaFile.builder("Controller", classBuilder.build()).build();

		try {
			javaFile.writeTo(destinationFolder);
			/*
			 * Comment the above line, use the following line to print all the
			 * class to console
			 */
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			// assertMessage(e1.toString());
		}
	}
}
