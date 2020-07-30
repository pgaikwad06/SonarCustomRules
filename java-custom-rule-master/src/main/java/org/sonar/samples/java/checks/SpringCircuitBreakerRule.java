/*
 * SonarQube Java Custom Rules Example
 * Copyright (C) 2016-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.samples.java.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.semantic.Symbol.TypeSymbol;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.AssignmentExpressionTree;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;



@Rule(key = "SpringCircuitBreaker", description = "The fallback method parameter at @HystrixCommand must be declared and defined with same name.", priority = Priority.MINOR, tags = {
"bugs" })
public class SpringCircuitBreakerRule extends BaseTreeVisitor implements JavaFileScanner{

	private JavaFileScannerContext context;

	@Override
	public void scanFile(JavaFileScannerContext context) {
		this.context = context;

		scan(context.getTree());
	}

	@Override
	public void visitClass(ClassTree tree) {
		TypeSymbol classSymbol = tree.symbol();
		if (isServiceContext(classSymbol)) {
			for (Tree member : tree.members()) {
				if (member.is(Kind.METHOD))
					checkAnnotationHystrix((MethodTree) member, tree);
			}			
		}
		super.visitClass(tree);
		
	}

	private boolean isServiceContext(TypeSymbol classSymbol) {
		return classSymbol.metadata().isAnnotatedWith("org.springframework.stereotype.Service");
	}

	private void checkAnnotationHystrix(MethodTree methodTree,ClassTree tree) {
		System.out.println("Here In checkAnnotationHystrix");
		String methodName = "";
		boolean methodFound = false;

		if (methodTree.symbol().metadata().isAnnotatedWith("com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand")) {
			for (AnnotationTree annotationTree : methodTree.modifiers().annotations()) {
				for (ExpressionTree argument : annotationTree.arguments()) {
					if (argument.is(Tree.Kind.ASSIGNMENT)) {
						AssignmentExpressionTree assignment = (AssignmentExpressionTree) argument;

						if (((IdentifierTree) assignment.variable()).name().equalsIgnoreCase("fallbackMethod"))
							methodName = assignment.expression().firstToken().text().replaceAll("^\"|\"$", "");
					}
				}
			}
			System.out.println(" MethodName : "+ methodName);
			if(!methodName.isEmpty()) {
				for (Tree member : tree.members()) {
					if (member.is(Kind.METHOD)) 
						methodFound = methodName.equals((((MethodTree) member).simpleName().name()));					 
				}
				if(!methodFound)
					context.reportIssue(this,methodTree, "fallbackMethod must be defined.");
			} else {
				System.out.println("issue found");
				context.reportIssue(this,methodTree, "fallbackMethod must be declared in the HystrixCommand annotation.");
			}				
		}
	}
}
