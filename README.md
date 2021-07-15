# Backend coding exercise

For this coding exercise you will use the public API provided by https://magicthegathering.io to build a command line tool that can query and filter Magic The Gathering cards.

## The exercise

Using **only** the https://api.magicthegathering.io/v1/cards endpoint and **without using any query parameters for filtering**, create a command-line tool that can be used by one of our engineers to query, filter, and group cards. Ensure that your tool can be used to return the following example lists:

* A list of **Cards** grouped by **`set`**.
* A list of **Cards** grouped by **`set`** and within each **`set`** grouped by **`rarity`**.
* A list of cards from the  **Khans of Tarkir (KTK)** set that ONLY have the colours `red` **AND** `blue`

Once you've finished coding, spend some time adding a section to this README explaining how you approached the problem: initial analysis, technical choices, trade-offs, etc.

## Environment

* You can use any one of the following programming languages: Ruby, Elixir, JavaScript, Crystal, Python, Go. We want to see you at your best, so use the language you're most comfortable with.
* If you introduce any dependencies outside of your programming language's standard library, document why you added them in this README.

## Limitations

* You are **not** allowed to use a third-party library that wraps the MTG API.
* You can only use the https://api.magicthegathering.io/v1/cards endpoint for fetching all the cards. You **can't use query parameters** to filter the cards.

## What we look for

