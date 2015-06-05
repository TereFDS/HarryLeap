package ar.edu.itba.harryleap;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

public class LeapHarryListener extends Listener {
	// LeapHarry instance.
	public LeapHarry paint;
	// Controller data frame.
	public Frame frame;
	// Leap interaction box.
	private InteractionBox normalizedBox;

	private long frameNumber;

	boolean isSwiping;
	boolean isLifting;

	// Constructor.

	public LeapHarryListener(LeapHarry newPaint) {
		// Assign the LeapHarry instance.
		paint = newPaint;
		isSwiping = false;
		isLifting = false;
	}

	// Member Function: onInit
	public void onInit(Controller controller) {
		System.out.println("Initialized");
		Config config = controller.config();
		System.out.println("swipe minLength: "
				+ config.getFloat("Gesture.Swipe.MinLength"));
		System.out.println("swipe minVelocity: "
				+ config.getFloat("Gesture.Swipe.MinVelocity"));
	}

	// Member Function: onConnect
	public void onConnect(Controller controller) {
		System.out.println("Connected");
	}

	// Member Function: onDisconnect
	public void onDisconnect(Controller controller) {
		System.out.println("Disconnected");
	}

	// Member Function: onExit
	public void onExit(Controller controller) {
		System.out.println("Exited");
	}

	public void onFrame(Controller controller) {

		boolean changes = false;
		frameNumber = (frameNumber + 1) % Integer.MAX_VALUE;

		// Get the most recent frame.
		frame = controller.frame();
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);

		// Detect if fingers are present.
		if (!frame.tools().isEmpty()) {
			changes = true;

			// Retrieve the front-most finger.
			Tool frontMost = frame.tools().frontmost();

			if (isLifting) {
				//lifting_movement(frontMost.tipPosition());
			} else {
				paint.images.changeFeatherPositionDown();
			}
		} else {
			if (paint.images.changeFeatherPositionDown()) {
				paint.begin_Spell = false;
				changes = true;
			}
		}

		boolean hasSwipe = false;
		if (!frame.gestures().isEmpty()) {
			changes = true;
			// Loop over all of the gestures detected by the Leap.
			for (Gesture gesture : frame.gestures()) {
				// If it's a circle gesture, print data for it.
				if (gesture.type() == Gesture.Type.TYPE_CIRCLE) {
					CircleGesture circleGesture = new CircleGesture(gesture);
					switch (circleGesture.state()) {
					case STATE_START:
						isLifting = true;
						paint.begin_Spell = true;
						paint.sounds.playSpellSound();
						lifting_movement(circleGesture.center());
						paint.images.changeFeatherPositionUp(paint.x);
						break;
					case STATE_UPDATE:

						// Must update image position here!
						lifting_movement(circleGesture.center());
						paint.images.changeFeatherPositionUp(paint.x);
						break;
					case STATE_STOP:
						isLifting = false;
						paint.begin_Spell = false;
					default:
						paint.begin_Spell = false;
						break;
					}

					System.out.println("Circle gesture on frame: "
							+ frameNumber);
				}
				if (gesture.type() == Gesture.Type.TYPE_SWIPE) {
					hasSwipe = true;

					SwipeGesture swipe = new SwipeGesture(gesture);

					boolean forward = true;

					if (gesture.state() == State.STATE_STOP) {
						float direction = swipe.startPosition().getX()
								- swipe.position().getX();
						System.out.println("diference: " + direction);
						System.out.println(swipe.startPosition());
						System.out.println(swipe.position());
						System.out.println("Changing image on frame: "
								+ frameNumber);
						paint.images.changeImage(true);
						// isSwiping = true;
					}
				}

			}
		}
		// isSwiping = hasSwipe;

		if (changes) {
			// Tell the painter to update.
			paint.paintPanel.repaint();
		}

	}

	private void lifting_movement(Vector position) {

		// Retrieve an interaction box so we can normalize the Leap's
		// coordinates to match screen size.
		normalizedBox = frame.interactionBox();
		// Retrieve normalized finger coordinates.
		position.setX(normalizedBox.normalizePoint(position).getX());
		position.setY(normalizedBox.normalizePoint(position).getY());
		position.setZ(normalizedBox.normalizePoint(position).getZ());

		// Scale coordinates to the resolution of the painter window.
		float x_position = Math.min(position.getX(), (float)0.82);
		position.setX(x_position * paint.getBounds().width);
		position.setY(position.getY() * paint.getBounds().height);

		// Flip Y axis so that up is actually up, and not down.
		position.setY(position.getY() * -1);
		position.setY(position.getY() + paint.getBounds().height);

		// Pass the X/Y coordinates to the painter.
		paint.prevX = paint.x;
		paint.prevY = paint.y;
		paint.x = (int) position.getX();
		paint.y = (int) position.getY();
		paint.z = position.getZ();
	}
}
