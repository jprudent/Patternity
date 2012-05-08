package com.patternity.rule.basic;

import com.patternity.ast.ClassElement;
import com.patternity.ast.MethodElement;
import com.patternity.rule.Rule;
import com.patternity.rule.RuleContext;

interface OverrideMethodRule extends Rule {
  String getSignature();
  String getTag();
}
class OverrideObjectMethodChecker {

  private OverrideObjectMethodChecker() {
   throw new AssertionError("Utility class");
  }

  static void reportViolation(final OverrideMethodRule rule, final ClassElement element, final RuleContext context) {
    if (rule == null) throw new NullPointerException("rule is mandatory");
    if (element == null) throw new NullPointerException("element is mandatory");
    if (context == null) throw new NullPointerException("context is mandatory");

    if (!context.isMarked(element, rule.getTag())) return;

    for (MethodElement method : element.getMethods()) {
      if (rule.getSignature().equals(method.getSignature())) {
        return;
      }
    }
    context.reportViolation(rule, rule.getSignature() + " is not overridden");
  }
}