* Organise your code with low coupling and high cohesion.
* Avoid unnecessary abstractions. Make it readable, not vague.
* Name things well. We know _naming things_ is one of the two hardest problems in programming, so try to not make your solution too cryptic or too clever. Avoid the use of [weasel words](https://gist.github.com/tmcw/35849b7e9b86bb0c125972b2bb275bc7).
* Adhere to your language/framework conventions. No need to get _too_ creative.
* Take a pragmatic approach to testing. Just make sure the basics are covered.
* Take your time. We set no time limit, so simply let us know roughly how much time you spent. By our estimate, it should take roughly 4 hours, but it's not an issue if you go over this.

# Programmer comments

## Initial analysis

After reading the instructions and the API documentation, it was clear that I had to do a lot of HTTP requests just to get the list of cards. It became evident that doing that every time would make the CLI useless, so I thought about a way to store the list of cards retrieved in a local file. Second usage of the CLI would read the list of cards from local file and make the application really fast. That's why in the code you will see these classes:

- CardLoaderService - If no local file is in $HOME or a refresh is forced using -r or --refresh, then it will read the cards from the API.
- APIAssembler - Orchestrates all the HTTP calls and assembles all the responses in a single list of cards.
- HTTPService - Makes the real HTTP calls.
- StorageService - Checks if local file exists, read, write and delete.

I decided to break all the logic in small services, so they can specialize in doing one thing and do it really well. For example HTTPService could be optimized to use HTTP connection pooling, connection reuse, etc. Storage service could be changed to do all the things in a database instead of a local filesystem.

The most important services are:

- FilterService - Filters a list of Cards with provided filters.
- GroupingService - Groups the resulting filtered list by none, one or two fields.

All of them have an interface and a real implementation. Implementations could be changed easily without affecting the consumer. I followed 'Gang of four' principle: "Write to an interface, not an implementation". This fulfills the requisite about low coupling and high cohesion.

## Dependency injection

Initially I thought about a dependency injection framework like Spring Boot, Micronaut, Quarkus, etc. Quickly I decided not to use them for a simple scenario like this. All these frameworks add tons of dependencies, and it's better to use them when you have a real complex application to be developed. So I decided to use a 'primitive' and well known pattern like 'Factory pattern' for the creation of instances and solving the dependency injection issue.

## Technical decisions

These were my technical decisions and why I took them:

- Use Picocli. It's a quite good library to create CLI commands like any Unix command we are used to. I took this as a guideline to create a good CLI: https://clig.dev/#guidelines  
- Testing Libraries. I have used JUnit and Mockito. I have used them a lot of times, and they are powerful and easy to use. I have also used Wiremock to simulate a web server and test HttpServiceImpl.
- Lombok. When I write a POJO I don't want to write getters, setters, constructors, toString methods, etc. With Lombok and few annotations you get the same result, and the final class is easier to read and understand.
- Jackson JSON serializer/deserializer. I have used a lot of times when creating Spring Boot Microservices, so I am used to it.

## Compiling

A JDK 8 or newer is required to compile this application. Maven should also be installed. After cloning the project, this Maven command will compile, run the unit tests and create 2 shell scripts for Windows and Unix systems (Linux and MacOS systems):

```bash
mvn clean package appassembler:assemble
```

## Usage

Having built the application we can start using the CLI.

On Windows we can get help if we write this command:

```bash
target\appassembler\bin\mtg.bat --help
```

On Unix systems:

```bash
./target/appassembler/bin/mtg --help
```

We will see an output like this:

```bash
Usage: mtg [-hrV] [--pretty] [-u=<url>] [-g=<groupBy>[,<groupBy>...]]...
           [-p=<properties>[,<properties>...]]... [<String=String>...]
Query, Filter and Group CLI for 'Magic The Gathering' cards.
      [<String=String>...]   Filters in the form of field1=value1
                               field2=value2...fieldN=valueN
                             If not filters are specified then all cards are
                               returned
  -g, --group=<groupBy>[,<groupBy>...]
                             Group by fields. Up to 2 fields can be specified
                               separated by commas
  -h, --help                 Show this help message and exit.
  -p, --properties=<properties>[,<properties>...]
                             Properties separated by commas to show in result.
                             If not property is specified all of them will be
                               shown.
      --pretty               Pretty prints the output JSON
  -r, --refresh              Forces a re-read of all the cards from the API
                               instead of reading them from local file
  -u, --url=<url>            Uses the provided url instead of the standard one
                               https://api.magicthegathering.io/v1/cards
  -V, --version              Print version information and exit.
```

Getting all the cards. Just use the command without any extra parameters. All the cards will start to be downloaded from the API. Take it easy, it will take several minutes. **You can skip this step decompressing and moving the file named 'mtg_cards.zip' in the test directory to $HOME/mtg_cards.json.**

```bash
$ ./target/appassembler/bin/mtg
Retrieving cards from API https://api.magicthegathering.io/v1/cards
1000 cards retrieved
2000 cards retrieved
...etc...

```

You will see all the cards output to the terminal. Now a file named 'mtg_cards.json' will appear in your $HOME directory. We can start querying, filtering, grouping and without the need to download all the cards from the API again:

```bash
$ ./target/appassembler/bin/mtg --properties=id,name,set,rarity,colors set=ktk colors=red,blue --pretty
Reading cards from file C:\Users\peter\mtg_cards.json
Applying filters = {set=ktk, colors=red,blue} to 58169 cards.
3 cards after filtering.
[ {
  "id" : "c15f231e-2a1a-549a-ac39-cdaa4f99b93a",
  "name" : "Master the Way",
  "colors" : [ "Red", "Blue" ],
  "rarity" : "Uncommon",
  "set" : "KTK"
}, {
  "id" : "9d69f2f9-1a9d-5de3-b455-e4c7501c9b2f",
  "name" : "Mindswipe",
  "colors" : [ "Red", "Blue" ],
  "rarity" : "Rare",
  "set" : "KTK"
}, {
  "id" : "17dfef20-c08a-5f61-a784-8699bb3f0739",
  "name" : "Winterflame",
  "colors" : [ "Red", "Blue" ],
  "rarity" : "Uncommon",
  "set" : "KTK"
} ]
```

Here we have used several features: show only certain fields with --properties option, pretty print the output so it is more user readable with --pretty and 2 filters. **Filters, groups and queries are not case-sensitive**. We can make more complex queries:

```bash
$ ./target/appassembler/bin/mtg --properties=id,name,set,rarity,colors set=ktk,znr colors=blue rarity=mythic --pretty
Reading cards from file C:\Users\peter\mtg_cards.json
Applying filters = {set=ktk,znr, colors=blue, rarity=mythic} to 58169 cards.
8 cards after filtering.
[ {
  "id" : "e502db19-a246-5e79-ab37-640acc996e03",
  "name" : "Clever Impersonator",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "KTK"
}, {
  "id" : "05f453ea-19e7-5ca7-8cd3-04da14190ec9",
  "name" : "Pearl Lake Ancient",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "KTK"
}, {
  "id" : "19914d4f-3f02-5050-a11e-c1c72acda20d",
  "name" : "Jace, Mirror Mage",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "ZNR"
}, {
  "id" : "302d4e22-af48-562b-86f3-182b142969f2",
  "name" : "Sea Gate Restoration // Sea Gate, Reborn",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "ZNR"
}, {
  "id" : "413aadac-d2ed-5f73-9331-051e0d7605ee",
  "name" : "Sea Gate Stormcaller",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "ZNR"
}, {
  "id" : "1e6cfbf9-83b7-5999-a544-d2b78b9f1a30",
  "name" : "Jace, Mirror Mage",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "ZNR"
}, {
  "id" : "445127ae-b2dd-5346-b98d-f894a11dcba6",
  "name" : "Sea Gate Restoration // Sea Gate, Reborn",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "ZNR"
}, {
  "id" : "a83f7191-b210-53af-82ff-1d5cf763d999",
  "name" : "Sea Gate Stormcaller",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "ZNR"
} ]
```

All messages from the application will be redirected to stderr, so they will not appear if we redirect the output to a file:

```bash
$ ./target/appassembler/bin/mtg --properties=id,name,set,rarity,colors set=ktk,znr colors=blue rarity=mythic --pretty > result.json
Reading cards from file C:\Users\peter\mtg_cards.json
Applying filters = {set=ktk,znr, colors=blue, rarity=mythic} to 58169 cards.
8 cards after filtering.
```

The content of the file will be (notice there are no messages);

```bash
[ {
  "id" : "e502db19-a246-5e79-ab37-640acc996e03",
  "name" : "Clever Impersonator",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "KTK"
}, {
  "id" : "05f453ea-19e7-5ca7-8cd3-04da14190ec9",
  "name" : "Pearl Lake Ancient",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "KTK"
}, {
  "id" : "19914d4f-3f02-5050-a11e-c1c72acda20d",
  "name" : "Jace, Mirror Mage",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "ZNR"
}, {
  "id" : "302d4e22-af48-562b-86f3-182b142969f2",
  "name" : "Sea Gate Restoration // Sea Gate, Reborn",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "ZNR"
}, {
  "id" : "413aadac-d2ed-5f73-9331-051e0d7605ee",
  "name" : "Sea Gate Stormcaller",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "ZNR"
}, {
  "id" : "1e6cfbf9-83b7-5999-a544-d2b78b9f1a30",
  "name" : "Jace, Mirror Mage",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "ZNR"
}, {
  "id" : "445127ae-b2dd-5346-b98d-f894a11dcba6",
  "name" : "Sea Gate Restoration // Sea Gate, Reborn",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "ZNR"
}, {
  "id" : "a83f7191-b210-53af-82ff-1d5cf763d999",
  "name" : "Sea Gate Stormcaller",
  "colors" : [ "Blue" ],
  "rarity" : "Mythic",
  "set" : "ZNR"
} ]
```

Now let's try to group the cards from the **Khans of Tarkir (KTK)** set by rarity having ONLY red and blue colors:

```bash
$ ./target/appassembler/bin/mtg --properties=id,name,set,rarity,colors set=ktk colors=red,blue --pretty
Reading cards from file C:\Users\peter\mtg_cards.json
Applying filters = {set=ktk, colors=red,blue} to 58169 cards.
3 cards after filtering.
[ {
  "id" : "c15f231e-2a1a-549a-ac39-cdaa4f99b93a",
  "name" : "Master the Way",
  "colors" : [ "Red", "Blue" ],
  "rarity" : "Uncommon",
  "set" : "KTK"
}, {
  "id" : "9d69f2f9-1a9d-5de3-b455-e4c7501c9b2f",
  "name" : "Mindswipe",
  "colors" : [ "Red", "Blue" ],
  "rarity" : "Rare",
  "set" : "KTK"
}, {
  "id" : "17dfef20-c08a-5f61-a784-8699bb3f0739",
  "name" : "Winterflame",
  "colors" : [ "Red", "Blue" ],
  "rarity" : "Uncommon",
  "set" : "KTK"
} ]
```

The same grouped by rarity:

```bash
$ ./target/appassembler/bin/mtg --properties=id,name,set,rarity,colors set=ktk colors=red,blue --group=rarity --pretty
Reading cards from file C:\Users\peter\mtg_cards.json
Applying filters = {set=ktk, colors=red,blue} to 58169 cards.
3 cards after filtering.
{
  "Rare" : [ {
    "id" : "9d69f2f9-1a9d-5de3-b455-e4c7501c9b2f",
    "name" : "Mindswipe",
    "colors" : [ "Red", "Blue" ],
    "rarity" : "Rare",
    "set" : "KTK"
  } ],
  "Uncommon" : [ {
    "id" : "c15f231e-2a1a-549a-ac39-cdaa4f99b93a",
    "name" : "Master the Way",
    "colors" : [ "Red", "Blue" ],
    "rarity" : "Uncommon",
    "set" : "KTK"
  }, {
    "id" : "17dfef20-c08a-5f61-a784-8699bb3f0739",
    "name" : "Winterflame",
    "colors" : [ "Red", "Blue" ],
    "rarity" : "Uncommon",
    "set" : "KTK"
  } ]
}
```

Now all the cards grouped by rarity and then by set having ONLY red and blue colors:

```bash
$ ./target/appassembler/bin/mtg --properties=id,name,set,rarity,colors --group=rarity,set --pretty colors=red,blue
Reading cards from file C:\Users\peter\mtg_cards.json
Applying filters = {colors=red,blue} to 58169 cards.
423 cards after filtering.
{
  "Uncommon" : {
    "RIX" : [ {
      "id" : "81bb9071-b002-5cfa-b057-eac428e0f1fa",
      "name" : "Storm Fleet Sprinter",
      "colors" : [ "Red", "Blue" ],
      "rarity" : "Uncommon",
      "set" : "RIX"
    } ],
    "KLD" : [ {
      "id" : "a3497e66-3050-5355-8374-b247289a2c84",
      "name" : "Whirler Virtuoso",
      "colors" : [ "Red", "Blue" ],
      "rarity" : "Uncommon",
      "set" : "KLD"
    } ],
    "KTK" : [ {
      "id" : "c15f231e-2a1a-549a-ac39-cdaa4f99b93a",
      "name" : "Master the Way",
      "colors" : [ "Red", "Blue" ],
      "rarity" : "Uncommon",
      "set" : "KTK"
    }, {
      "id" : "17dfef20-c08a-5f61-a784-8699bb3f0739",
      "name" : "Winterflame",
      "colors" : [ "Red", "Blue" ],
      "rarity" : "Uncommon",
      "set" : "KTK"
    } ],
    "SLD" : [ {
      "id" : "709ff9c9-890f-5e52-a7c8-c05cbcd946f8",
      "name" : "Saheeli, Sublime Artificer",
      "colors" : [ "Red", "Blue" ],
      "rarity" : "Uncommon",
      "set" : "SLD"
    } ],
    "PRM" : [ {
      "id" : "7e340e28-2649-5f70-81b6-d183c7ceaffd",
      "name" : "Electrolyze",
      "colors" : [ "Red", "Blue" ],
      "rarity" : "Uncommon",
      "set" : "PRM"
    }, {
      "id" : "be4f1446-43ec-5ab3-8b99-82d04d0e7f17",
      "name" : "Izzet Charm",
      "colors" : [ "Red", "Blue" ],
      "rarity" : "Uncommon",
      "set" : "PRM"
    } ],
...
output omitted
```

## Improvements

- Speed when making HTTP requests through connection reuse, connection pooling, etc.
- Use GraalVM to create a native executable binary. Better startup time and less memory used at runtime. I decided not to do it because it would make more difficult to build the executable. In a production environment it would be nice to have.  
- Better management of queries involving numbers. Right now 1 != 1.0
- Output to stdout looses UTF-8 encoding. International characters will be shown as '?'. This problem will be solved doing the output to a file instead of stdout.

## Time to develop this CLI

At the end I liked a lot this challenge, so I designed with this in mind: "do a tool that I would like to use myself". So I wanted to filter and group by any field. Also I wanted a fast cli command, so I cached the response from the API in a local file, etc.

It took me a little more than 10 hours in total.
