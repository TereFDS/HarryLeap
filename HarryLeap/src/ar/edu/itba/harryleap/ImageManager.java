package ar.edu.itba.harryleap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageManager {
	static public int IMAGES_NUMBER = 3;
	static String IMAGES_PATH = "resources/background_";
	static String IMAGE_EXTENSION = ".jpg";
	BufferedImage image = null;
	int index_image = 0;
	
	public ImageManager() {
		
		try {
			image = ImageIO.read(new File(IMAGES_PATH+ (index_image+1) +IMAGE_EXTENSION));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void changeImage(){
		index_image++;
		index_image= (index_image%IMAGES_NUMBER)+1;
		try {
			image = ImageIO.read(new File(IMAGES_PATH+ index_image +IMAGE_EXTENSION));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
}
