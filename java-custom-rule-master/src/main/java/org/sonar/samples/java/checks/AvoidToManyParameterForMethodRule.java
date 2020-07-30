package org.sonar.samples.java.checks;


import java.util.List;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Symbol.MethodSymbol;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import com.google.common.collect.ImmutableList;

@Rule(key = "AvoidToManyParameterForMethod", description = "Method should not have more than 4 parameters", priority = Priority.MAJOR, tags = {
		"bugs" })
public class AvoidToManyParameterForMethodRule extends IssuableSubscriptionVisitor {

	@Override
	public List<Kind> nodesToVisit() {
		return ImmutableList.of(Tree.Kind.METHOD);
	}

	@Override
	public void visitNode(Tree tree) {
		MethodTree methodTree = (MethodTree) tree;
		MethodSymbol methodSymbol = methodTree.symbol();
		
		if (methodSymbol.parameterTypes().size() > 4) {
		reportIssue(tree, "Too Many Parameters");
		}
	}

}
