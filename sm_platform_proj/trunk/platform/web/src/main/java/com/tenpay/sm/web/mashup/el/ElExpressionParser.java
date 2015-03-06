/**
 * 
 */
package com.tenpay.sm.web.mashup.el;

import org.springframework.binding.expression.Expression;

import org.springframework.binding.expression.ParserException;
import org.springframework.binding.expression.SettableExpression;

import com.tenpay.sm.web.mashup.el.AbstractExpressionParser;

/**
 * @author li.hongtl
 *
 */
public class ElExpressionParser extends AbstractExpressionParser {
	public ElExpressionParser() {
	}
	
	@Override
	protected Expression doParseExpression(String expressionString) throws ParserException {
		return doParseSettableExpression(expressionString);
	}

	@Override
	protected SettableExpression doParseSettableExpression(String expressionString) throws ParserException, UnsupportedOperationException {
		ElExpression expression = new ElExpression(expressionString);
		return expression;
	}



}
