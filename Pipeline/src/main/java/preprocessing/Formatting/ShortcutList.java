package preprocessing.Formatting;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 27.02.2017.
 */
public class ShortcutList {

    private List<String> shortcuts = new ArrayList<String>();

    public ShortcutList(boolean isDeployMode) {
        String line = "";

        try {
            InputStream is;
            if (isDeployMode) {
                is = new FileInputStream("/lawstats/preprocessing/shortcuts.txt");
            } else {
                is = new FileInputStream("./resources/preprocessing/shortcuts.txt");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {

                shortcuts.add(line);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<String> getShortcuts() {
        return shortcuts;
    }

}