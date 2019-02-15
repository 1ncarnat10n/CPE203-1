import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.*;


public class modifyPoints{

    private static final String outputFileName = "drawMe.txt";

    public static void writeModifiedInput(String fileName) {

        try (Stream<String> inputFileStream = Files.lines(Paths.get(fileName))) {
            BufferedWriter fileOutputBuffer = new BufferedWriter(new FileWriter(outputFileName));

            String[] result = inputFileStream
                    .filter((x) -> Double.parseDouble(x.split(",")[x.split(",").length - 1]) <= 2.0)
                    .map((x) -> Arrays.stream(x.split(","))
                            .map((y) -> Double.toString(Double.parseDouble(y) * 0.5))
                            .collect(Collectors.joining(", ")))
                    .map((x) -> translatePt(x))
                    .toArray(String[]::new);

            for (String line: result) {
                fileOutputBuffer.write(line + "\n");
                System.out.println(line);
            }
            
            fileOutputBuffer.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open " + fileName);
        } catch (IOException ex) {
            System.out.println("Unable to read " + fileName);
        }>
    }

    private static String translatePt(String _input) {
        String[] result = _input.split(", ");
        result[0] = Double.toString(Double.parseDouble(result[0]) - 150.0);
        result[1] = Double.toString(Double.parseDouble(result[1]) - 37.0);
        return result[0] + ", " + result[1] + ", " + result[2];
    }

}
