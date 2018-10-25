package preprocessing;

import java.io.IOException;

/**
 * @author Phillip
 */
public class PDFToTextConverter {


    /**
     * Converts a PDF-file to a plaintext file.
     * @param path The path where to file to convert is found.
     * @param isDeployMode Indicates whether or not the software is used locally.
     */
    public void convertPDFToText(String path, boolean isDeployMode) {
        try {
            // PDF in Text umwandeln

            if(!isDeployMode) {
                ProcessBuilder pb = new ProcessBuilder("src/main/resources/preprocessing/pdftotext", "-enc", "UTF-8", path);

                Process p = pb.start();
                p.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("lawstats/preprocessing/pdftotext", "-enc", "UTF-8", path);

                Process p = pb.start();
                p.waitFor();
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
