package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "TOVariableNamingRule")

public class TOVariableNamingRule extends BaseTreeVisitor implements JavaFileScanner {
	private JavaFileScannerContext context;

	@Override
	public void scanFile(JavaFileScannerContext context) {
		this.context = context;
		scan(context.getTree());
	}

	@Override
	public void visitVariable(VariableTree tree) {
		Symbol variableSymbol = tree.symbol();
		if (variableSymbol.type().isPrimitive() && variableSymbol.name().endsWith("TO")) {
			context.reportIssue(this, tree.simpleName(),
					"Rename this variable as Primitive data type variable can not be Tansfer Object");
		}
		super.visitVariable(tree);
	}

}
