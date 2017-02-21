# Program verification II: predicate abstraction

## Opening in Eclipse

- In Eclipse, go to **Import** | **Gradle Project** and import this project.
- Right click on the `hu.bme.mit.ca.bmc` project. Select **Build path** | **Configure Build Path...** and on the `Libraries` tab select **Project and External Dependencies** | **Native library location**. Click on **Edit** and browse the `lib` folder that is in the root directory of the project.
- Run `FrameworkTest` as JUnit Test
- If the native dependencies are still missing, try the following command from this directory:

    ```
    sudo cp hu.bme.mit.ca.bmc/lib/libz3*so /usr/lib/
    ```

## Buiding with Gradle

- Run `./gradlew build` from the command line

## Tasks

Implement the following methods:

- Method `check()` in class `CegarChecker`: the CEGAR loop. You can implement this method using `Abstractor`, `Refiner` and `PredPrecision.join()`.

- Method `buildAbstraction()` in class `Abstractor`: construction of the abstract reachability graph. You can implement this method in terms of methods `close()` and `expand()`.

- Method `expand(ArgNode)` in class `Abstractor`: expanding a non-covered leaf node with all its abstract successors.

- Method `close(ArgNode)` in class `Abstractor`: covering a non-covered leaf node with an already reached node if possible.
