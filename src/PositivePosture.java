import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PositivePosture {
    CognitiveServices cognitiveServices;
    BufferedImage image;
    DisplayFrame displayFrame;
    DisplayPanel panel;
    Webcam webcam;
    Capture captureThread;
    CalibrateCapture calibrationThread;
    int frameWidth = 1366, frameHeight = 768;
    int faceTop, faceLeft, faceWidth, faceHeight;
    boolean calibrated = false;

    public static void main(String[] args) {
        PositivePosture pp = new PositivePosture();
    }

    public PositivePosture() {
        setupCognitiveServices();
        setupWebcam();
        setupUI();
        startVideo();
    }

    public void startVideo() {
        webcam.open(true);
        long lastFrameTime = 0, frameTime = 16;
        while (calibrationThread == null) {
            if (System.currentTimeMillis() - lastFrameTime >= frameTime) {
                lastFrameTime = System.currentTimeMillis();
                panel.drawImage(webcam.getImage());
            }
        }
    }

    public void setupCognitiveServices() {
        cognitiveServices = new CognitiveServices("45480e1bce5d49028bf803d097db09c5");
    }

    public void setupWebcam() {
        webcam = Webcam.getDefault();
        Dimension[] nonStandardResolutions = new Dimension[] { WebcamResolution.PAL.getSize(),
                WebcamResolution.HD720.getSize(), };
        webcam.setCustomViewSizes(nonStandardResolutions);
        webcam.setViewSize(WebcamResolution.HD720.getSize());
    }

    public void setupUI() {
        displayFrame = new DisplayFrame(this, "Positive Posture", frameWidth, frameHeight);
        panel = displayFrame.panel;
    }

    public void runCalibrateCapture() {
        if (calibrationThread == null || !calibrationThread.isAlive()) {
            calibrationThread = new CalibrateCapture();
            calibrationThread.addListener(new ThreadListener() {
                @Override
                public void onFinished() {
                    displayFrame.calibratingLabel.setVisible(false);
                }
            });
            calibrationThread.start();
            displayFrame.calibratingLabel.setVisible(true);
        }
    }

    public void runCapture() {
        if (calibrated) {
            if (captureThread == null || !captureThread.isAlive()) {
                captureThread = new Capture();
                captureThread.start();
                displayFrame.captureLabel.setVisible(true);
            }
        }
    }

    class CalibrateCapture extends Thread {
        ThreadListener listener;

        public void addListener(ThreadListener listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            webcam.open();
            BufferedImage calibrateImage = webcam.getImage();
            webcam.close();
            JsonArray jsonArray = cognitiveServices.postLocalToFaceAPI(calibrateImage);
            if (jsonArray.size() > 0) {
                JsonObject faceRectangle = determineUser(jsonArray);
                faceTop = faceRectangle.get("top").getAsInt();
                faceLeft = faceRectangle.get("left").getAsInt();
                faceWidth = faceRectangle.get("width").getAsInt();
                faceHeight = faceRectangle.get("height").getAsInt();
                panel.drawImage(calibrateImage);
                panel.drawFaceRectangle(Color.GREEN, faceLeft, faceTop, faceWidth, faceHeight);
                calibrated = true;
            }
            listener.onFinished();
        }
    }

    class Capture extends Thread {
        long lastCaptureTime = 0;
        long timeBetweenCaptures = 5000;
        volatile boolean run = true;

        @Override
        public void run() {
            while (run) {
                if (System.currentTimeMillis() - lastCaptureTime >= timeBetweenCaptures) {
                    lastCaptureTime = System.currentTimeMillis();
                    webcam.open();
                    image = webcam.getImage();
                    webcam.close();
                    panel.drawImage(image);

                    JsonArray jsonArray = cognitiveServices.postLocalToFaceAPI(image);
                    if (jsonArray.size() > 0) {
                        JsonObject faceRectangle = determineUser(jsonArray);

                        int diffWidth = faceRectangle.get("width").getAsInt() - faceWidth;
                        int diffHeight = faceRectangle.get("height").getAsInt() - faceHeight;
                        boolean out = false;
                        if (diffWidth >= 30 && diffHeight >= 30) {
                            out = true;
                        }

                        Color drawColor = null;
                        if (out) {
                            triggerNotification();
                            drawColor = Color.BLUE;
                        } else {
                            drawColor = Color.RED;
                        }
                        panel.drawFaceRectangle(drawColor, faceRectangle.get("left").getAsInt(),
                        faceRectangle.get("top").getAsInt(), faceRectangle.get("width").getAsInt(),
                        faceRectangle.get("height").getAsInt());
                    }
                }
            }
        }
    }

    public JsonObject determineUser(JsonArray jsonArray) {
        int maxSize = 0, index = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            JsonObject faceRectangle = jsonObject.get("faceRectangle").getAsJsonObject();

            int width = faceRectangle.get("width").getAsInt();
            int height = faceRectangle.get("height").getAsInt();

            if (width + height > maxSize) {
                maxSize = width + height;
                index = i;
            }
        }
        return jsonArray.get(index).getAsJsonObject().get("faceRectangle").getAsJsonObject();
    }

    public void triggerNotification() {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        Image image = null;
        try {
            image = ImageIO.read(new File("icon.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        TrayIcon trayIcon = new TrayIcon(image, "Positive Posture");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Positive Posture");
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        trayIcon.displayMessage("Posture Alert", "Bad Posture", MessageType.NONE);
    }

}
