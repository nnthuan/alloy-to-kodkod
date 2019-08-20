package GenerateRule;

import java.io.File;
import java.io.IOException;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

/**
 *Generate class GameObject 
 **/
public class GenerateGameObjectClass {
	public void execute(String path){
		File destinationFolder; // where Model is written to
		destinationFolder = new File(path);
		//builder class
		Builder classBuilder = TypeSpec.classBuilder("GameObject").addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT);
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
