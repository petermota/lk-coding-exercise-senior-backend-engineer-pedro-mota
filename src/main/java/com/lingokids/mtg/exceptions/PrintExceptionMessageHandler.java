package com.lingokids.mtg.exceptions;

import picocli.CommandLine;
import picocli.CommandLine.ParseResult;

/**
 * This class is used by the Picocli library to print beauty error messages
 * to the user instead of a typical Java stack trace error.
 *
 */
public class PrintExceptionMessageHandler implements CommandLine.IExecutionExceptionHandler {
    @Override
    public int handleExecutionException(Exception e, CommandLine cmd,
                                        ParseResult parseResult) throws Exception {
        // bold red error message
        cmd.getErr().println(cmd.getColorScheme().errorText(e.getMessage()));

        return cmd.getExitCodeExceptionMapper() != null
                ? cmd.getExitCodeExceptionMapper().getExitCode(e)
                : cmd.getCommandSpec().exitCodeOnExecutionException();
    }
}
