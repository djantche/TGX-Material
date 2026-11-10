import org.thunderdog.challegram.theme.MaterialColorScheme;

public final class MaterialColorSchemeTest {
  public static void main (String[] args) {
    for (boolean dark : new boolean[] {false, true}) {
      MaterialColorScheme scheme = MaterialColorScheme.fallback(dark);
      check(scheme);
    }
    if (args.length != 0 && args.length != 20) {
      throw new IllegalArgumentException("Supply ten ARGB colors for light mode and ten for dark mode.");
    }
    for (int offset = 0; offset < args.length; offset += 10) {
      int[] colors = new int[10];
      for (int index = 0; index < colors.length; index++) {
        colors[index] = Integer.parseUnsignedInt(args[offset + index], 16);
      }
      check(new MaterialColorScheme(colors[0], colors[1], colors[2], colors[3], colors[4], colors[5], colors[6], colors[7], colors[8], colors[9]));
    }
    System.out.println("Material color tests passed: light/dark contrast, semantic states and fallback compatibility.");
  }

  private static void check (MaterialColorScheme scheme) {
      contrast("body", scheme.color("text", 0), scheme.color("filling", 0));
      contrast("secondary text", scheme.color("textLight", 0), scheme.color("filling", 0));
      contrast("incoming message", scheme.color("bubbleIn_text", 0), scheme.color("bubbleIn_background", 0));
      contrast("outgoing message", scheme.color("bubbleOut_text", 0), scheme.color("bubbleOut_background", 0));
      contrast("unread badge", scheme.color("badgeText", 0), scheme.color("badge", 0));
      contrast("muted badge", scheme.color("badgeMutedText", 0), scheme.color("badgeMuted", 0));
      contrast("floating action", scheme.color("circleButtonRegularIcon", 0), scheme.color("circleButtonRegular", 0));
      contrast("navigation label", scheme.color("headerText", 0), scheme.color("chatKeyboard", 0));
      contrast("selected switch thumb", scheme.color("togglerPositive", 0), scheme.color("togglerPositiveBackground", 0));
      contrast("selected switch glyph", scheme.color("togglerPositiveContent", 0), scheme.color("togglerPositive", 0));
      same("error state", 0xffba1a1a, scheme.color("inputNegative", 0xffba1a1a));
      same("secure chat indicator", 0xff168254, scheme.color("textSecure", 0xff168254));
      same("unknown extension", 0xff123456, scheme.color("futureColor", 0xff123456));
      same("content avatar", 0xff8090a0, scheme.color("avatarBlue", 0xff8090a0));
  }

  private static void same (String label, int expected, int actual) {
    if (expected != actual) {
      throw new AssertionError(label + ": expected " + Integer.toHexString(expected) + ", got " + Integer.toHexString(actual));
    }
  }

  private static void contrast (String label, int foreground, int background) {
    double first = luminance(foreground);
    double second = luminance(background);
    double ratio = (Math.max(first, second) + 0.05) / (Math.min(first, second) + 0.05);
    if (ratio < 4.5) {
      throw new AssertionError(label + " contrast below 4.5:1: " + ratio);
    }
  }

  private static double luminance (int color) {
    return 0.2126 * linear((color >> 16) & 255) + 0.7152 * linear((color >> 8) & 255) + 0.0722 * linear(color & 255);
  }

  private static double linear (int value) {
    double channel = value / 255.0;
    return channel <= 0.04045 ? channel / 12.92 : Math.pow((channel + 0.055) / 1.055, 2.4);
  }
}
