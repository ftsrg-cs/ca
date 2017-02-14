# Program verification I: Bounded model checking

## Opening in Eclipse

- In Eclipse, go to **Import** | **Gradle Project** and import this project.
- Right click on the `hu.bme.mit.ca.bmc` project. Select **Build path** | **Configure Build Path...** and on the `Libraries` tab select **Project and External Dependencies** | **Native library location**. Click on **Edit** and browse the `lib` folder that is in the root directory of the project.
- Run `FrameworkTest` as JUnit Test
- If the native dependencies are still missing, try the following command from this directory:

    ```
    sudo cp hu.bme.mit.ca.bmc/lib/libz3*so /usr/lib/
    ```

## Buiding with Gradle

- `./gradlew build` for normal build
- `./gradlew shadow` to obtain fat jar
