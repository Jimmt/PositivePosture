import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class CameraControl implements Runnable {
	public Webcam makeWebcam() {
		Webcam webcam = Webcam.getDefault();
		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
		} else {
			System.out.println("No webcam detected");
		}
		return webcam;
	}
	public void takePics(Webcam web){
		web.open();
		BufferedImage img;
		int imgNum = 0;
		boolean isRunning = true;
		try {
			while(isRunning){
				img = web.getImage();
				ImageIO.write(img,"PNG", new File("img-" + imgNum + ".png"));
				imgNum++;
				web.close();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				web.open();
			}
		} catch (IOException e) {
			e.printStackTrace();
			isRunning = false;
		}		
		}
	
	public void run(){
		takePics(makeWebcam());
	}
	
	public static void main(String[] args){
		(new Thread(new CameraControl())).start();
	}
	
	
	
}
