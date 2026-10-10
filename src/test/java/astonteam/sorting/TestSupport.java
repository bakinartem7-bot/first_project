package astonteam.sorting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public final class TestSupport {

    private TestSupport() {
    }

    public record Outcome(String output, RuntimeException error) {
    }

    public static Outcome run(String input, Runnable action) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        RuntimeException error = null;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(capture);
            action.run();
        } catch (RuntimeException e) {
            error = e;
        } finally {
            capture.flush();
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return new Outcome(buffer.toString(StandardCharsets.UTF_8), error);
    }
}
