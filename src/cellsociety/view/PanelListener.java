package cellsociety.view;

import javafx.scene.paint.Color;

import java.util.Locale;

public interface PanelListener {
    void updateLanguage(String newLanguage);

    void clearScreen();

    void updateColorScheme(Color newColor);
}
