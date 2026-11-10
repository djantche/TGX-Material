package org.thunderdog.challegram.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import org.thunderdog.challegram.theme.ColorId;
import org.thunderdog.challegram.theme.Theme;
import org.thunderdog.challegram.tool.Paints;
import org.thunderdog.challegram.tool.Screen;

import me.vkryl.core.ColorUtils;

// Soft placeholder bubbles with a moving sheen, shown while the first messages of a chat are loading
public final class MessagesSkeletonView extends View {
  private static final float[] WIDTHS = {.62f, .42f, .70f, .36f, .55f, .66f, .40f, .58f, .48f, .72f};
  private static final int[] LINES = {2, 1, 3, 1, 2, 1, 1, 2, 1, 2};
  private static final boolean[] OUTGOING = {false, true, false, false, true, true, false, false, true, false};

  private final RectF rect = new RectF();
  private final Paint sheenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Matrix sheenMatrix = new Matrix();
  private final ValueAnimator sheenAnimator;
  private @Nullable View anchor;
  private float sheenProgress;
  private int sheenWidth, sheenColor;

  public MessagesSkeletonView (Context context) {
    super(context);
    sheenAnimator = ValueAnimator.ofFloat(0f, 1f);
    sheenAnimator.setDuration(1400L);
    sheenAnimator.setRepeatCount(ValueAnimator.INFINITE);
    sheenAnimator.setInterpolator(new LinearInterpolator());
    sheenAnimator.addUpdateListener(animation -> {
      sheenProgress = (float) animation.getAnimatedValue();
      invalidate();
    });
  }

  // Bubbles are laid out inside the anchor bounds, which follow the composer height
  public void setAnchor (@Nullable View anchor) {
    this.anchor = anchor;
  }

  public void setSkeletonVisible (boolean visible, boolean animated) {
    animate().cancel();
    if (visible) {
      setVisibility(View.VISIBLE);
      if (animated) {
        animate().alpha(1f).setDuration(150L).start();
      } else {
        setAlpha(1f);
      }
      if (!sheenAnimator.isStarted() && ViewCompat.isAttachedToWindow(this)) {
        sheenAnimator.start();
      }
    } else if (getVisibility() == View.VISIBLE) {
      if (animated) {
        animate().alpha(0f).setDuration(220L).withEndAction(this::hideNow).start();
      } else {
        hideNow();
      }
    }
  }

  public boolean isSkeletonVisible () {
    return getVisibility() == View.VISIBLE;
  }

  private void hideNow () {
    setVisibility(View.GONE);
    sheenAnimator.cancel();
  }

  @Override
  protected void onAttachedToWindow () {
    super.onAttachedToWindow();
    if (getVisibility() == View.VISIBLE && !sheenAnimator.isStarted()) {
      sheenAnimator.start();
    }
  }

  @Override
  protected void onDetachedFromWindow () {
    super.onDetachedFromWindow();
    sheenAnimator.cancel();
  }

  private void updateSheenShader (int width) {
    int color = ColorUtils.alphaColor(Theme.isDark() ? .07f : .45f, 0xffffffff);
    if (sheenWidth != width || sheenColor != color) {
      sheenWidth = width;
      sheenColor = color;
      sheenPaint.setShader(new LinearGradient(0, 0, width / 2f, 0, new int[] {0, color, 0}, null, Shader.TileMode.CLAMP));
    }
    sheenMatrix.setTranslate(-width / 2f + sheenProgress * width * 1.5f, 0);
    sheenPaint.getShader().setLocalMatrix(sheenMatrix);
  }

  @Override
  protected void onDraw (@NonNull Canvas c) {
    int width = getWidth();
    if (width == 0) {
      return;
    }
    int areaTop = anchor != null ? anchor.getTop() + anchor.getPaddingTop() : getPaddingTop();
    int areaBottom = anchor != null ? anchor.getBottom() - anchor.getPaddingBottom() : getHeight() - getPaddingBottom();
    updateSheenShader(width);

    final float margin = Screen.dp(12f);
    final float maxBubbleWidth = width - Screen.dp(64f);
    final float lineHeight = Screen.dp(20f), padding = Screen.dp(12f), gap = Screen.dp(8f), radius = Screen.dp(18f);
    final int incomingColor = ColorUtils.alphaColor(.75f, Theme.getColor(ColorId.bubbleIn_background));
    final int outgoingColor = ColorUtils.alphaColor(.75f, Theme.getColor(ColorId.bubbleOut_background));

    float bottom = areaBottom - Screen.dp(8f);
    for (int i = 0; bottom > areaTop; i++) {
      int index = i % WIDTHS.length;
      float height = padding * 2 + LINES[index] * lineHeight;
      float bubbleWidth = maxBubbleWidth * WIDTHS[index];
      float left = OUTGOING[index] ? width - margin - bubbleWidth : margin;
      rect.set(left, bottom - height, left + bubbleWidth, bottom);
      c.drawRoundRect(rect, radius, radius, Paints.fillingPaint(OUTGOING[index] ? outgoingColor : incomingColor));
      c.drawRoundRect(rect, radius, radius, sheenPaint);
      bottom -= height + gap;
    }
  }
}
