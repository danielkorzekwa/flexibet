This page contains sql reports that provide some useful data not available on the web console:

  1. Number of placed bets for a given day grouped by hour:

```
select date_trunc('hour',placed_date) as hour,count(*)  from runner_state_bet where placed_date between '2010-04-22 00:00:00' and '2010-04-22 23:59:59' group by hour order by hour

Example:
"2010-04-22 00:00:00";830
"2010-04-22 01:00:00";486
"2010-04-22 02:00:00";412
"2010-04-22 03:00:00";338
"2010-04-22 04:00:00";318
"2010-04-22 05:00:00";279
"2010-04-22 06:00:00";388
"2010-04-22 07:00:00";330
"2010-04-22 08:00:00";526
"2010-04-22 09:00:00";393
```