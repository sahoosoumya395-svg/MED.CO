package com.med.co.security;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Base64;

import javax.imageio.ImageIO;

public class CaptchaGenerator {

    private static final String CHARACTERS =
            "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private static final SecureRandom RANDOM =
            new SecureRandom();

    public static String generateCaptchaText(int length) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {

            sb.append(
                    CHARACTERS.charAt(
                            RANDOM.nextInt(CHARACTERS.length())
                    )
            );
        }

        return sb.toString();
    }

    public static String generateCaptchaImage(String text) {

        try {

            int width = 180;
            int height = 60;

            BufferedImage image =
                    new BufferedImage(width, height,
                            BufferedImage.TYPE_INT_RGB);

            Graphics2D g = image.createGraphics();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);

            for (int i = 0; i < 12; i++) {

                g.setColor(new Color(
                        RANDOM.nextInt(255),
                        RANDOM.nextInt(255),
                        RANDOM.nextInt(255)));

                g.drawLine(
                        RANDOM.nextInt(width),
                        RANDOM.nextInt(height),
                        RANDOM.nextInt(width),
                        RANDOM.nextInt(height));
            }

            g.setStroke(new BasicStroke(2));

            g.setFont(new Font("Arial", Font.BOLD, 34));

            FontMetrics metrics = g.getFontMetrics();

            int x = 20;

            for (char c : text.toCharArray()) {

                g.setColor(new Color(
                        RANDOM.nextInt(150),
                        RANDOM.nextInt(150),
                        RANDOM.nextInt(150)));

                int y = 35 + RANDOM.nextInt(15);

                double angle =
                        (RANDOM.nextDouble() - 0.5) * 0.5;

                g.rotate(angle, x, y);

                g.drawString(String.valueOf(c), x, y);

                g.rotate(-angle, x, y);

                x += metrics.charWidth(c) + 8;
            }

            for (int i = 0; i < 500; i++) {

                image.setRGB(
                        RANDOM.nextInt(width),
                        RANDOM.nextInt(height),
                        new Color(
                                RANDOM.nextInt(255),
                                RANDOM.nextInt(255),
                                RANDOM.nextInt(255)
                        ).getRGB());
            }

            g.dispose();

            ByteArrayOutputStream baos =
                    new ByteArrayOutputStream();

            ImageIO.write(image, "png", baos);

            return "data:image/png;base64,"
                    + Base64.getEncoder()
                    .encodeToString(baos.toByteArray());

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}