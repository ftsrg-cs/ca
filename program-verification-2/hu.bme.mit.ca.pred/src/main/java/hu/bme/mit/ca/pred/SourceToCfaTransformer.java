package hu.bme.mit.ca.pred;

import hu.bme.mit.theta.formalism.cfa.CFA;
import hu.bme.mit.theta.frontend.c.cfa.FunctionToCFATransformer;
import hu.bme.mit.theta.frontend.c.ir.Function;
import hu.bme.mit.theta.frontend.c.ir.GlobalContext;
import hu.bme.mit.theta.frontend.c.parser.Parser;

final class SourceToCfaTransformer {

	public static CFA largeBlockEncoding(final String filepath) {
		final GlobalContext context = Parser.parse(filepath);
		final Function function = context.getEntryPoint();
		final CFA result = FunctionToCFATransformer.createLBE(function);
		return result;
	}

	public static CFA singleBlockEncoding(final String filepath) {
		final GlobalContext context = Parser.parse(filepath);
		final Function function = context.getEntryPoint();
		final CFA result = FunctionToCFATransformer.createSBE(function);
		return result;
	}

}
