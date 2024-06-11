package me.endistic.util;

import java.util.function.Function;

public sealed interface Result<T, E> {
    public record Ok<T, E>(T inner) implements Result<T, E> {
        @Override
        public boolean isOk() {
            return true;
        }

        @Override
        public boolean isErr() {
            return false;
        }

        @Override
        public Result<T, E> map(Function<T, T> func) {
            return new Ok<T, E>(func.apply(this.inner()));
        }

        @Override
        public T unwrap() {
            return this.inner();
        }

        @Override
        public E unwrapErr() {
            throw new RuntimeException("This is an OK instance");
        }
    }
    public record Err<T, E>(E inner) implements Result<T, E> {
        @Override
        public boolean isOk() {
            return false;
        }

        @Override
        public boolean isErr() {
            return true;
        }

        @Override
        public Result<T, E> map(Function<T, T> func) {
            return this;
        }

        @Override
        public T unwrap() {
            throw new RuntimeException("This is an Err instance");
        }

        @Override
        public E unwrapErr() {
            return this.inner();
        }
    }

    boolean isOk();
    boolean isErr();
    Result<T, E> map(Function<T, T> func);
    T unwrap();
    E unwrapErr();
}
