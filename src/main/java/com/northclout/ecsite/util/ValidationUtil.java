package com.northclout.ecsite.util;

import java.util.regex.Pattern;

public final class ValidationUtil {
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

  private ValidationUtil() {
  }

  public static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  public static boolean isEmail(String value) {
    if (isBlank(value)) {
      return false;
    }
    return EMAIL_PATTERN.matcher(value).matches();
  }

  public static boolean isPositiveInt(String value, int min, int max) {
    if (isBlank(value)) {
      return false;
    }
    try {
      int v = Integer.parseInt(value);
      return v >= min && v <= max;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static boolean isLong(String value) {
    if (isBlank(value)) {
      return false;
    }
    try {
      Long.parseLong(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
