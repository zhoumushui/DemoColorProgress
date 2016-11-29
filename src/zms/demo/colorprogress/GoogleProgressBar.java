package zms.demo.colorprogress;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

public class GoogleProgressBar extends ProgressBar {

	private static final int INTERPOLATOR_ACCELERATE = 0;
	private static final int INTERPOLATOR_LINEAR = 1;
	private static final int INTERPOLATOR_ACCELERATEDECELERATE = 2;
	private static final int INTERPOLATOR_DECELERATE = 3;

	public GoogleProgressBar(Context context) {
		this(context, null);
	}

	public GoogleProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.googleProgressStyle);
	}

	public GoogleProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		if (isInEditMode()) {
			setIndeterminateDrawable(new GoogleProgressDrawable.Builder(
					context, true).build());
			return;
		}

		Resources res = context.getResources();
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.GoogleStyleProgressBar, defStyle, 0);

		final int color = a.getColor(R.styleable.GoogleStyleProgressBar_color,
				res.getColor(R.color.default_color));
		final int sectionsCount = a.getInteger(
				R.styleable.GoogleStyleProgressBar_sections_count,
				res.getInteger(R.integer.default_sections_count));
		final int separatorLength = a
				.getDimensionPixelSize(
						R.styleable.GoogleStyleProgressBar_stroke_separator_length,
						res.getDimensionPixelSize(R.dimen.default_stroke_separator_length));
		final float strokeWidth = a.getDimension(
				R.styleable.GoogleStyleProgressBar_stroke_width,
				res.getDimension(R.dimen.default_stroke_width));
		final float speed = a.getFloat(
				R.styleable.GoogleStyleProgressBar_speed,
				Float.parseFloat(res.getString(R.string.default_speed)));
		final float speedProgressiveStart = a.getFloat(
				R.styleable.GoogleStyleProgressBar_progressiveStart_speed,
				speed);
		final float speedProgressiveStop = a
				.getFloat(
						R.styleable.GoogleStyleProgressBar_progressiveStop_speed,
						speed);
		final int iInterpolator = a.getInteger(
				R.styleable.GoogleStyleProgressBar_interpolator, -1);
		final boolean reversed = a.getBoolean(
				R.styleable.GoogleStyleProgressBar_reversed,
				res.getBoolean(R.bool.default_reversed));
		final boolean mirrorMode = a.getBoolean(
				R.styleable.GoogleStyleProgressBar_mirror_mode,
				res.getBoolean(R.bool.default_mirror_mode));
		final int colorsId = a.getResourceId(
				R.styleable.GoogleStyleProgressBar_colors, 0);
		final boolean progressiveStartActivated = a.getBoolean(
				R.styleable.GoogleStyleProgressBar_progressiveStart_activated,
				res.getBoolean(R.bool.default_progressiveStart_activated));
		final Drawable backgroundDrawable = a
				.getDrawable(R.styleable.GoogleStyleProgressBar_background);
		final boolean generateBackgroundWithColors = a
				.getBoolean(
						R.styleable.GoogleStyleProgressBar_generate_background_with_colors,
						false);
		final boolean gradients = a.getBoolean(
				R.styleable.GoogleStyleProgressBar_gradients, false);
		a.recycle();

		// interpolator
		Interpolator interpolator = null;
		if (iInterpolator == -1) {
			interpolator = getInterpolator();
		}
		if (interpolator == null) {
			switch (iInterpolator) {
			case INTERPOLATOR_ACCELERATEDECELERATE:
				interpolator = new AccelerateDecelerateInterpolator();
				break;
			case INTERPOLATOR_DECELERATE:
				interpolator = new DecelerateInterpolator();
				break;
			case INTERPOLATOR_LINEAR:
				interpolator = new LinearInterpolator();
				break;
			case INTERPOLATOR_ACCELERATE:
			default:
				interpolator = new AccelerateInterpolator();
			}
		}

		int[] colors = null;
		// colors
		if (colorsId != 0) {
			colors = res.getIntArray(colorsId);
		}

		GoogleProgressDrawable.Builder builder = new GoogleProgressDrawable.Builder(
				context).speed(speed)
				.progressiveStartSpeed(speedProgressiveStart)
				.progressiveStopSpeed(speedProgressiveStop)
				.interpolator(interpolator).sectionsCount(sectionsCount)
				.separatorLength(separatorLength).strokeWidth(strokeWidth)
				.reversed(reversed).mirrorMode(mirrorMode)
				.progressiveStart(progressiveStartActivated)
				.gradients(gradients);

		if (backgroundDrawable != null) {
			builder.backgroundDrawable(backgroundDrawable);
		}

		if (generateBackgroundWithColors) {
			builder.generateBackgroundUsingColors();
		}

		if (colors != null && colors.length > 0)
			builder.colors(colors);
		else
			builder.color(color);

		GoogleProgressDrawable d = builder.build();
		setIndeterminateDrawable(d);
	}

	public void applyStyle(int styleResId) {
		TypedArray a = getContext().obtainStyledAttributes(null,
				R.styleable.GoogleStyleProgressBar, 0, styleResId);

		if (a.hasValue(R.styleable.GoogleStyleProgressBar_color)) {
			setSmoothProgressDrawableColor(a.getColor(
					R.styleable.GoogleStyleProgressBar_color, 0));
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_colors)) {
			int colorsId = a.getResourceId(
					R.styleable.GoogleStyleProgressBar_colors, 0);
			if (colorsId != 0) {
				int[] colors = getResources().getIntArray(colorsId);
				if (colors != null && colors.length > 0)
					setSmoothProgressDrawableColors(colors);
			}
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_sections_count)) {
			setSmoothProgressDrawableSectionsCount(a.getInteger(
					R.styleable.GoogleStyleProgressBar_sections_count, 0));
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_stroke_separator_length)) {
			setSmoothProgressDrawableSeparatorLength(a.getDimensionPixelSize(
					R.styleable.GoogleStyleProgressBar_stroke_separator_length,
					0));
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_stroke_width)) {
			setSmoothProgressDrawableStrokeWidth(a.getDimension(
					R.styleable.GoogleStyleProgressBar_stroke_width, 0));
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_speed)) {
			setSmoothProgressDrawableSpeed(a.getFloat(
					R.styleable.GoogleStyleProgressBar_speed, 0));
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_progressiveStart_speed)) {
			setSmoothProgressDrawableProgressiveStartSpeed(a.getFloat(
					R.styleable.GoogleStyleProgressBar_progressiveStart_speed,
					0));
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_progressiveStop_speed)) {
			setSmoothProgressDrawableProgressiveStopSpeed(a
					.getFloat(
							R.styleable.GoogleStyleProgressBar_progressiveStop_speed,
							0));
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_reversed)) {
			setSmoothProgressDrawableReversed(a.getBoolean(
					R.styleable.GoogleStyleProgressBar_reversed, false));
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_mirror_mode)) {
			setSmoothProgressDrawableMirrorMode(a.getBoolean(
					R.styleable.GoogleStyleProgressBar_mirror_mode, false));
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_progressiveStart_activated)) {
			setProgressiveStartActivated(a
					.getBoolean(
							R.styleable.GoogleStyleProgressBar_progressiveStart_activated,
							false));
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_progressiveStart_activated)) {
			setProgressiveStartActivated(a
					.getBoolean(
							R.styleable.GoogleStyleProgressBar_progressiveStart_activated,
							false));
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_gradients)) {
			setSmoothProgressDrawableUseGradients(a.getBoolean(
					R.styleable.GoogleStyleProgressBar_gradients, false));
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_generate_background_with_colors)) {
			if (a.getBoolean(
					R.styleable.GoogleStyleProgressBar_generate_background_with_colors,
					false)) {
				setSmoothProgressDrawableBackgroundDrawable(GoogleProgressBarUtils
						.generateDrawableWithColors(
								checkIndeterminateDrawable().getColors(),
								checkIndeterminateDrawable().getStrokeWidth()));
			}
		}
		if (a.hasValue(R.styleable.GoogleStyleProgressBar_interpolator)) {
			int iInterpolator = a.getInteger(
					R.styleable.GoogleStyleProgressBar_interpolator, -1);
			Interpolator interpolator;
			switch (iInterpolator) {
			case INTERPOLATOR_ACCELERATEDECELERATE:
				interpolator = new AccelerateDecelerateInterpolator();
				break;
			case INTERPOLATOR_DECELERATE:
				interpolator = new DecelerateInterpolator();
				break;
			case INTERPOLATOR_LINEAR:
				interpolator = new LinearInterpolator();
				break;
			case INTERPOLATOR_ACCELERATE:
				interpolator = new AccelerateInterpolator();
				break;
			default:
				interpolator = null;
			}
			if (interpolator != null) {
				setInterpolator(interpolator);
			}
		}
		a.recycle();
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isIndeterminate()
				&& getIndeterminateDrawable() instanceof GoogleProgressDrawable
				&& !((GoogleProgressDrawable) getIndeterminateDrawable())
						.isRunning()) {
			getIndeterminateDrawable().draw(canvas);
		}
	}

	private GoogleProgressDrawable checkIndeterminateDrawable() {
		Drawable ret = getIndeterminateDrawable();
		if (ret == null || !(ret instanceof GoogleProgressDrawable))
			throw new RuntimeException(
					"The drawable is not a SmoothProgressDrawable");
		return (GoogleProgressDrawable) ret;
	}

	@Override
	public void setInterpolator(Interpolator interpolator) {
		super.setInterpolator(interpolator);
		Drawable ret = getIndeterminateDrawable();
		if (ret != null && (ret instanceof GoogleProgressDrawable))
			((GoogleProgressDrawable) ret).setInterpolator(interpolator);
	}

	public void setSmoothProgressDrawableInterpolator(Interpolator interpolator) {
		checkIndeterminateDrawable().setInterpolator(interpolator);
	}

	public void setSmoothProgressDrawableColors(int[] colors) {
		checkIndeterminateDrawable().setColors(colors);
	}

	public void setSmoothProgressDrawableColor(int color) {
		checkIndeterminateDrawable().setColor(color);
	}

	public void setSmoothProgressDrawableSpeed(float speed) {
		checkIndeterminateDrawable().setSpeed(speed);
	}

	public void setSmoothProgressDrawableProgressiveStartSpeed(float speed) {
		checkIndeterminateDrawable().setProgressiveStartSpeed(speed);
	}

	public void setSmoothProgressDrawableProgressiveStopSpeed(float speed) {
		checkIndeterminateDrawable().setProgressiveStopSpeed(speed);
	}

	public void setSmoothProgressDrawableSectionsCount(int sectionsCount) {
		checkIndeterminateDrawable().setSectionsCount(sectionsCount);
	}

	public void setSmoothProgressDrawableSeparatorLength(int separatorLength) {
		checkIndeterminateDrawable().setSeparatorLength(separatorLength);
	}

	public void setSmoothProgressDrawableStrokeWidth(float strokeWidth) {
		checkIndeterminateDrawable().setStrokeWidth(strokeWidth);
	}

	public void setSmoothProgressDrawableReversed(boolean reversed) {
		checkIndeterminateDrawable().setReversed(reversed);
	}

	public void setSmoothProgressDrawableMirrorMode(boolean mirrorMode) {
		checkIndeterminateDrawable().setMirrorMode(mirrorMode);
	}

	public void setProgressiveStartActivated(boolean progressiveStartActivated) {
		checkIndeterminateDrawable().setProgressiveStartActivated(
				progressiveStartActivated);
	}

	public void setSmoothProgressDrawableCallbacks(
			GoogleProgressDrawable.Callbacks listener) {
		checkIndeterminateDrawable().setCallbacks(listener);
	}

	public void setSmoothProgressDrawableBackgroundDrawable(Drawable drawable) {
		checkIndeterminateDrawable().setBackgroundDrawable(drawable);
	}

	public void setSmoothProgressDrawableUseGradients(boolean useGradients) {
		checkIndeterminateDrawable().setUseGradients(useGradients);
	}

	public void progressiveStart() {
		checkIndeterminateDrawable().progressiveStart();
	}

	public void progressiveStop() {
		checkIndeterminateDrawable().progressiveStop();
	}
}
