package cosc561.tsp.util;

import java.util.LinkedList;


/**
 * 
 * @author Welah
 *
 */
public class EventScheduler {
	
	/**
	 * True if the scheduler has been started and not yet stopped
	 */
	private volatile boolean running;
	
	/**
	 * True if the scheduler has been paused
	 */
	private volatile boolean paused;
	
	/**
	 * True if we need to skip events this cycle in order to catch up
	 */
	private boolean skipEvents;
	
	/**
	 * The list of events to be run by this scheduler
	 */
	private LinkedList<Event> eventList;
	
	private LinkedList<Event> continuousEventList;
	
	/**
	 * The number of cycles this scheduler has run without sleeping or yielding.
	 * When this exceeds the given yieldAllowance, the scheulder will yield to avoid
	 *  tying up systems resources
	 */
	private int uninterruptedCycles;
	
	/**
	 * The number of cycles the scheduler can run before yielding the thread.
	 * This value will trade off scheduler performance (high) with other process performance (low)
	 */
	private int yieldAllowance; 
	
	/**
	 * The time in ms between cycle runs
	 */
	private int period;
	
	/**
	 * The system time when we should ideally run the next cycle
	 */
	private int targetCycleStartTime;

	/**
	 * 
	 * @param targetPeriod the desired time in milliseconds between cycles
	 * @param yieldAllowance the number of consecutive cycles allowed before forcing a yield.
	 */
	public EventScheduler(int targetPeriod, int yieldAllowance) {
		this.period = targetPeriod;
		this.yieldAllowance = yieldAllowance;
		running = paused = false;
		uninterruptedCycles = 0;
		eventList = new LinkedList<>();	
		continuousEventList = new LinkedList<>();
	}
	
	private void cycle() {
		while (running) {
			skipEvents = false;
			
			//Figure out when we want the next cycle to start
			targetCycleStartTime += period;
			
			synchronize();

			if (uninterruptedCycles >= yieldAllowance) {
				Thread.yield();
				uninterruptedCycles = 0;
			}
			
			for (Event event : eventList) {
				if (event.shouldRun(paused, skipEvents)) {
					event.invoke();
				}
			}
		};
	}
	
	private void synchronize() {
		
		//Find out how far away from now that is
		int delayToSynchronize = targetCycleStartTime - realTime();
					
		//If we have time before the next cycle, sleep for that long
		if (delayToSynchronize <= 0) {
			uninterruptedCycles++;
			skipEvents = true;
			return;
		}
		
		if (continuousEventList.isEmpty() || paused) {
			sleep(delayToSynchronize);
			uninterruptedCycles = 0;
		}
		else {
			while (realTime() < targetCycleStartTime) {
				runContinuousEvents();
			}
		}
	}
	
	private void runContinuousEvents() {
		for (Event event : continuousEventList) {
			event.invoke();
		}
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.err.println("Unexpected Interruption to EventScheduler cycle");
			e.printStackTrace();
		}
	}

	public static int realTime() {
		return (int) (System.nanoTime() / 1000000L);
	}
	
	/**
	 * Starts the cycle if not already running.
	 * Notifies the console if called when already running.
	 * @throws InterruptedException 
	 */
	public void start() throws InterruptedException {
		if (running) {
			System.out.println("Start called when already running");
		}
		else {
			//Start the cycle
			targetCycleStartTime = realTime();
			running = true;
			cycle();
		}
	}
	
	/**
	 * Stops the cycle if running.
	 * Notifies the console if called when not running.
	 */
	public void end() {
		if (!running) {
			System.err.println("End called when cycle not running");
		}
		else {
			//End the cycle
			running = false;
		}
	}
	
	/**
	 * Sets the pause state of the cycle based on the passed parameter.
	 * @param pause whether or not you want the game to be paused
	 */
	public void setPaused(boolean pause) {
		paused = pause;
	}
	
	/**
	 * Pauses the cycle 
	 */
	public void togglePaused() {
		paused = !paused;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	/**
	 * Takes the desired number of cycles per second 
	 *  and returns the period in milliseconds
	 * @param cps the number of cycles per second desired
	 * @return corresponding interval in milliseconds between cycles
	 */
	public static int cyclesPerSecond(double cps) {
		return (int) (1000 / cps);
	}
	
	/**
	 * Adds an event to the list of available events to run during the cycle.
	 * By default, each event is executed in the order it was added.
	 * @param newEvent
	 */
	public void addEvent(Event newEvent) {
		eventList.add(newEvent);
		
		if (newEvent.isContinuous()) {
			continuousEventList.add(newEvent);
		}
	}
	
	private abstract class Event {
		protected Runnable event;
		protected boolean pauseable;
		protected int skipTolerance;
		protected int skips;
		protected int totalSkips;
		
		public Event(boolean pauseable, int skipTolerance, Runnable event) {
			this.pauseable = pauseable;
			this.skipTolerance = skipTolerance;
			this.event = event;
		}
		
		public int getTotalSkips() {
			return totalSkips;
		}

		protected abstract void invoke();
		
		public boolean isContinuous() {
			return false;
		}
		
		public boolean shouldRun(boolean paused, boolean skipEvents) {
			//If the game is paused and this event is allowed to pause, don't run
			if (paused && this.pauseable) {
				return false;
			}
			
			//If we don't need to skip, don't. Duh.
			if (! skipEvents) {
				return true;
			}
			else {
				//Check if we haven't skipped too many times
				if (skips < skipTolerance) {
					skips++;
					totalSkips++;
					return false;
				}
				//Either we've skipped too many times or don't need to
				else {
					skips = 0;
					return true;
				}
			}
		}
	}
	
	public class ConsecutiveEvent extends Event {
		public ConsecutiveEvent(boolean pauseable, int skipTolerance, Runnable event) {
			super(pauseable, skipTolerance, event);
		}

		@Override
		protected void invoke() {
			event.run();
		}
	}
	
	public class ConcurrentEvent extends Event {
		private volatile Thread thread;
		private final int priority;
		
		public ConcurrentEvent(boolean pauseable, int skipTolerance, int priority, Runnable event) {
			super(pauseable, skipTolerance, event);
			this.priority = priority;
		}
		
		@Override
		public boolean shouldRun(boolean paused, boolean skipEvents) {
			if (!super.shouldRun(paused, skipEvents)) {
				return false;
			}
			
			//If the previous execution hasn't finished, skip if allowed.
			if (thread != null && thread.isAlive() && (skips < skipTolerance)) {
				skips++;
				totalSkips++;
				return false;
			}
			
			return true;
		}

		@Override
		protected void invoke() {
			thread = new Thread(event);
			thread.setPriority(priority);
			thread.start();
		}
	}
	
	public class ContinuousEvent extends ConsecutiveEvent {
		public ContinuousEvent(boolean pauseable, int skipTolerance, Runnable event) {
			super(pauseable, skipTolerance, event);
		}

		public boolean isContinuous() {
			return true;
		}
	}

	public synchronized boolean isRunning() {
		return running;
	}
}
