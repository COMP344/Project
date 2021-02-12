MODULE Conditions;
  INT x, y, z, w; SET s; BOOL b;
BEGIN
  IF x = y THEN z := 0 END ;
  IF x = y & y # z & z >= w THEN z := 0 END ;
  IF x < y OR y <= z OR z > w THEN z := 0 END
END Conditions.
