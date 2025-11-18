package com.solvd.navigator.cli;

public interface Command {
    String name();
    String key();
    void execute();
}
