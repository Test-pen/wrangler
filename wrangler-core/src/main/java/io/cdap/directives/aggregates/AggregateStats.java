package io.cdap.wrangler.directives.aggregates;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.DirectiveContext;
import io.cdap.wrangler.api.annotations.Name;
import io.cdap.wrangler.api.annotations.Description;
import io.cdap.wrangler.api.annotations.Scope;
import io.cdap.wrangler.api.annotations.Plugin;
import io.cdap.wrangler.api.parser.DirectiveArguments;
import io.cdap.wrangler.api.parser.Text;
import io.cdap.wrangler.api.parser.Arguments;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@Plugin(type = Directive.TYPE)
@Name("aggregate-stats")
@Description("Computes basic aggregate statistics like min, max, avg, and sum for a numeric column")
@Scope(Scope.Type.TRANSFORM)
public class AggregateStats implements Directive {
  private String column;

  @Override
  public void initialize(DirectiveContext ctx, DirectiveArguments args) {
    column = ((Text) args.value("column")).value();
  }

  @Override
  public List<Row> execute(List<Row> rows, ExecutorContext ctx) {
    if (rows.isEmpty()) {
      return rows;
    }

    double sum = 0;
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    int count = 0;

    for (Row row : rows) {
      Object val = row.getValue(column);
      if (val instanceof Number) {
        double number = ((Number) val).doubleValue();
        sum += number;
        min = Math.min(min, number);
        max = Math.max(max, number);
        count++;
      }
    }

    double avg = count == 0 ? 0 : sum / count;

    Row statRow = new Row("column", column);
    statRow.add("sum", sum);
    statRow.add("min", min);
    statRow.add("max", max);
    statRow.add("avg", avg);
    statRow.add("count", count);

    return Collections.singletonList(statRow);
  }
}

