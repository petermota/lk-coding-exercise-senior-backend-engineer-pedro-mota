package com.lingokids.mtg;

import com.lingokids.mtg.exceptions.PrintExceptionMessageHandler;
import com.lingokids.mtg.factories.CardLoaderServiceFactory;
import com.lingokids.mtg.factories.FilterServiceFactory;
import com.lingokids.mtg.factories.GroupingServiceFactory;
import com.lingokids.mtg.factories.PrinterServiceFactory;
import com.lingokids.mtg.model.Card;
import com.lingokids.mtg.services.CardLoaderService;
import com.lingokids.mtg.services.FilterService;
import com.lingokids.mtg.services.GroupingService;
import com.lingokids.mtg.services.PrinterService;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Main entry point. It uses Java Picocli library to implement a standar CLI with help messages
 * and options to retrieve, filter, group and print List of Cards.
 *
 */
@Command(name = "mtg",
        mixinStandardHelpOptions = true,
        version = "mtg 0.1",
        description = "Query, Filter and Group CLI for 'Magic The Gathering' cards.")
public class MagicTheGathering implements Callable<Integer> {

    @Option(names = {"-p", "--properties"},
            split = ",",
            description = {"Properties separated by commas to show in result.",
                    "If not property is specified all of them will be shown."})
    private Set<String> properties;

    @Option(names = {"-g", "--group"},
            split = ",",
            description = "Group by fields. Up to 2 fields can be specified separated by commas")
    private List<String> groupBy;

    @Option(names = {"-r", "--refresh"},
            description = "Forces a re-read of all the cards from the API instead of reading them from local file")
    private boolean refresh;

    @Option(names = {"--pretty"},
            description = "Pretty prints the output JSON")
    private boolean pretty;

    @Option(names = {"-u", "--url"},
            description = "Uses the provided url instead of the standard one https://api.magicthegathering.io/v1/cards")
    private String url;

    @Parameters(description = {"Filters in the form of field1=value1 field2=value2...fieldN=valueN",
            "If not filters are specified then all cards are returned"})
    private Map<String, String> filters;

    /**
     * External dependencies solved at construction time through Factory pattern
     */
    private final CardLoaderService cardLoaderService;
    private final FilterService filterService;
    private final GroupingService groupingService;
    private final PrinterService printerService;

    public MagicTheGathering() {
        cardLoaderService = CardLoaderServiceFactory.getCardLoaderServiceInstance();
        filterService = FilterServiceFactory.getFilterServiceInstance();
        groupingService = GroupingServiceFactory.getGroupingServiceInstance();
        printerService = PrinterServiceFactory.getPrinterServiceInstance();
    }

    /**
     * Steps
     *
     * (1) Get the cards
     * (2) Filter them
     * (3) Group them
     * (4) Output to stdout
     *
     * @return code (0 means OK)
     * @throws Exception
     */
    @Override
    public Integer call() throws Exception {
        // Can only group by a maximum of 2 fields. Check it at the beginning
        if (groupBy != null && groupBy.size() > 2) {
            throw new IllegalArgumentException("Can only group by a maximum of 2 fields.");
        }

        List<Card> cards = cardLoaderService.getCards(url, refresh);
        cards = filterService.filter(cards, filters);
        Object result = groupingService.groupBy(cards, groupBy);
        printerService.print(result, properties, pretty);

        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new MagicTheGathering())
                .setExecutionExceptionHandler(new PrintExceptionMessageHandler())
                .execute(args);

        System.exit(exitCode);
    }
}
