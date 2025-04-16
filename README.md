# Zeotap Wrangler Enhancement Assignment

## Overview

This enhancement adds native support for Byte Size (e.g., KB, MB, GB) and Time Duration (e.g., ms, s, min) unit parsing in Wrangler recipes.

## What Was Added

✅ Lexer & Grammar
- Added BYTESIZE and TIMEDURATION lexer rules in Directives.g4
- Updated value rules to allow these tokens

✅ API
- Added ByteSize.java and TimeDuration.java in wrangler-api
- These classes parse string values (e.g., "10KB", "1.5s") and convert to bytes/nanos

✅ Parser
- Updated RecipeVisitor.java to add visit methods for BYTE_SIZE and TIME_DURATION
- Tokens are added to the TokenGroup

✅ Directive
- Created AggregateStats.java under wrangler-core
- Accepts:
  - Column with byte size strings
  - Column with time duration strings
  - Output columns for total/avg size/time (in MB/sec)

✅ Tests
- Unit test for ByteSize (ByteSizeTest.java)
- Unit test for TimeDuration (TimeDurationTest.java)
- Directive test (AggregateStatsTest.java)

## Example Usage
aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec
Build & Test
Run:

bash: run 
mvn clean install
