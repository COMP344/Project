MODULE RepeatStat;
  INT x, y;
BEGIN
  REPEAT x := x + 10; y := y - 1 UNTIL y = 0;
  REPEAT DEC y UNTIL y = 0
END RepeatStat.
