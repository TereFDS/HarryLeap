package ar.edu.itba.harryleap;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

public class LeapaintListener extends Listener
{
	//Leapaint instance.
	public Leapaint paint;
	//Controller data frame.
	public Frame frame;
	//Leap interaction box.
	private InteractionBox normalizedBox;

	private long frameNumber;

//	boolean isSwiping;
	//Constructor.
	
	public LeapaintListener(Leapaint newPaint)
	{
		//Assign the Leapaint instance.
		paint = newPaint;
		
		//isSwiping = false;
	}
	
	//Member Function: onInit
	public void onInit(Controller controller)
	{
	System.out.println("Initialized");
	Config config = controller.config();
	System.out.println("swipe minLength: "+config.getFloat("Gesture.Swipe.MinLength"));
	System.out.println("swipe minVelocity: "+config.getFloat("Gesture.Swipe.MinVelocity"));
	}
	
	//Member Function: onConnect
	public void onConnect(Controller controller)
	{
	System.out.println("Connected");
	}
	
	//Member Function: onDisconnect
	public void onDisconnect(Controller controller)
	{
	System.out.println("Disconnected");
	}
	
	//Member Function: onExit
	public void onExit(Controller controller)
	{
	System.out.println("Exited");
	}
	
	public void onFrame(Controller controller) {

		boolean changes = false;
		frameNumber = (frameNumber + 1) % Integer.MAX_VALUE;
		
		//Get the most recent frame.
		frame = controller.frame();
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		
		//Detect if fingers are present.
		if (!frame.tools().isEmpty())
		{
			changes = true;

			//Retrieve the front-most finger.
			Tool frontMost = frame.tools().frontmost();

			//Set up its position.
			Vector position = new Vector(-1, -1, -1);
			//Retrieve an interaction box so we can normalize the Leap's coordinates to match screen size.
			normalizedBox = frame.interactionBox();
			//Retrieve normalized finger coordinates.
			position.setX(normalizedBox.normalizePoint(frontMost.tipPosition()).getX());
			position.setY(normalizedBox.normalizePoint(frontMost.tipPosition()).getY());
			position.setZ(normalizedBox.normalizePoint(frontMost.tipPosition()).getZ());
			
			//Scale coordinates to the resolution of the painter window.
			position.setX(position.getX() * paint.getBounds().width);
			position.setY(position.getY() * paint.getBounds().height);
			
			//Flip Y axis so that up is actually up, and not down.
			position.setY(position.getY() * -1);
			position.setY(position.getY() + paint.getBounds().height);
			
			//Pass the X/Y coordinates to the painter.
			paint.prevX = paint.x;
			paint.prevY = paint.y;
			paint.x = (int) position.getX();
			paint.y = (int) position.getY();
			paint.z = position.getZ();

			//Check if the user is hovering over any buttons.
			if (paint.button1.getBigBounds().contains((int) position.getX(),(int) position.getY()))
				paint.button1.expand();
			else paint.button1.canExpand = false;

			if (paint.button2.getBigBounds().contains((int) position.getX(),(int) position.getY()))
				paint.button2.expand();
			else paint.button2.canExpand = false;

			if (paint.button3.getBigBounds().contains((int) position.getX(),(int) position.getY()))
				paint.button3.expand();
			else paint.button3.canExpand = false;

			if (paint.button4.getBigBounds().contains((int) position.getX(),(int) position.getY()))
				paint.button4.expand();
			else paint.button4.canExpand = false;
		}

		boolean hasSwipe = false;
		if (!frame.gestures().isEmpty())
		{
			changes = true;
			//Loop over all of the gestures detected by the Leap.
			for (Gesture gesture : frame.gestures())
			{
				//If it's a circle gesture, print data for it.

				if(gesture.type() == Gesture.Type.TYPE_SWIPE) {
					hasSwipe = true;
					
					SwipeGesture swipe = new SwipeGesture(gesture);
								
					boolean forward= true;
					
					if (gesture.state()==State.STATE_STOP) {
						float direction= swipe.startPosition().getX()-swipe.position().getX();
						System.out.println("diference: "+direction);
						System.out.println(swipe.startPosition());
						System.out.println(swipe.position());
						System.out.println("Changing image on frame: " + frameNumber);
						paint.backgroundImage.changeImage(true);
						//isSwiping = true;
					}
				}

			}
		}
//		isSwiping = hasSwipe;

		if (changes) {
			//Tell the painter to update.
			paint.paintPanel.repaint();
		}
	}

}
