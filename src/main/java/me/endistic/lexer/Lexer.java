package me.endistic.lexer;

import me.endistic.util.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lexer {
    public String internal;
    public int index = -1;

    int column = 1;
    int row = 1;

    List<Token> toks = new ArrayList<Token>();

    public static List<Token> lex(String lex) {
        var lexer = new Lexer();
        lexer.internal = lex;
        return lexer.getTokens();
    }

    public String getInternalLine() {
        try {
            return internal.split("\n")[column-1];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return "";
        }

    }

    public List<Token> getTokens() {
        while(true) {
            var incoming = tryEat();
            if(incoming.isOk()) {
                toks.add(incoming.unwrap());
            } else break;
        }
        return toks;
    }

    String read() {
        row++;
        return String.valueOf(internal.charAt(++index));
    }

    String peek() {
        return String.valueOf(internal.charAt(index+1));
    }

    void skipWhitespace() {
        while(Character.isWhitespace(internal.charAt(index+1))) {
            if(internal.charAt(index+1) == '\n') {
                column++;
                row = 1;
                toks.add(new Token.NewLine(new SpanData(
                    this.column,
                    this.row,
                    this.getInternalLine(),
                    this.internal
                )));
            }
            index++;
            row++;
        }

    }

    char match(char check) {
        if(internal.charAt(index+1) != check)
            throw new RuntimeException("match");
        index++;
        return check;
    }

    public Result<Token, Exception> tryEat() {
        try {
            skipWhitespace();

            var keywordString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_.";
            if(keywordString.contains(peek())) {
                var b = new StringBuilder();
                while (keywordString.contains(peek())) {
                    b.append(read());
                }
                return new Result.Ok<>(new Token.Keyword(b.toString(),
                    new SpanData(this.column, this.row, getInternalLine(), internal)));
            }

            var numberString = "123456789";
            if(numberString.contains(peek())) {
                var b = new StringBuilder();
                while (numberString.contains(peek())) {
                    b.append(read());
                }
                return new Result.Ok<>(new Token.Number(b.toString(),
                    new SpanData(this.column, this.row, getInternalLine(), internal)));
            }


            return switch(read()) {
                case "\"" -> {
                    var sb = new StringBuilder();
                    while (!peek().equals("\""))
                        sb.append(read());
                    match('"');
                    yield new Result.Ok<>(new Token.StringValue(sb.toString(),
                        new SpanData(this.column, this.row, getInternalLine(), internal)));
                }

                case "(" -> new Result.Ok<>(new Token.OpenParen(
                    new SpanData(this.column, this.row, getInternalLine(), internal)
                ));
                case ")" -> new Result.Ok<>(new Token.CloseParen(
                    new SpanData(this.column, this.row, getInternalLine(), internal)
                ));
                case "[" -> new Result.Ok<>(new Token.OpenBracket(
                    new SpanData(this.column, this.row, getInternalLine(), internal)
                ));
                case "]" -> new Result.Ok<>(new Token.CloseBracket(
                    new SpanData(this.column, this.row, getInternalLine(), internal)
                ));
                case "{" -> new Result.Ok<>(new Token.OpenBrace(
                    new SpanData(this.column, this.row, getInternalLine(), internal)
                ));
                case "}" -> new Result.Ok<>(new Token.CloseBrace(
                    new SpanData(this.column, this.row, getInternalLine(), internal)
                ));

                case "+" -> new Result.Ok<>(new Token.Plus(
                    new SpanData(this.column, this.row, getInternalLine(), internal)
                ));
                case "-" -> new Result.Ok<>(new Token.Minus(
                    new SpanData(this.column, this.row, getInternalLine(), internal)
                ));
                case "*" -> new Result.Ok<>(new Token.Star(
                    new SpanData(this.column, this.row, getInternalLine(), internal)
                ));
                case "/" -> new Result.Ok<>(new Token.Slash(
                    new SpanData(this.column, this.row, getInternalLine(), internal)
                ));
                case "=" -> new Result.Ok<>(new Token.Equals(
                    new SpanData(this.column, this.row, getInternalLine(), internal)
                ));
                case ":" -> new Result.Ok<>(new Token.Colon(
                    new SpanData(this.column, this.row, getInternalLine(), internal)
                ));

                default -> new Result.Err<>(new RuntimeException("invalid char"));
            };
        } catch (StringIndexOutOfBoundsException ex) {
            return new Result.Err<>(ex);
        }

    }
}
