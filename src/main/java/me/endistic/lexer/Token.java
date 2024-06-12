package me.endistic.lexer;

public sealed interface Token {
    record Keyword(String value, SpanData span) implements Token {
        @Override
        public String toString() {
            return '`' + this.value + '`';
        }
    }

    record Number(String value, SpanData span) implements Token {
        @Override
        public String toString() {
            return this.value;
        }
    }

    record StringValue(String value, SpanData span) implements Token {
        @Override
        public String toString() {
            return '"' + this.value + '"';
        }
    }

    record Boolean(boolean value, SpanData span) implements Token {
        @Override
        public String toString() {
            return java.lang.Boolean.toString(this.value);
        }
    }

    record OpenParen(SpanData span) implements Token {
        @Override
        public String toString() {
            return "(";
        }
    }

    record CloseParen(SpanData span) implements Token {
        @Override
        public String toString() {
            return ")";
        }
    }

    record OpenBrace(SpanData span) implements Token {
        @Override
        public String toString() {
            return "{";
        }
    }

    record CloseBrace(SpanData span) implements Token {
        @Override
        public String toString() {
            return "}";
        }
    }

    record OpenBracket(SpanData span) implements Token {
        @Override
        public String toString() {
            return "[";
        }
    }

    record CloseBracket(SpanData span) implements Token {
        @Override
        public String toString() {
            return "]";
        }
    }

    record Plus(SpanData span) implements Token {
        @Override
        public String toString() {
            return "+";
        }
    }

    record Minus(SpanData span) implements Token {
        @Override
        public String toString() {
            return "-";
        }
    }

    record Star(SpanData span) implements Token {
        @Override
        public String toString() {
            return "*";
        }
    }

    record Slash(SpanData span) implements Token {
        @Override
        public String toString() {
            return "/";
        }
    }

    record Equals(SpanData span) implements Token {
        @Override
        public String toString() {
            return "=";
        }
    }

    record Colon(SpanData span) implements Token {
        @Override
        public String toString() {
            return ":";
        }
    }

    record NewLine(SpanData span) implements Token {
        @Override
        public String toString() {
            return "%newline%";
        }
    }

    SpanData span();

}
