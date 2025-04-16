package io.cdap.wrangler.directives.aggregates;

import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.annotations.Description;
import io.cdap.wrangler.api.annotations.Name;
import io.cdap.wrangler.api.annotations.Plugin;
import io.cdap.wrangler.api.annotations.Scope;
import io.cdap.wrangler.api.parser.*;

import java.util.Collections;
import java.util.List;

@Plugin(type = Directive.TYPE)
@Name("aggregate-stats")
@Description("Aggregates total size and time from given columns, with optional unit conversion.")
@Scope(Scope.Type.TRANSFORM)
public class AggregateStats implements Directive {
  private String sizeCol;
  private String timeCol;
  private String outputSizeCol;
  private String outputTimeCol;

  @Override
  public void initialize(DirectiveContext ctx, DirectiveArguments args) {
    sizeCol = ((ColumnName) args.value("sizeCol")).value();
    timeCol = ((ColumnName) args.value("timeCol")).value();
    outputSizeCol = ((ColumnName) args.value("outputSizeCol")).value();
    outputTimeCol = ((ColumnName) args.value("outputTimeCol")).value();
  }

  @Override
  public List<Row> execute(List<Row> rows, ExecutorContext ctx) throws DirectiveExecutionException {
    long totalBytes = 0;
    long totalNanos = 0;
    int count = 0;

    for (Row row : rows) {
      Object sizeObj = row.getValue(sizeCol);
      Object timeObj = row.getValue(timeCol);

      if (sizeObj instanceof String && timeObj instanceof String) {
        try {
          long bytes = new io.cdap.wrangler.api.parser.ByteSize((String) sizeObj).getBytes();
          long nanos = new io.cdap.wrangler.api.parser.TimeDuration((String) timeObj).getNanos();
          totalBytes += bytes;
          totalNanos += nanos;
          count++;
        } catch (IllegalArgumentException e) {
          throw new DirectiveExecutionException("Failed to parse ByteSize or TimeDuration", e);
        }
      }
    }

    double totalMB = totalBytes / (1024.0 * 1024);
    double totalSeconds = totalNanos / 1_000_000_000.0;

    Row result = new Row();
    result.add(outputSizeCol, totalMB);
    result.add(outputTimeCol, totalSeconds);
    return Collections.singletonList(result);
  }
}


