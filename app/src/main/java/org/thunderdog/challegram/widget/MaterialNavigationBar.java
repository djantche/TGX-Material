package org.thunderdog.challegram.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.thunderdog.challegram.R;
import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.support.RippleSupport;
import org.thunderdog.challegram.theme.ColorId;
import org.thunderdog.challegram.theme.Theme;
import org.thunderdog.challegram.tool.Fonts;
import org.thunderdog.challegram.tool.Screen;

public final class MaterialNavigationBar extends LinearLayout {
  public static final int HEIGHT = 80;
  private final int[] labels = {R.string.Chats, R.string.Calls, R.string.Contacts, R.string.Settings};
  private final int[] icons = {R.drawable.outline_forum_24, R.drawable.baseline_call_24, R.drawable.baseline_person_24, R.drawable.baseline_settings_24};
  private final ImageView[] iconViews = new ImageView[4];
  private final TextView[] labelViews = new TextView[4];
  private final GradientDrawable[] indicators = new GradientDrawable[4];
  private int selected;

  public interface Listener {
    void onDestinationSelected (int destination);
  }

  public MaterialNavigationBar (Context context, Listener listener) {
    super(context);
    setOrientation(HORIZONTAL);
    setGravity(Gravity.TOP);
    setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
    for (int index = 0; index < labels.length; index++) {
      final int destination = index;
      LinearLayout item = new LinearLayout(context);
      item.setOrientation(VERTICAL);
      item.setGravity(Gravity.CENTER);
      item.setClickable(true);
      item.setFocusable(true);
      item.setOnClickListener(view -> listener.onDestinationSelected(destination));
      item.setAccessibilityDelegate(new View.AccessibilityDelegate() {
        @Override
        public void onInitializeAccessibilityNodeInfo (View host, AccessibilityNodeInfo info) {
          super.onInitializeAccessibilityNodeInfo(host, info);
          info.setClassName("android.widget.Button");
        }
      });
      ImageView icon = new ImageView(context);
      icon.setImageResource(icons[index]);
      icon.setScaleType(ImageView.ScaleType.CENTER);
      icon.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
      GradientDrawable indicator = new GradientDrawable();
      indicator.setCornerRadius(Screen.dp(24f));
      icon.setBackground(indicator);
      indicators[index] = indicator;
      item.addView(icon, new LinearLayout.LayoutParams(Screen.dp(64f), Screen.dp(32f)));
      TextView label = new TextView(context);
      label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
      label.setGravity(Gravity.CENTER);
      label.setSingleLine(true);
      label.setEllipsize(TextUtils.TruncateAt.END);
      label.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
      LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
      labelParams.topMargin = Screen.dp(4f);
      item.addView(label, labelParams);
      iconViews[index] = icon;
      labelViews[index] = label;
      addView(item, new LinearLayout.LayoutParams(0, Screen.dp(HEIGHT), 1f));
      RippleSupport.setTransparentSelector(item, 24f, null);
    }
    refresh();
  }

  public void setSelectedDestination (int destination) {
    if (selected != destination) {
      selected = destination;
      refresh();
    }
  }

  public int getSelectedDestination () {
    return selected;
  }

  public void refresh () {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      setLayoutDirection(Lang.rtl() ? LAYOUT_DIRECTION_RTL : LAYOUT_DIRECTION_LTR);
    }
    for (int index = 0; index < labels.length; index++) {
      boolean active = index == selected;
      View item = getChildAt(index);
      String label = Lang.getString(labels[index]);
      item.setSelected(active);
      item.setContentDescription(label);
      labelViews[index].setText(label);
      labelViews[index].setTypeface(active ? Fonts.getRobotoMedium() : Fonts.getRobotoRegular());
    }
    updateColors();
  }

  public void updateColors () {
    setBackgroundColor(Theme.getColor(ColorId.chatKeyboard));
    for (int index = 0; index < labels.length; index++) {
      boolean active = index == selected;
      indicators[index].setColor(active ? Theme.getColor(ColorId.circleButtonTheme) : Color.TRANSPARENT);
      iconViews[index].setColorFilter(Theme.getColor(active ? ColorId.circleButtonThemeIcon : ColorId.icon));
      labelViews[index].setTextColor(Theme.getColor(active ? ColorId.text : ColorId.textLight));
      getChildAt(index).getBackground().invalidateSelf();
    }
  }
}
