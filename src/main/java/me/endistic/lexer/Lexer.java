package me.endistic.lexer;

import me.endistic.util.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lexer {
    public String internal;
    public int index = -1;

    public static List<Token> lex(String lex) {
        var lexer = new Lexer();
        lexer.internal = lex;
        return lexer.getTokens();
    }

    public List<Token> getTokens() {
        var toks = new ArrayList<Token>();
        while(true) {
            var incoming = tryEat();
            if(incoming.isOk()) {
                toks.add(incoming.unwrap());
            } else break;
        }
        return toks;
    }

    String read() {
        return String.valueOf(internal.charAt(++index));
    }

    String peek() {
        return String.valueOf(internal.charAt(index+1));
    }

    void skipWhitespace() {
        while(Character.isWhitespace(internal.charAt(index+1)))
            index++;
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
                return new Result.Ok<>(new Token.Keyword(b.toString()));
            }

            var numberString = "123456789";
            if(numberString.contains(peek())) {
                var b = new StringBuilder();
                while (numberString.contains(peek())) {
                    b.append(read());
                }
                return new Result.Ok<>(new Token.Number(b.toString()));
            }


            return switch(read()) {
                case "\"" -> {
                    var sb = new StringBuilder();
                    while (!peek().equals("\""))
                        sb.append(read());
                    yield new Result.Ok<>(new Token.StringValue(sb.toString()));
                }

                case "(" -> new Result.Ok<>(new Token.OpenParen());
                case ")" -> new Result.Ok<>(new Token.CloseParen());
                case "[" -> new Result.Ok<>(new Token.OpenBracket());
                case "]" -> new Result.Ok<>(new Token.CloseBracket());
                case "{" -> new Result.Ok<>(new Token.OpenBrace());
                case "}" -> new Result.Ok<>(new Token.CloseBrace());

                case "+" -> new Result.Ok<>(new Token.Plus());
                case "-" -> new Result.Ok<>(new Token.Minus());
                case "*" -> new Result.Ok<>(new Token.Star());
                case "/" -> new Result.Ok<>(new Token.Slash());
                case "=" -> new Result.Ok<>(new Token.Equals());

                default -> new Result.Err<>(new RuntimeException("invalid char"));
            };
        } catch (StringIndexOutOfBoundsException ex) {
            return new Result.Err<>(ex);
        }

    }
}
