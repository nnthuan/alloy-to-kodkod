package GenerateRule;

import kodkod.ast.operator.ExprCastOperator;
import kodkod.ast.operator.ExprCompOperator;
import kodkod.ast.operator.ExprOperator;
import kodkod.ast.operator.FormulaOperator;
import kodkod.ast.operator.IntCastOperator;
import kodkod.ast.operator.IntCompOperator;
import kodkod.ast.operator.IntOperator;
import kodkod.ast.operator.Multiplicity;
import kodkod.ast.operator.Quantifier;
/**
 *Container operators, expr, intexpr in kodkod 
 **/
public class OperatorRepositories {

	private String type;

	public String getType() {
		return type;
	}

	public String getExprOperator() {
		return exprOperator;
	}

	private String exprOperator;

	public OperatorRepositories() {
		this.type = null;
		this.exprOperator = null;
	}

	public void execute(String str) {
		if (str == null || str.isEmpty()) {
			return;
		}
		this.exprOperator = null;
		this.type = null;
		getInExprCastOperator(str);
		if (exprOperator != null) {
			type = "ExprCastOperator";
		} else {
			getInExprCompOperator(str);
			if (exprOperator != null) {
				type = "ExprCompOperator";
			} else {
				getInExprOperator(str);
				if (exprOperator != null) {
					type = "ExprOperator";
				} else {
					getInFormulaOperator(str);
					if (exprOperator != null) {
						type = "FormulaOperator";
					} else {
						getInIntCastOperator(str);
						if (exprOperator != null) {
							type = "IntCastOperator";
						} else {
							getInIntCompOperator(str);
							if (exprOperator != null) {
								type = "IntCompOperator";
							} else {
								getInIntOperator(str);
								if (exprOperator != null) {
									type = "IntOperator";
								} else {
									getInMultiplicity(str);
									if (exprOperator != null) {
										type = "Multiplicity";
									} else {
										getInQuantifier(str);
										if (exprOperator != null) {
											type = "Quantifier";
										}

									}

								}

							}

						}

					}

				}

			}
		}
	}

	/**
	 * Get Enumerates expression 'cast' operators. Use package
	 * kodkod.ast.operator.ExprCastOperator
	 * 
	 * @param syntax
	 * @return ExprCastOperator
	 **/
	private ExprCastOperator getInExprCastOperator(String syntax) {
		for (ExprCastOperator item : ExprCastOperator.values()) {
			if (item.toString().equals(syntax)) {
				exprOperator = item.name();
				return item;
			}
		}
		return null;
	}

	/**
	 * Get Enumerates relational comparison operators. Use
	 * kodkod.ast.operator.ExprCompOperator
	 *
	 * @param syntax
	 * @return ExprCompOperator
	 **/
	private ExprCompOperator getInExprCompOperator(String syntax) {
		for (ExprCompOperator item : ExprCompOperator.values()) {
			if (item.toString().equals(syntax)) {
				exprOperator = item.name();
				return item;
			}
		}
		return null;
	}

	/**
	 * Get Enumerates unary (~, ^, *), binary (+, &, ++, ->, -, .) and nary (+,
	 * &, ++, ->) expression operators. Use package
	 * kodkod.ast.operator.ExprOperator.
	 * 
	 * @param syntax
	 * @return ExprOperator
	 **/
	private ExprOperator getInExprOperator(String syntax) {
		for (ExprOperator item : ExprOperator.values()) {
			if (item.toString().equals(syntax)) {
				exprOperator = item.name();
				return item;
			}
		}
		return null;
	}

	/**
	 * Get Enumerates binary (&&, ||, =>, <=>) and nary (&&, ||) logical
	 * operators. Use package kodkod.ast.operator.FormulaOperator
	 * 
	 * @param syntax
	 * @return FormulaOperator
	 **/
	private FormulaOperator getInFormulaOperator(String syntax) {
		for (FormulaOperator item : FormulaOperator.values()) {
			if (item.toString().equals(syntax)) {
				exprOperator = item.name();
				return item;
			}
		}
		return null;
	}

	/**
	 * Get an int expression 'cast' operator. Use package
	 * kodkod.ast.operator.IntCastOperator
	 * 
	 * @param syntax
	 * @return IntCastOperator
	 **/
	private IntCastOperator getInIntCastOperator(String syntax) {
		for (IntCastOperator item : IntCastOperator.values()) {
			if (item.toString().equals(syntax)) {
				exprOperator = item.name();
				return item;
			}
		}
		return null;
	}

	/**
	 * Get Enumerates binary comparison operators: =, < , >, <=, >= Use package
	 * kodkod.ast.operator.IntCompOperator
	 * 
	 * @param syntax
	 * @return IntCompOperator
	 **/
	private IntCompOperator getInIntCompOperator(String syntax) {
		for (IntCompOperator item : IntCompOperator.values()) {
			if (item.toString().equals(syntax)) {
				exprOperator = item.name();
				return item;
			}
		}
		return null;
	}

	/**
	 * Get Enumerate unary (-, ~, abs, sgn), binary (+, *, &, |, -, /, %, >>,
	 * >>>, <<) and nary (+, *, &, |) operators on integer expressions. Use
	 * package kodkod.ast.operator.IntOperator
	 * 
	 * @param syntax
	 * @return IntOperator
	 **/
	private IntOperator getInIntOperator(String syntax) {

		for (IntOperator item : IntOperator.values()) {
			if (item.toString().equals(syntax)) {
				exprOperator = item.name();
				return item;
			}
		}
		return null;
	}

	/**
	 * Get Represents the multiplicity of an expression in a Multiplicity
	 * Formula or the multiplicity of a variable in a Deck. Use package
	 * kodkod.ast.operator.Multiplicity
	 * 
	 * @param syntax
	 * @return Multiplicity
	 **/
	private Multiplicity getInMultiplicity(String syntax) {
		for (Multiplicity item : Multiplicity.values()) {
			if (item.toString().equals(syntax)) {
				exprOperator = item.name();
				return item;
			}
		}
		return null;
	}

	/**
	 * Get Enumerates logical quantifiers. Use package
	 * kodkod.ast.operator.Quantifier
	 * 
	 * @param syntax
	 * @return Quantifier
	 **/
	private Quantifier getInQuantifier(String syntax) {
		for (Quantifier item : Quantifier.values()) {
			if (item.toString().equals(syntax)) {
				exprOperator = item.name();
				return item;
			}
		}
		return null;
	}
}
