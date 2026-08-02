package me.nam.promine.command;

/**
 * Represents a command that can be executed in ProMine.
 */
public class PromineCommand {
    private String name;
    private String[] args;

    public PromineCommand(String name, String[] args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public String[] getArgs() {
        return args;
    }
}
