package org.sonar.samples.java.checks;

import java.util.Collections;
import java.util.List;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Symbol.TypeSymbol;
import org.sonar.plugins.java.api.semantic.Symbol.VariableSymbol;
import org.sonar.plugins.java.api.tree.BlockTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.IfStatementTree;
import org.sonar.plugins.java.api.tree.LiteralTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.StatementTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "SingletonRule")
public class SingletonRule extends IssuableSubscriptionVisitor {
	@Override
	public List<Tree.Kind> nodesToVisit() {
		return Collections.singletonList(Tree.Kind.CLASS);
	}

	@Override
	public void visitNode(Tree tree) {
		ClassTree treeClass = (ClassTree) tree;
		
		TypeSymbol classSymbol = treeClass.symbol();
		boolean verifyFlag = false;
		boolean constructorFound = false;
		boolean synchronizeFound = false;

		if (classSymbol.isPublic() && classSymbol.isFinal()) {

			for (Tree member : treeClass.members()) {
				if (member.is(Kind.VARIABLE)) {
					verifyFlag = checkVariableTree((VariableTree) member);
				}
				
				if(member.is(Tree.Kind.CONSTRUCTOR)) {
					constructorFound = isValidConstructorType((MethodTree) member);
				}
				if (member.is(Kind.METHOD)) {
					if(verifyFlag && constructorFound && checkValidGetMethod((MethodTree) member)) {
						synchronizeFound = hasSingleSyncStatement((MethodTree) member);
						if (!synchronizeFound) {
							reportIssue( treeClass, "Synchronize the getinstance method");
					     }
					}
					
				}
			}
		}
	}

	private boolean checkVariableTree(VariableTree member) {
		if (((LiteralTree) member.initializer()).value().equalsIgnoreCase("null")) {

			VariableSymbol vsym = (VariableSymbol) member.symbol();

			if (vsym.isPrivate() && vsym.isStatic()) {
				return true;
			}
		}
		return false;

	}

	private boolean isValidConstructorType(MethodTree mt) {
		return mt.symbol().isPrivate();
	}

	private boolean checkValidGetMethod(MethodTree vmt) {
		if (vmt.simpleName().toString().startsWith("get")) {
			return true;
		}
		return false;
	}

	
	private static boolean hasSingleSyncStatement(MethodTree methodTree) {
		BlockTree blockTree = methodTree.block();
	    if (blockTree != null  && blockTree.body().get(0).is(Tree.Kind.IF_STATEMENT) ) {
	    	IfStatementTree ifStatementTree = (IfStatementTree)blockTree.body().get(0);
	    	StatementTree sTree = ifStatementTree.thenStatement();
	    	if(sTree !=null) {
	    		BlockTree innerBlock = (BlockTree)sTree;
	    		return innerBlock.body().size() == 1 && innerBlock.body().get(0).is(Tree.Kind.SYNCHRONIZED_STATEMENT);
	    	}
	      
	    }
	    return false;
	  }
}
