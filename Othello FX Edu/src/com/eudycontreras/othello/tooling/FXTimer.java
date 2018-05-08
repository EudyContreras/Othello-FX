package com.eudycontreras.othello.tooling;


import java.util.function.Consumer;
import java.util.function.Predicate;

import com.eudycontreras.othello.tooling.FXRepeater.IRepeater;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.util.Duration;


public class FXTimer<T> implements ITimer {

	public static void runAfter(Duration delay, Runnable script){
		Thread thread = new Thread(()->{
			waitTime(delay.toMillis());
			Platform.runLater(script);
		});
		
		thread.setDaemon(true);
		thread.start();
	}

	private static void waitTime(double millis){
		try {
			Thread.sleep((long) millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static<V> TimerInstance<V> getTimeCapsule(){
		return new TimeCapsule<V>();
	}
    /**
     * Prepares a (stopped) timer that lasts for {@code delay} and whose action runs when timer <em>ends</em>.
     */
    private static ITimer create(Duration delay, TimerAction action) {
        return new FXTimer<>(delay, delay, action, 1);
    }

    /**
     * Equivalent to {@code create(delay, action).restart()}.
     */
    public static ITimer runLater(Duration delay, TimerAction action) {
        ITimer timer = create(delay, action);
        timer.restart();
        return timer;
    }

    /**
     * Prepares a (stopped) timer that lasts for {@code interval} and that executes the given action periodically
     * when the timer <em>ends</em>.
     */
    private static ITimer createPeriodic(Duration interval, TimerAction action) {
        return new FXTimer<>(interval, interval, action, Animation.INDEFINITE);
    }

    /**
     * Prepares a (stopped) timer that lasts for {@code interval} and that executes the given action periodically
     * when the timer <em>ends</em>.
     */
    private static ITimer createPeriodic(Duration interval,IRepeater repeats, TimerAction action) {
        return new FXTimer<>(interval, interval,repeats, action, Animation.INDEFINITE);
    }

    /**
     * Prepares a (stopped) timer that lasts for {@code interval} and that executes the given action periodically
     * when the timer <em>ends</em>.
     */
    private static<T> ITimer createPeriodic(Duration interval, TimerAction action, T type, Predicate<T> predicate, Consumer<T> consumer) {
        return new FXTimer<>(interval, interval, null, action, Animation.INDEFINITE, type, predicate, consumer);
    }

    /**
     * Prepares a (stopped) timer that lasts for {@code interval} and that executes the given action periodically
     * when the timer <em>ends</em>.
     */
    private static<T> ITimer createPeriodic(Duration interval, IRepeater repeats, TimerAction action, T type, Predicate<T> predicate, Consumer<T> consumer) {
        return new FXTimer<>(interval, interval, repeats, action, Animation.INDEFINITE, type, predicate, consumer);
    }

    /**
     * Equivalent to {@code createPeriodic(interval, action).restart()}.
     */
    public static ITimer runPeriodically(Duration interval, TimerAction action) {
        ITimer timer = createPeriodic(interval, action);
        timer.restart();
        return timer;
    }

    /**
     * Equivalent to {@code createPeriodic(interval, action).restart()}.
     */
    public static ITimer runPeriodically(Duration interval, IRepeater repeats, TimerAction action) {
        ITimer timer = createPeriodic(interval,repeats,action);
        timer.restart();
        return timer;
    }

    /**
     * Equivalent to {@code createPeriodic(interval, action).restart()}.
     */
    public static<T> ITimer runAsLong(Duration interval, IRepeater repeats, TimerAction action, T type, Predicate<T> condition, Consumer<T> consumer) {
        ITimer timer = createPeriodic(interval, repeats, action, type, condition, consumer);
        timer.restart();
        return timer;
    }

    /**
     * Equivalent to {@code createPeriodic(interval, action).restart()}.
     */
    public static<T> ITimer runAsLong(Duration interval, TimerAction action, T type, Predicate<T> condition, Consumer<T> consumer) {
        ITimer timer = createPeriodic(interval, action, type, condition, consumer);
        timer.restart();
        return timer;
    }

    /**
     * Prepares a (stopped) timer that lasts for {@code interval} and that executes the given action periodically
     * when the timer <em>starts</em>.
     */
    private static ITimer createPeriodic_zero(Duration interval, TimerAction action) {
        return new FXTimer<>(Duration.ZERO, interval, action, Animation.INDEFINITE);
    }

    /**
     * Equivalent to {@code createPeriodic0(interval, action).restart()}.
     */
    public static ITimer runPeriodically_zero(Duration interval, TimerAction action) {
        ITimer timer = createPeriodic_zero(interval, action);
        timer.restart();
        return timer;
    }


    private final Consumer<T> consumer;
    private final Predicate<T> predicate;
    private final Duration actionTime;
    private final Timeline timeline;
    private final Capsule capsule;
    private final TimerAction action;
    private final IRepeater repeater;
    private volatile int iterations;
    private final T type;
    private long seq = 0;

    private FXTimer(Duration actionTime, Duration period, TimerAction action, int cycles) {
        this(actionTime,period,null,action,cycles,null,null,null);
    }

    private FXTimer(Duration actionTime, Duration period, IRepeater repeats, TimerAction action, int cycles) {
        this(actionTime,period,repeats,action,cycles,null,null,null);
    }

    private FXTimer(Duration actionTime, Duration period, IRepeater repeats, TimerAction action, int cycles, T type, Predicate<T> predicate, Consumer<T> consumer) {
    	 this.actionTime = actionTime;
    	 this.predicate = predicate;
    	 this.repeater = repeats;
    	 this.consumer = consumer;
         this.action = action;
    	 this.type = type;

    	 this.timeline = new Timeline();
    	 this.capsule = new Capsule(timeline);

         this.timeline.getKeyFrames().add(new KeyFrame(this.actionTime));

         if (period != actionTime) {
             this.timeline.getKeyFrames().add(new KeyFrame(Duration.millis(period.toMillis())));
         }

         this.timeline.setCycleCount(cycles);

	}


	@Override
    public void restart() {
        stop();
        long expected = seq;
        timeline.getKeyFrames().set(0, new KeyFrame(actionTime, ae -> {
			if (repeater != null) {

				FXRepeater.repeat(repeater.getRepeats(), (repetitions)->{
					if (isRunning() && !capsule.stopped()) {
						iterations++;
						if (seq == expected) {
							action.run(iterations,capsule);
						}
						if (predicate != null) {
							if (predicate.test(type)) {
							} else {
								stop();
								if (consumer != null) {
									consumer.accept(type);
								}
							}
						}
					}
				});
			} else{
				if (isRunning() && !capsule.stopped()) {
					iterations++;
					if (seq == expected) {
						action.run(iterations,capsule);
					}
					if (predicate != null) {
						if (predicate.test(type)) {
						} else {
							stop();
							if (consumer != null) {
								consumer.accept(type);
							}
						}
					}
				}
			}
        }));

        timeline.play();
    }

    @Override
    public void stop() {
		timeline.stop();
		++seq;
	}

    @Override
    public boolean isRunning(){
    	return timeline.getStatus() == Status.RUNNING;
    }

    public interface TimerAction{
    	void run(int actions, Capsule capsule);
    }

    public static class Capsule{

    	private boolean stop;
    	private SimpleIntegerProperty maxValue;
    	private SimpleDoubleProperty progress;
    	private Timeline timeline;

    	public Capsule(Timeline timeline){
    		this.timeline = timeline;
    		this.progress = new SimpleDoubleProperty();
    	}

		public void updateProgress(int current, int max) {
			if(maxValue == null){
				maxValue = new SimpleIntegerProperty();
				maxValue.set(max);
			}

			if(maxValue!=null)
			progress.set(1.0*current / maxValue.get());
		}

		public ObservableDoubleValue getProgressProperty(){
			return progress;
		}

    	public void stop(){
    		this.stop = true;
    	}

    	public Timeline getTimeline(){
    		return timeline;
    	}

    	public boolean stopped(){
    		return stop;
    	}
    }

    public interface TimerInstance<V>{
    	void start();
    	void stop();
    	void reset();
    	void setAction(TimerAction action);
    	void setInterval(Duration duration);
    	void setEndCondition(V type, Predicate<V> predicate);
    	void setEndAction(Consumer<V> consumer);
    	void setRepeatRate(IRepeater iterations);
    	boolean isRunning();
    	Duration getDuration();
    }

    private static class TimeCapsule<V> implements TimerInstance<V>{

		private Consumer<V> consumer;
		private Predicate<V> predicate;
		private Duration duration;
		private Timeline timeline;
		private Capsule capsule;
		private TimerAction action;
		private IRepeater repeater;
		private volatile int iterations;
        private volatile int repeats;
		private long seq = 0;
		private V type;

    	public TimeCapsule(){
			this.timeline = new Timeline();
			this.capsule = new Capsule(timeline);
			this.timeline.setCycleCount(Animation.INDEFINITE);
    	}

		@Override
		public void start() {
			if(action==null){
				return;
			}
			 stop();

		        long expected = seq;

		        timeline.getKeyFrames().set(0, new KeyFrame(duration, ae -> {
					if (repeater != null) {

						FXRepeater.repeat(repeats, (repetitions)->{

							if (isRunning() && !capsule.stopped()) {
								iterations++;
								if (seq == expected) {
									action.run(iterations,capsule);
								}
								if (predicate != null) {
									if (predicate.test(type)) {
									} else {
										stop();
										if (consumer != null) {
											consumer.accept(type);
										}
									}
								}
							}
						});
					} else{
						if (isRunning() && !capsule.stopped()) {
							iterations++;
							if (seq == expected) {

								action.run(iterations,capsule);
							}
							if (predicate != null) {
								if (predicate.test(type)) {
								} else {
									stop();
									if (consumer != null) {
										consumer.accept(type);
									}
								}
							}
						}
					}
		        }));
		        timeline.play();
		}

		@Override
		public void stop() {
			++seq;
			timeline.stop();
			reset();
		}

		@Override
		public void reset() {
    		seq = 0;
    		iterations = 0;
    		repeats = repeater.getRepeats();
    		timeline.getKeyFrames().clear();
    		timeline.getKeyFrames().add(new KeyFrame(duration));
    		timeline.setCycleCount(Animation.INDEFINITE);
		}

		@Override
		public void setAction(TimerAction action) {
			this.action = action;
		}

		@Override
		public void setInterval(Duration duration) {
			this.duration = duration;
			this.timeline.getKeyFrames().add(new KeyFrame(duration));
		}

		@Override
		public void setEndCondition(V type, Predicate<V> predicate) {
			this.type = type;
			this.predicate = predicate;
		}

		@Override
		public void setEndAction(Consumer<V> consumer) {
			this.consumer =consumer;
		}

		@Override
		public void setRepeatRate(IRepeater iterations) {
			this.repeater = iterations;
			this.repeats = iterations.getRepeats();
		}

		@Override
		public Duration getDuration() {
			return duration;
		}

		@Override
		public boolean isRunning() {
			return timeline.getStatus() == Status.RUNNING;
		}
    }
}

/**
 * Timer represents a delayed action. This means that every timer has an
 * associated action and an associated delay. Action and delay are specified
 * on timer creation.
 *
 * <p>Every timer also has an associated thread (such as JavaFX application
 * thread or a single-thread executor's thread). Timer may only be accessed
 * from its associated thread. Timer's action is executed on its associated
 * thread, too. This design allows to implement guarantees provided by
 * {@link #stop()}.
 */
interface ITimer {
    /**
     * Schedules the associated action to be executed after the associated
     * delay. If the action is already scheduled but hasn't been executed yet,
     * the timeout is reset, so that the action won't be executed before the
     * full delay from now.
     */
    void restart();

    void stop();

    boolean isRunning();
}
