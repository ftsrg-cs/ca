package hu.bme.mit.ca.bmc;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import hu.bme.mit.theta.formalism.cfa.CFA;

public final class Application {

	public static void main(final String[] args) {
		final Options options = new Options();

		final Option optSource = new Option("s", "source", true, "Path of the .c source file");
		optSource.setRequired(true);
		optSource.setArgName("filename");
		options.addOption(optSource);

		final Option optBound = new Option("b", "bound", true, "Bound of the analysis");
		optBound.setRequired(true);
		optBound.setArgName("bound");
		options.addOption(optBound);

		final Option optTimeout = new Option("t", "timeout", true, "Timeout of the analysis in seconds (default: 10)");
		optTimeout.setArgName("timeout");
		options.addOption(optTimeout);

		final OptionGroup optEncoidng = new OptionGroup();
		final Option optLbe = new Option("lbe", "Large block encoding");
		final Option optSbe = new Option("sbe", "Single block encoding (default)");
		optEncoidng.addOption(optLbe);
		optEncoidng.addOption(optSbe);
		options.addOptionGroup(optEncoidng);

		final CommandLineParser parser = new DefaultParser();
		final HelpFormatter helpFormatter = new HelpFormatter();
		final CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
		} catch (final ParseException e) {
			helpFormatter.printHelp("ca-bmc.jar", options, true);
			return;
		}

		final String source = cmd.getOptionValue(optSource.getOpt());
		final int bound = Integer.parseInt(cmd.getOptionValue(optBound.getOpt()));
		final boolean lbe = cmd.hasOption(optLbe.getOpt());
		final int timeout = cmd.hasOption(optTimeout.getOpt())
				? Integer.parseInt(cmd.getOptionValue(optTimeout.getOpt())) : 10;

		final CFA cfa;
		if (lbe) {
			cfa = SourceToCfaTransformer.largeBlockEncoding(source);
		} else {
			cfa = SourceToCfaTransformer.singleBlockEncoding(source);
		}

		final BoundedModelChecker checker = new BoundedModelChecker(cfa, bound, timeout);
		final SafetyStatus result = checker.check();

		System.out.println(result);
	}

}
