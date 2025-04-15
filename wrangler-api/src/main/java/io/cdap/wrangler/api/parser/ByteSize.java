
package io.cdap.wrangler.api.parser;

public class ByteSize implements Token {
  private final double value;
  private final String unit;

  public ByteSize(String input) {
    input = input.trim().toUpperCase();
    if (input.endsWith("KB")) {
      value = Double.parseDouble(input.replace("KB", ""));
      unit = "KB";
    } else if (input.endsWith("MB")) {
      value = Double.parseDouble(input.replace("MB", ""));
      unit = "MB";
    } else if (input.endsWith("GB")) {
      value = Double.parseDouble(input.replace("GB", ""));
      unit = "GB";
    } else if (input.endsWith("TB")) {
      value = Double.parseDouble(input.replace("TB", ""));
      unit = "TB";
    } else if (input.endsWith("B")) {
      value = Double.parseDouble(input.replace("B", ""));
      unit = "B";
    } else {
      throw new IllegalArgumentException("Invalid byte size format: " + input);
    }
  }

  public long getBytes() {
    switch (unit) {
      case "KB": return (long) (value * 1024);
      case "MB": return (long) (value * 1024 * 1024);
      case "GB": return (long) (value * 1024 * 1024 * 1024);
      case "TB": return (long) (value * 1024L * 1024L * 1024L * 1024L);
      case "B":  return (long) value;
      default: throw new IllegalStateException("Unknown unit: " + unit);
    }
  }

  @Override
  public String value() {
    return String.valueOf(getBytes());
  }
}
