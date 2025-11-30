package com.cafepos.app.command;

public interface Command {
    void execute();

    default void undo() {/* optional */ }
}
