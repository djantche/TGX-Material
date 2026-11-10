package org.thunderdog.challegram.theme;

public final class MaterialColorScheme {
  public final int primary;
  public final int onPrimary;
  public final int primaryContainer;
  public final int onPrimaryContainer;
  public final int surface;
  public final int surfaceContainer;
  public final int surfaceContainerHigh;
  public final int onSurface;
  public final int onSurfaceVariant;
  public final int outlineVariant;

  public MaterialColorScheme (int primary, int onPrimary, int primaryContainer, int onPrimaryContainer, int surface, int surfaceContainer, int surfaceContainerHigh, int onSurface, int onSurfaceVariant, int outlineVariant) {
    this.primary = primary;
    this.onPrimary = onPrimary;
    this.primaryContainer = primaryContainer;
    this.onPrimaryContainer = onPrimaryContainer;
    this.surface = surface;
    this.surfaceContainer = surfaceContainer;
    this.surfaceContainerHigh = surfaceContainerHigh;
    this.onSurface = onSurface;
    this.onSurfaceVariant = onSurfaceVariant;
    this.outlineVariant = outlineVariant;
  }

  public static MaterialColorScheme fallback (boolean dark) {
    // Vivid Material 3 blue baseline, used when the system palette is missing or nearly grey
    return dark ?
      new MaterialColorScheme(0xffa8c7fa, 0xff062e6f, 0xff0842a0, 0xffd3e3fd, 0xff131314, 0xff1e1f20, 0xff282a2c, 0xffe3e3e3, 0xffc4c7c5, 0xff444746) :
      new MaterialColorScheme(0xff0b57d0, 0xffffffff, 0xffd3e3fd, 0xff041e49, 0xfff8fafd, 0xfff0f4f9, 0xffe9eef6, 0xff1f1f1f, 0xff444746, 0xffc4c7c5);
  }

  public int color (String name, int fallback) {
    switch (name) {
      case "background":
      case "filling":
      case "chatBackground":
      case "bubble_chatBackground":
      case "headerBackground":
      case "headerLightBackground":
      case "statusBar":
      case "drawer":
        return surface;
      case "chatKeyboard":
      case "overlayFilling":
      case "snackbarUpdate":
        return surfaceContainer;
      case "chatKeyboardButton":
      case "badgeMuted":
      case "bubbleIn_background":
      case "bubble_date_noWallpaper":
      case "bubble_unread_noWallpaper":
      case "bubble_mediaReply_noWallpaper":
      case "bubble_button_noWallpaper":
      case "bubble_overlay_noWallpaper":
      case "fillingPressed":
      case "togglerInactiveBackground":
        return surfaceContainerHigh;
      case "bubbleOut_background":
      case "circleButtonActive":
      case "circleButtonTheme":
        return primaryContainer;
      case "bubbleOut_text":
      case "circleButtonActiveIcon":
      case "circleButtonThemeIcon":
        return onPrimaryContainer;
      case "text":
      case "bubbleIn_text":
      case "headerText":
      case "headerLightText":
      case "headerTabActiveText":
      case "drawerText":
      case "statusBarContent":
      case "statusBarLegacyContent":
        return onSurface;
      case "headerIcon":
      case "headerLightIcon":
      case "headerButton":
      case "textLight":
      case "textPlaceholder":
      case "background_text":
      case "background_textLight":
      case "icon":
      case "iconLight":
      case "background_icon":
      case "chatListIcon":
      case "chatListMute":
      case "headerTabInactiveText":
      case "bubbleIn_time":
      case "bubbleOut_time":
      case "bubbleOut_ticks":
      case "togglerInactive":
      case "badgeMutedText":
      case "bubble_dateText_noWallpaper":
      case "bubble_unreadText_noWallpaper":
      case "bubble_mediaReplyText_noWallpaper":
      case "bubble_overlayText_noWallpaper":
        return onSurfaceVariant;
      case "textLink":
      case "textNeutral":
      case "iconActive":
      case "headerTabActive":
      case "chatSendButton":
      case "circleButtonRegular":
      case "chatListAction":
      case "chatListVerify":
      case "controlActive":
      case "checkActive":
      case "inputActive":
      case "sliderActive":
      case "togglerPositiveBackground":
      case "togglerPositiveContent":
      case "progress":
      case "badge":
      case "bubbleOut_ticksRead":
      case "bubbleIn_textLink":
      case "bubbleOut_textLink":
      case "bubbleIn_progress":
      case "bubbleOut_progress":
      case "inlineText":
      case "inlineIcon":
      case "file":
      case "bubble_buttonText_noWallpaper":
        return primary;
      case "badgeText":
      case "circleButtonRegularIcon":
      case "checkContent":
      case "controlContent":
      case "fileContent":
      case "togglerPositive":
        return onPrimary;
      case "separator":
      case "chatSeparator":
      case "bubble_chatSeparator":
      case "bubbleIn_outline":
      case "bubbleOut_outline":
        return 0;
      case "inputInactive":
      case "bubbleIn_separator":
      case "bubbleOut_separator":
        return outlineVariant;
      default:
        return fallback;
    }
  }
}
