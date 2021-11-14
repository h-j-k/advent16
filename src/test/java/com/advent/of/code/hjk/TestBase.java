package com.advent.of.code.hjk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class TestBase {

    public List<String> getInput() {
        try {
            return Files.readAllLines(
                    Paths.get("build", "resources", "test", this.getClass().getSimpleName().replace("Test", ".txt"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
