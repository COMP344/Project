MODULE WhileStatements;
  INT x, y, z; BOOL b;
BEGIN
  WHILE x # 0 DO z := z + y; x := x – 1 END ;
  WHILE x = y & ~b DO !b END ;
  WHILE x >= y OR b DO !~b END ;
END WhileStatements.
