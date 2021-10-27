package cellsociety.view;

import javafx.scene.paint.Color;

public interface PanelListener {
    void updateLanguage(String newLanguage);

    void resetScreen();

    void updateColorScheme(Color newColor);

    void loadNewFile(String filename);

    void saveCurrentFile();
}
