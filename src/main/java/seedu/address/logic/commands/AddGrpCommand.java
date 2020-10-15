package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GRP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PATH;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.Group;
import seedu.address.model.group.GrpContainsKeywordPredicate;

public class AddGrpCommand extends Command {

    public static final String COMMAND_WORD = "addgrp";
    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Adds a new tutorial group.\n"
        + "Parameters: "
        + PREFIX_GRP + "GRP "
        + PREFIX_PATH + "PATH\n"
        + "Example: " + COMMAND_WORD + " " + PREFIX_GRP + "G04 " + PREFIX_PATH + "CS2101_G04.csv";

    public static final String MESSAGE_SUCCESS = "New tutorial group added: %1$s";
    public static final String MESSAGE_DUPLICATE_GROUP = "This tutorial group already exists.";

    private final Group toAdd;

    /**
     * Creates an AddGrpCommand to add the specified {@code Group}
     */
    public AddGrpCommand(Group group) {
        requireNonNull(group);
        toAdd = group;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasGroupName(toAdd.getName())) {
            throw new CommandException(MESSAGE_DUPLICATE_GROUP);
        }

        model.addGroup(toAdd);
        model.updateFilteredGroupList(new GrpContainsKeywordPredicate(toAdd.getName()));

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd), false, false, false, true);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof AddGrpCommand // instanceof handles nulls
            && toAdd.equals(((AddGrpCommand) other).toAdd));
    }

}
