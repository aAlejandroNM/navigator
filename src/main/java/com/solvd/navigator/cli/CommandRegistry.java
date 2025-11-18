package com.solvd.navigator.cli;

import java.util.Map;
import java.util.HashMap;


public class CommandRegistry {

    private final Map<String, Command> commands = new HashMap<>();

    public void register(Command command) {
        if (commands.containsKey(command.key())) {
            throw new IllegalStateException("Duplicate command key: " + command.key());
        }
        commands.put(command.key(), command);
    }

    public Command get(String key) {
        return commands.get(key);
    }

    public Map<String, Command> getAll() {
        return commands;
    }
}