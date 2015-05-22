package ar.edu.itba.harryleap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JButton;

public class LeapButton extends JButton
{
	//Expanding state of the button.
	private boolean expanding = false;
	//Original button size.
	private int originalSizeX, originalSizeY;
	//Button expansion multiplier; defaults to 1.5 times as big.
	private double expansionMultiplier;
	//Allow expansion?
	public boolean canExpand = false;
	//Constructor
	LeapButton(String label, double expansionMultiplier)
	{
		//Always call the superclass methods with Swing.
		super(label);
		//Assign values.
		this.expansionMultiplier = expansionMultiplier;
	}

	public Rectangle getBigBounds() { 
		
		//Retrieve original bounds.
		Rectangle rect = getBounds();

		//Increase height and width of the button.
		rect.width = rect.width + 30;
		rect.height = rect.height + 30;

		//Reposition the button so that its central coordinates more or less remain the same.
		rect.x = rect.x - 15;
		rect.y = rect.y - 15;
		
		//Return the new button size.
		return rect;
		
	}
	
	public void expand() { 
		
		//Don't start anything if this button is already expanding.
		if (!expanding)
		{
			//Begin expanding.
			canExpand = true;
			expanding = true;
		
			//Create an anonymous inner thread, so as not to freeze the main loop.
			(new Thread()
			{
			public void run()
			{
			//Change the button's color to green for even better visual feedback.
			Color originalColor = getBackground();
			setBackground(Color.green);
			//Store the original button size.
			originalSizeX = getPreferredSize().width;
			originalSizeY = getPreferredSize().height;
			//Calculate the target size based on this LeapButton's expansion multiplier.
			int targetSizeX = (int) (originalSizeX * expansionMultiplier);
			int targetSizeY = (int) (originalSizeY * expansionMultiplier);

			//Calculate the amount to increase button size by in term	of steps.
			int stepX = (targetSizeX - originalSizeX) / 10;
			int stepY = (targetSizeY - originalSizeY) / 10;
				
			//Loop while expanding is ok and we haven't reached the target size.
			while (canExpand && getPreferredSize().width < targetSizeX)
			{
				//Increase button size.
				setPreferredSize(new Dimension(getPreferredSize().width + stepX, getPreferredSize().height + stepY));
				//Repaint (update) the button.
				revalidate();
				//Wait a moment before increasing size again.
				try { Thread.sleep(75); }
				catch (Exception e) { }
			}
			//Trigger all callbacks if the button size on loop exit		meets or exceeds our target expansion size.
			
			if (getPreferredSize().width >= targetSizeX) doClick();
			
			//Otherwise, revalidate (update) the button to make sure	renders, since doClick() would normally handle this.
			else
			revalidate();
			//Reset the size of the button to its original dimensions.
			setPreferredSize(new Dimension(originalSizeX,	originalSizeY));
			
			//Revalidate (update) the button.
			revalidate();
			
			//This button is no longer expanding.
			expanding = false;
			//Restore the original button color.
			setBackground(originalColor);
			}
			}).start();
		}
		
	}
}
