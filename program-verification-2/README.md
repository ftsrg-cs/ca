# Program verification II: predicate abstraction

## Buiding with Gradle

- Run `./gradlew build` from the command line

## Tasks

Implement the following methods:

- Method `check()` in class `CegarChecker`: the CEGAR loop. You can implement this method using `Abstractor`, `Refiner` and `PredPrecision.join()`.

- Method `buildAbstraction()` in class `Abstractor`: construction of the abstract reachability graph. You can implement this method in terms of methods `close()` and `expand()`.

- Method `expand(ArgNode)` in class `Abstractor`: expanding a non-covered leaf node with all its abstract successors.

- Method `close(ArgNode)` in class `Abstractor`: covering a non-covered leaf node with an already reached node if possible.

## Utilities

You can use `ArgVisualizer` to transform the reachability tree to an instance of `Graph` that can be serialized to Graphviz format using `GraphvizWriter`. The class `CfaVisualizer` serves the same purpose for CFAs.

To quickly viusalize a `Graph`, use `GraphvizWriter.writeString()` and render the result using http://www.webgraphviz.com/.
