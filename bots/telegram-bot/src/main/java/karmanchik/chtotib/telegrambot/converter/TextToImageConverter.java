package karmanchik.chtotib.telegrambot.converter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TextToImageConverter {
    public static final HashMap<RenderingHints.Key, Object> RENDERING_MAP = new HashMap<>();

    static {
        RENDERING_MAP.putAll(Map.of(
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
                RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE,
                RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON,
                RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR
        ));
        RENDERING_MAP.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        RENDERING_MAP.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        RENDERING_MAP.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    TextToImageConverter() { }

    public static BufferedImage render(String text, Font font, Integer width, Integer height) {
        BufferedImage image = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration()
                .createCompatibleImage(width, height);
        Graphics g2d = image.createGraphics();
        JEditorPane jep = new JEditorPane("text/html", text);
//        g2d.drawString(jep.getText(), font.getAscent());
        jep.setSize(width, height);
        jep.print(g2d);
        return image;
    }
}
