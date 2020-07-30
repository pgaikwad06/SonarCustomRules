package org.sonar.samples.java.checks;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

@Rule(key = "JAFClassNamingRule")

public class JAFClassNamingRule extends IssuableSubscriptionVisitor {
	@Override
	  public List<Kind> nodesToVisit() {
	    return Arrays.asList(Tree.Kind.CLASS);
	  }

	  @Override
	  public void visitNode(Tree tree) {
	    ClassTree classTree = (ClassTree) tree;
	    if (hasSemantic()) {
	      Symbol.TypeSymbol classSymbol = classTree.symbol();
	      checkSuperType(classTree, classSymbol.superClass());
	    }
	  }
	  
	  private void checkSuperType(ClassTree tree, @Nullable Type superType) {
	    if (superType != null && !hasSpecificName(tree, superType) && !isInnerClass(tree)) {
	      String classOrInterface = tree.is(Tree.Kind.CLASS) ? "class" : "interface";
	      reportIssue(tree.simpleName(), "Rename this " + classOrInterface + " with class name endswith "+superType.symbol().name());
	    }
	  }

	  private static boolean hasSpecificName(ClassTree tree, Type superType) {
		  boolean result = true;
		  switch(superType.symbol().name()) {
		  	case "BusinessService":
		  	case "RestCapability":
			case "SoapCapability":
			case "DbCapability":
				result = tree.symbol().name().endsWith(superType.symbol().name());
				break;
			default:
				break;
		  }
		  return result;
	  }

	  private static boolean isInnerClass(ClassTree tree) {
	    Symbol owner = tree.symbol().owner();
	    return owner != null && !owner.isUnknown() && owner.isTypeSymbol();
	  }
	
}
