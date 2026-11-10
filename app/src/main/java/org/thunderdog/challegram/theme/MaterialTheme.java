package org.thunderdog.challegram.theme;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;

import org.thunderdog.challegram.unsorted.AppContext;

public final class MaterialTheme implements ThemeDelegate {
  private final ThemeDelegate source;
  private MaterialColorScheme scheme;
  private int configuration;

  public MaterialTheme (ThemeDelegate source) {
    this.source = source;
  }

  private MaterialColorScheme scheme () {
    Resources resources = AppContext.get().getResources();
    int currentConfiguration = resources.getConfiguration().hashCode();
    if (scheme == null || configuration != currentConfiguration) {
      configuration = currentConfiguration;
      scheme = MaterialColorScheme.fallback(source.isDark());
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && hasVividSystemAccent(resources)) {
        boolean dark = source.isDark();
        scheme = new MaterialColorScheme(
          resources.getColor(dark ? android.R.color.system_accent1_200 : android.R.color.system_accent1_600, null),
          resources.getColor(dark ? android.R.color.system_accent1_800 : android.R.color.system_accent1_0, null),
          resources.getColor(dark ? android.R.color.system_accent1_700 : android.R.color.system_accent1_100, null),
          resources.getColor(dark ? android.R.color.system_accent1_100 : android.R.color.system_accent1_900, null),
          resources.getColor(dark ? android.R.color.system_neutral1_900 : android.R.color.system_neutral1_10, null),
          resources.getColor(dark ? android.R.color.system_neutral1_800 : android.R.color.system_neutral1_50, null),
          resources.getColor(dark ? android.R.color.system_neutral2_800 : android.R.color.system_neutral2_100, null),
          resources.getColor(dark ? android.R.color.system_neutral1_100 : android.R.color.system_neutral1_900, null),
          resources.getColor(dark ? android.R.color.system_neutral2_200 : android.R.color.system_neutral2_700, null),
          resources.getColor(dark ? android.R.color.system_neutral2_700 : android.R.color.system_neutral2_200, null)
        );
      }
    }
    return scheme;
  }

  // A monochrome system style yields an almost grey accent: keep the vivid fallback palette in that case
  private static boolean hasVividSystemAccent (Resources resources) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
      return false;
    }
    float[] hsv = new float[3];
    Color.colorToHSV(resources.getColor(android.R.color.system_accent1_600, null), hsv);
    return hsv[1] >= .25f;
  }

  @Override
  public int getId () {
    return source.getId();
  }

  @Override
  public int getColor (int colorId) {
    return scheme().color(ThemeColors.getName(colorId), source.getColor(colorId));
  }

  @Override
  public String getDefaultWallpaper () {
    return null;
  }

  public String getLegacyDefaultWallpaper () {
    return source.getDefaultWallpaper();
  }

  @Override
  public float getProperty (int propertyId) {
    switch (propertyId) {
      case PropertyId.BUBBLE_CORNER:
        return 22f;
      case PropertyId.BUBBLE_CORNER_MERGED:
      case PropertyId.BUBBLE_CORNER_LEGACY:
        return 6f;
      case PropertyId.IMAGE_CORNER:
        return 16f;
      case PropertyId.BUBBLE_OUTER_MARGIN:
        return 12f;
      case PropertyId.SHADOW_DEPTH:
      case PropertyId.BUBBLE_OUTLINE_SIZE:
      case PropertyId.BUBBLE_UNREAD_SHADOW:
        return 0f;
      case PropertyId.BUBBLE_OUTLINE:
        // Flat bubbles: a full outline factor disables the drop shadow, and the zero size hides the outline itself
        return 1f;
      case PropertyId.LIGHT_STATUS_BAR:
        return source.isDark() ? 0f : 1f;
      default:
        return source.getProperty(propertyId);
    }
  }
}
