package io.cdap.wrangler.api.parser;

public class TimeDuration implements Token {
  private final double value;
  private final String unit;

  public TimeDuration(String input) {
    input = input.trim().toLowerCase();
    if (input.endsWith("ms")) {
      value = Double.parseDouble(input.replace("ms", ""));
      unit = "ms";
    } else if (input.endsWith("sec")) {
      value = Double.parseDouble(input.replace("sec", ""));
      unit = "sec";
    } else if (input.endsWith("s")) {
      value = Double.parseDouble(input.replace("s", ""));
      unit = "s";
    } else if (input.endsWith("min")) {
      value = Double.parseDouble(input.replace("min", ""));
      unit = "min";
    } else {
      throw new IllegalArgumentException("Invalid time duration format: " + input);
    }
  }

  public long getMilliseconds() {
    switch (unit) {
      case "ms": return (long) value;
      case "s":
      case "sec": return (long) (value * 1000);
      case "min": return (long) (value * 60 * 1000);
      default: throw new IllegalStateException("Unknown unit: " + unit);
    }
  }

  @Override
  public String value() {
    return String.valueOf(getMilliseconds());
  }
}

