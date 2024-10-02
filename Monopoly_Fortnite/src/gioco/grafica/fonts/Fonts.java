package gioco.grafica.fonts;

import java.awt.*;
import java.io.*;

public class Fonts {
    /**
     * @param size grandezza dei caratteri
     * @return font da applicare ai caratteri
     */
    public static Font getFont(float size){
        Font customFont = null;
        try {

            File fontFile = new File("Resources/fonts/fortnite.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        return customFont;
    }
}
