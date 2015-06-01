package ar.edu.itba.harryleap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageManager {
	static public int IMAGES_NUMBER = 3;
	static String BG_IMAGES_PATH = "resources/background_";
	static String FT_IMAGE_PATH = "resources/feather";
	static String BG_IMAGE_EXTENSION = ".jpg";
	static String FT_IMAGE_EXTENSION = ".png";
	BufferedImage backgroundImage = null;
	BufferedImage featherImage = null;
	int bg_index_image = 0;
	int fPosX = 0;
	int fPosY = 0;

	public ImageManager() {

		try {
			backgroundImage = ImageIO.read(new File(BG_IMAGES_PATH
					+ (bg_index_image + 1) + BG_IMAGE_EXTENSION));
			featherImage = ImageIO.read(new File(FT_IMAGE_PATH
					+ FT_IMAGE_EXTENSION));
			fPosX = 300;
			fPosY = 500;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BufferedImage getBackgroundImage() {
		return backgroundImage;
	}
	
	public BufferedImage getFeatherImage() {
		return featherImage;
	}
	
	public int getFeatherXPos() {
		return fPosX;
	}
	
	public int getFeatherYPos() {
		return fPosY;
	}

	public void changeImage(boolean stepForward) {
		bg_index_image++;
		bg_index_image = (bg_index_image % IMAGES_NUMBER) + 1;
		try {
			backgroundImage = ImageIO.read(new File(BG_IMAGES_PATH
					+ bg_index_image + BG_IMAGE_EXTENSION));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void changeFeatherPositionUp(int x) {
		System.out.println("fPosY: " + fPosY);
		if(fPosY > 10)
			fPosY -= 2;
		fPosX = x;
		System.out.println("Changed feather position: " + fPosY);
	}
	
	public boolean changeFeatherPositionDown() {
		if(fPosY < 500){
			fPosY += 2;
			return true;
		}
		return false;	
	}

}
