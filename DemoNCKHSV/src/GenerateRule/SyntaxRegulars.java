package GenerateRule;
/**
 *Container syntax regular expression 
 **/
public class SyntaxRegulars {
	/**
	 * name1 : one bounding-expression 
	 * name2 : lone expr // at most one 
	 * name3 : some expr // one or more 
	 * name4 : set expr // zero or more 
	 **/
	static String regMultiplicities="(\\w*) : (one|lone|some|set) (\\w*)";
	public static String getRegMultiplicities() {
		return regMultiplicities;
	}
	
	/**
	 * some var : bounding-expr | expr 
	 * all var : bounding-expr | expr 
	 * one var : bounding-expr | expr 
	 * lone var : bounding-expr | expr 
	 * no var : bounding-expr | expr 
	 **/
	static String regQuantified ="(one|lone|some|all|no) (\\w*) : (\\w*)";
	public static String getRegQuantified() {
		return regQuantified;
	}
	/**
	 * some var1,var2,var3 : bounding-expr | expr 
	 * all var1,var2,var3 : bounding-expr | expr 
	 * one var1,var2,var3 : bounding-expr | expr 
	 * lone var1,var2,var3 : bounding-expr | expr 
	 * no var1,var2,var3 : bounding-expr | expr 
	 **/
	static String regMulVarSig="(one|lone|some|all|no) (\\w* , \\w*)* : (\\w*)";
	public static String getRegMulVarSig() {
		return regMulVarSig;
	}
}
