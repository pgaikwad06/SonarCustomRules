package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "UseImmutableMap")

public class EnumRule extends BaseTreeVisitor implements JavaFileScanner {
	private JavaFileScannerContext context;

	@Override
	public void scanFile(final JavaFileScannerContext context) {
		this.context = context;
		scan(context.getTree());
	}

	@Override
	public void visitVariable(VariableTree tree) {
		if (tree.initializer() != null &&	(((ExpressionTree) tree.initializer()).firstToken().text().equals("ImmutableMap"))) {

			if (!(tree.type().symbolType().name().equals("ImmutableMap"))) {

				context.reportIssue(this, tree, "Use Identifier type ImmutableMap.");

			}
		}

	}
}
