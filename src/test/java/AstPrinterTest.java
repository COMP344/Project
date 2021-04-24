import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AstPrinterTest {
    AstPrinter astPrinter;
    Expr simpleExpr;
    Stmt simpleStmt;
    Mod simpleModHead;
    Mod simpleModBody;
    Decl simpleDecl;


    @Before
    public void setUp() {
        List<Stmt> statement_list = new ArrayList<>();
        statement_list.add(simpleStmt);
        astPrinter = new AstPrinter();
        simpleExpr = new Expr.Literal(10);
        simpleStmt = new Stmt.ExprStmt(simpleExpr);
        simpleModHead = new Mod.Header(new Token(Token.TokenType.MODULE, "Test", null, 0, 0));
        simpleModBody = new Mod.Body(null, null, null, new Stmt.Seq(statement_list));
        simpleDecl = new Decl.Module((Mod.Header) simpleModHead, (Mod.Body) simpleModBody);
    }

    @Test
    public void testPrintExpr() {
        Assert.assertEquals("Expected: 10", "10", astPrinter.printExpr(simpleExpr));
    }

    @Test
    public void testPrintDecl() {
        String expected = """
                MODULE Test;
                \t10
                END Test.""";
        Assert.assertEquals("Expected:\n" + expected, expected, astPrinter.printDecl(simpleDecl));
    }

    @Test
    public void testPrintMod() {
        String expected = "Test;\n";
        Assert.assertEquals("Expected: " + expected, expected, astPrinter.printMod(simpleModHead));
    }

    @Test
    public void testPrintStmt() {
        Assert.assertEquals("Expected: 10", "10", astPrinter.printStmt(simpleStmt));
    }

    @Test
    public void testVisitUnaryExpr() {
        Expr.Unary unary = new Expr.Unary(new Token(Token.TokenType.PLUS, "+", null, 0, 0), simpleExpr);
        Assert.assertEquals("Expected: (+ 10)", "(+ 10)", astPrinter.visitUnaryExpr(unary));
    }

    @Test
    public void testVisitBinaryExpr() {
        Expr.Binary binary = new Expr.Binary(new Token(Token.TokenType.PLUS, "+", null, 0, 0), simpleExpr, simpleExpr);
        Assert.assertEquals("Expected: (+ 10 10)", "(+ 10 10)", astPrinter.visitBinaryExpr(binary));
    }

    @Test
    public void testVisitAssignExpr() {
        Expr.Assign assign = new Expr.Assign(new Token(Token.TokenType.IDENT, "N", null, 0, 0), simpleExpr);
        Assert.assertEquals("Expected: (= N 10)", "(= N 10)", astPrinter.visitAssignExpr(assign));
    }

    @Test
    public void testVisitLogicalExpr() {
        Expr.Logical logical = new Expr.Logical(new Expr.Literal(5), new Token(Token.TokenType.LEQ, "<=", null, 0, 0), simpleExpr);
        Assert.assertEquals("Expected: (<= 5 10)", "(<= 5 10)", astPrinter.visitLogicalExpr(logical));
    }

    @Test
    public void testVisitLiteral() {
        Assert.assertEquals("Expected: 10", "10", astPrinter.visitLiteral(new Expr.Literal(10)));
    }

    @Test
    public void testVisitModuleDecl() {
        String expected = """
                MODULE Test;
                \t10
                END Test.""";
        Assert.assertEquals("Expected:\n" + expected, expected, astPrinter.visitModuleDecl((Decl.Module) simpleDecl));
    }

//    @Test
//    public void testVisitConstDecl() {
//        Decl.Const constants = new Decl.Const(
//                Collections.singletonList(new Stmt.Assign(
//                        new Token(Token.TokenType.IDENT, "N", null, 0, 0),
//                        new Expr.Literal(10))
//                ));
//        Assert.assertEquals("Expected: CONST N = 10;", "CONST N = 10;\n", astPrinter.visitConstDecl(constants));
//    }

    @Test
    public void testVisitVarStmt() {
        Stmt.Var vars = new Stmt.Var(
                new Token(Token.TokenType.INT, "INT", null, 0, 0),
                Collections.singletonList(
                        new Token(Token.TokenType.IDENT, "x", null, 0, 0)
                ));
        Assert.assertEquals("Expected:  INT x;", "INT x;\n", astPrinter.visitVarStmt(vars));
    }

    @Test
    public void testVisitProcDecl() {
    }

    @Test
    public void testVisitSeqStmt() {
        List<Stmt> statement_list = new ArrayList<>();
        statement_list.add(new Stmt.ExprStmt(
                new Expr.Literal(10)
        ));
        statement_list.add(new Stmt.ExprStmt(
                new Expr.Literal(20)
        ));
        Stmt.Seq stmts = new Stmt.Seq(statement_list);
        Assert.assertEquals("Expected: \n10\n20", "10\n20\n", astPrinter.visitSeqStmt(stmts));
    }

    @Test
    public void testVisitAssignStmt() {
        Stmt.Assign assign = new Stmt.Assign(
                new Token(Token.TokenType.IDENT, "x", null, 0, 0),
                simpleExpr
        );
        Assert.assertEquals("Expected: x := 10", "x := 10", astPrinter.visitAssignStmt(assign));
    }

    @Test
    public void testVisitExprStmt() {
        Stmt.ExprStmt exprStmt = new Stmt.ExprStmt(
                new Expr.Literal(10)
        );
        Assert.assertEquals("Expected: 10", "10", astPrinter.visitExprStmt(exprStmt));
    }

    @Test
    public void testVisitHeaderMod() {
        Assert.assertEquals("Expected: Test;", "Test;\n", astPrinter.visitHeaderMod((Mod.Header) simpleModHead));
    }

    @Test
    public void testVisitBodyMod() {
        Assert.assertEquals("Expected: \t10\n", "\t10\n", astPrinter.visitBodyMod((Mod.Body) simpleModBody));
    }
}