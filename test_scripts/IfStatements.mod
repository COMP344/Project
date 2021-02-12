MODULE IfStatements;
  INT x; BOOL p, q;
BEGIN
  IF p THEN x := 0-x END ;
  IF p THEN x := 1 ELSIF q THEN x := 2 END ;
  IF p THEN x := 3 ELSIF q THEN x := 4 ELSE x := 5 END
END IfStatements.
