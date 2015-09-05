import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Jim on 9/5/2015.
 */
public class LevelController implements Initializable {

    @FXML
    StackPane root;
    @FXML
    Canvas canvas;

    private ArrayList<String> maps;
    private int currLvl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        maps = new ArrayList<>();
        findMaps();

        if (maps.size() > 0) {
            currLvl = 0;
            createLvl();
        } else {
            System.out.println("No maps found!");
        }

    }

    private void findMaps() {
        File folder = new File("c:/users/jim/workspace/bubblebobble/src/main/resources");
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (listOfFile.getName().matches("map[0-9]*.txt")) {
                    maps.add(listOfFile.getName());
                }
            }
        }
    }

    public void createLvl() {
        new Level(maps.get(currLvl), canvas);
    }

    public void nextLevel() {
        currLvl++;
        createLvl();
    }

}
