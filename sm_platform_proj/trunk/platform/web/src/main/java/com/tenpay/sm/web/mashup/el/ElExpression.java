/**
 * 
 */
package com.tenpay.sm.web.mashup.el;

import javax.servlet.jsp.el.ELException;

import org.apache.commons.el.ExpressionEvaluatorImpl;
import org.springframework.binding.expression.EvaluationAttempt;
import org.springframework.binding.expression.EvaluationContext;
import org.springframework.binding.expression.EvaluationException;
import org.springframework.binding.expression.SettableExpression;

/**
 * @author li.hongtl
 *
 */
public class ElExpression implements SettableExpression {
	public static ExpressionEvaluatorImpl expressionEvaluator = new ExpressionEvaluatorImpl(); 
	private String expressionString;
	public ElExpression() {
	}
	
	public ElExpression(String expressionString) {
		this.expressionString = expressionString;
	}

	/* (non-Javadoc)
	 * @see org.springframework.binding.expression.SettableExpression#evaluateToSet(java.lang.Object, java.lang.Object, org.springframework.binding.expression.EvaluationContext)
	 */
	public void evaluateToSet(Object target, Object value,
			EvaluationContext context) throws EvaluationException {
		//TODO 
		//暂时没实现

	}

	/* (non-Javadoc)
	 * @see org.springframework.binding.expression.Expression#evaluate(java.lang.Object, org.springframework.binding.expression.EvaluationContext)
	 */
	public Object evaluate(Object target, EvaluationContext context)
			throws EvaluationException {
		MyVariableResolverImpl vr = new MyVariableResolverImpl(target);
		try {
			//Object result = expressionEvaluator.evaluate("${"+expressionString+"}", Object.class, vr, null);
			Object result = expressionEvaluator.evaluate(expressionString, Object.class, vr, null);
			return result;
		} catch (ELException e) {
			//return this.expressionString;
			throw new EvaluationException(new EvaluationAttempt(this,target,context),e);
		}
	}

}
