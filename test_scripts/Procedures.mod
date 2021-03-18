MODULE Procedures;
  INT x, y;
  
  PROCEDURE NofBits(INT x): INT;
    INT cnt, n;
  BEGIN cnt := 0; n := 8;
    REPEAT
      IF x.0 THEN INC cnt END ];
      ROR x; DEC n
    UNTIL n = 0;
    RETURN cnt
  END NofBits;

  PROCEDURE Swap;
    INT z;
  BEGIN z := x; x := y; y := z
  END Swap;

  PROCEDURE P(INT a);
  BEGIN
    x := a + 10
  END P;

BEGIN Swap; P(y); x := NofBits(y)
END Procedures.
