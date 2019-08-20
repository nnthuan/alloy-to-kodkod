package Parser;

/*
 * Ho Chi Minh University of Technology
 * Faculty of Computer Science and Engineering
 * Class Description: Parser Class for generate Java "Model" Packet (in MVC) from Alloy Files (.als file)
 * 					  Alloy API to extract Alloy Object
 * 					  Javapoet to generate java source files.
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.Field;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.PrimSig;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;



public class Alloy2JavaParser {
	
	private File 	destinationFolder; // where Model is written to
	private Module 	module; // module got from als file
	public boolean error = false;
	/*
	 * Class Constructor
	 * @param: dstFolderAbsolutePath - the absolute path of the destination folder
	 * 		   alloyAbsolutePath 	 - the absolute path of the alloy file
	 */

	public Alloy2JavaParser(String dstFolderAbsolutePath, String alloyAbsolutePath) {
		destinationFolder = new File(dstFolderAbsolutePath);
		File alloyFile = new File(alloyAbsolutePath);
		if(!alloyFile.isFile()) {
			assertMessage("Error in the parser constructor: alloy file");
			error = true;
		}
		try {
			module = CompUtil.parseEverything_fromFile(null, null, alloyAbsolutePath);
		} catch (Err e) {
			assertMessage("module is empty: " + e.toString());
			error = true;
		}
	}
	
	/*
	 * Create model from alloy files
	 */
	
	public void createModelFromAlloy(){
		// Get signatures list
		SafeList<Sig> sigs = module.getAllSigs();
		// Check if the list is empty
		if(sigs.isEmpty()) assertMessage("Error in create Model FromAlloy: no sig found!");
		// Generate java class source file for one top-level sig at a time
		for(int i = 0; i<sigs.size(); i++) {
			Sig aSig = sigs.get(i);
			if (!aSig.isTopLevel()) continue;
			if(!aSig.builtin){ // User-defined sig
				if(aSig.isSubsig != null){
					PrimSig pSig = (PrimSig) aSig;
					recursiveGenerate(null, pSig);	
				} else assertMessage("TODO: Dealt with subset sig");
			}
			else assertMessage("TODO: Dealt with built-in sig");
		}
	}
	
	/*
	 * Create model from alloy files
	 */
	
	
	void recursiveGenerate(List<String> pSigParentAttrs, PrimSig pSig){
		// Pre-condition
		if (pSig == null) assertMessage("Error in recursiveGenerate: PrimSig pSig null!");
		// Main code
		String superClass = null;
		boolean isTopLevel = pSig.isTopLevel();
		if(!isTopLevel) superClass = resolveLabel(pSig.parent.label);
		String className = resolveLabel(pSig.label);
		//Create a pSig attrs list
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<String> pSigAttrs = new ArrayList();
		// Create the class builder
		Builder classBuilder = TypeSpec.classBuilder(className);
		com.squareup.javapoet.MethodSpec.Builder constructorBuilder 
		= MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

		TypeSpec finalClass = null;
		if (superClass != null){ // not top-level sig, using its superClass constructor
			ClassName superClassType = ClassName.get("Model", superClass);
			classBuilder.superclass(superClassType);
			String attrCombined = "";
			if(pSigParentAttrs != null){
				for(int i = 0; i<pSigParentAttrs.size(); i+=2){
					String tmpAttrName = pSigParentAttrs.get(i);
					if(i+2 < pSigParentAttrs.size()) 
						attrCombined = attrCombined + tmpAttrName +", ";
					else 
						attrCombined = attrCombined + tmpAttrName;
					
					String attrTypeInString = pSigParentAttrs.get(i+1);
					Class<?> tmpAttrType = null;
						ClassName tmpAttrClassName = null;
						tmpAttrType = resolveBuiltInAttrType(attrTypeInString);
					
						if(tmpAttrType != null) {
							constructorBuilder.addParameter(tmpAttrType, tmpAttrName);
						}
						else 
						{
							tmpAttrClassName = ClassName.get("Model", attrTypeInString);
							constructorBuilder.addParameter(tmpAttrClassName, tmpAttrName);
						}
				}// for
			}// fi pSigParentAttrs
			
			constructorBuilder.addStatement("super(" + attrCombined + ")");
		} else 
		{ // top-level sig
			// Adding regular methods for top level class
			// The toString method
			MethodSpec toString = MethodSpec.methodBuilder("toString")
					.addModifiers(Modifier.PUBLIC)
					.returns(String.class)
					.addStatement("return \"\"")
					.build();
			classBuilder.addMethod(toString);
		}
		SafeList<Sig.Field> sigFields = pSig.getFields();
		for(int i = 0; i<sigFields.size(); i++){
			Field field = sigFields.get(i);
			// resolve the attribute name from the field label
			String attrName = resolveLabel(field.label);
			// resolve the attribute type 
			List<List<PrimSig>> typeSigList = field.type().fold();
			PrimSig attrSig = typeSigList.get(0).get(1);
			String attrType = resolveLabel(attrSig.label);
			// Add the attrType and Name to pSig attr list
			pSigAttrs.add(attrName);
			pSigAttrs.add(resolveAttrType(attrType));
			// create field builder components
			// Map between alloy type with java type
			Class<?> typeClass = null;
			ClassName attrClassName = null;
			typeClass = resolveBuiltInAttrType(attrType);
			//if(isTopLevel){
				if(typeClass == null) {
					attrClassName = ClassName.get("Model", attrType);
					buildAttributeComponent(classBuilder, 
							constructorBuilder,  
							attrName, 
							attrClassName);
				} else {
					buildAttributeComponent(classBuilder, 
							constructorBuilder, 
							attrName, 
							typeClass);
				}
			//}
		}
		classBuilder.addMethod(constructorBuilder.build());
		finalClass = classBuilder.build();
		
		JavaFile javaFile = JavaFile.builder("Model", finalClass).build();
		try {
			javaFile.writeTo(destinationFolder);
			/* Comment the above line, 
			 * use the following line to print all the class to console*/
			// javaFile.writeTo(System.out); 
		} catch (IOException e1) {	
			// TODO Auto-generated catch block
			assertMessage(e1.toString());
		}
		// Check its childs
		try {
			SafeList<PrimSig> childs = pSig.children();
			for (int i = 0; i < childs.size(); i++) {
				recursiveGenerate(pSigAttrs, childs.get(i));
			}
		} catch (Err e) {
			assertMessage(e.toString());
			return;
		}
	}
	
	/*
	 * to build a attribute for a class
	 * @param: classBuilder 	  - the builder used to build the class
	 * 		   constructorBuilder - the builder used to build the class 's constructor
	 * 		   attrName			  - name of the attribute that is going to be added
	 *		   attrType			  - this type is a self-defined type
	 */	
	private void buildAttributeComponent(
			Builder classBuilder, 
			com.squareup.javapoet.MethodSpec.Builder constructorBuilder,
			String attrName,
			ClassName attrType){
		// Add an private attr which is build by "thisField" to the class
		FieldSpec field = FieldSpec.builder(attrType, attrName, Modifier.PRIVATE).build();
		classBuilder.addField(field);
		// An attr is always followed by a constructor, a getter and a setter
		// Add the attr's construct part to the constructor of the class
		constructorBuilder.addParameter(attrType, attrName)
						.addModifiers(Modifier.PUBLIC)
						.addStatement("this.$N = $N", attrName, attrName);
		
		// generate getter and setter for the attr
		String capitalizedAttrName = capitalizeFirstLetter(attrName);
		MethodSpec getter = MethodSpec.methodBuilder("get" + capitalizedAttrName)
				.returns(attrType)
				.addModifiers(Modifier.PUBLIC)
				.addStatement("return $N", attrName).build();
		
		MethodSpec setter = MethodSpec.methodBuilder("set" + capitalizedAttrName)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(attrType, attrName)
				.addStatement("this.$N = $N", attrName, attrName).build();
		classBuilder.addMethod(setter).addMethod(getter);
	
	}
	
	/*
	 * to build a attribute for a class
	 * @param: classBuilder 	  - the builder used to build the class
	 * 		   constructorBuilder - the builder used to build the class 's constructor
	 * 		   attrName			  - name and type of the attribute that is going to be added
	 *		   attrType			  - this type is a user defined type
	 */
	private void buildAttributeComponent(
			Builder classBuilder, 
			com.squareup.javapoet.MethodSpec.Builder constructorBuilder,
			String attrName,
			Class<?> attrType) {
		// Add an private attr which is build by "thisField" to the class
		FieldSpec field = FieldSpec.builder(attrType, attrName, Modifier.PRIVATE).build();
		classBuilder.addField(field);
		// An attr is always followed by a constructor, a getter and a setter
		// Add the attr's construct part to the constructor of the class
		constructorBuilder.addParameter(attrType, attrName)
						.addModifiers(Modifier.PUBLIC)
						.addStatement("this.$N = $N", attrName, attrName);
		
		// generate getter and setter for the attr
		String capitalizedAttrName = capitalizeFirstLetter(attrName);
		MethodSpec getter = MethodSpec.methodBuilder("get" + capitalizedAttrName)
				.returns(attrType)
				.addModifiers(Modifier.PUBLIC)
				.addStatement("return $N", attrName).build();
		
		MethodSpec setter = MethodSpec.methodBuilder("set" + capitalizedAttrName)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(attrType, attrName)
				.addStatement("this.$N = $N", attrName, attrName).build();
		classBuilder.addMethod(setter).addMethod(getter);

	}
	
	/*
	 * Mapping between alloy built-in type and Java type
	 * @param: 	anAlloyAttrType - an alloy type in string format
	 * @return: Class<?> that represent the type alloy map to
	 * 			null if got an unexpected alloy type
	 */
	private Class<?> resolveBuiltInAttrType(String anAlloyAttrType){
		if(anAlloyAttrType == "int" || anAlloyAttrType == "Int") return int.class;
		else if(anAlloyAttrType == "String") return String.class;
		else if(anAlloyAttrType == "univ") assertMessage("TODO: Map univ with Java type");
		else if(anAlloyAttrType == "none") assertMessage("TODO: Map none with Java type");
		else if(anAlloyAttrType.contains("seq")) assertMessage("TODO: Map seqInt with Java type");
		
		return null;
	}
	
	/*
	 * Mapping between self-defined type and Java types
	 * @param: 	aType - an alloy type in string format
	 * @return: a String that represent the type alloy map to
	 * 			else return aType
	 */
	private String resolveAttrType(String aType){
		if(aType == "int" || aType == "Int") return "int";
		else if(aType == "String") return "String";
		else if(aType == "univ") assertMessage("TODO: Map univ with Java type");
		else if(aType == "none") assertMessage("TODO: Map none with Java type");
		else if(aType.contains("seq")) assertMessage("TODO: Map seqInt with Java type");

		return aType;
	}
	
	/*
	 * Short functions to assert errors or incompletion (TODO)
	 * @param: aMessage - a String message
	 */
	private void assertMessage(String aMessage){
		System.out.println(aMessage);
		error = true;
	}
	
	/*
	 * Get the name from alloy label
	 * @param: 	aLabel - a label
	 * @return: the name that will be used in class build.
	 */
	private String resolveLabel(String aLabel){
		// Pre-condition
		if(aLabel == null) return aLabel;
		// Main code
		if (aLabel == "univ") return null;
		String actualName = aLabel.contains("this/")?aLabel.substring("this/".length()):aLabel;
		return actualName;
	}
	
	/*
	 * Capitalize the first letter of every input String
	 * @param: 	aString - an input string
	 * @return: input String with capitalized its first letter.
	 */
	private String capitalizeFirstLetter(String aString){
		// pre-conditon
		if(aString == null || aString.length() == 0) return aString;
		// Main code
		return aString.substring(0, 1).toUpperCase() + aString.substring(1);
	}

}
