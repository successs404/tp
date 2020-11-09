package team.serenity.logic.commands;

import static java.util.Objects.requireNonNull;
import static team.serenity.commons.core.Messages.MESSAGE_GROUP_EMPTY;
import static team.serenity.logic.parser.CliSyntax.PREFIX_GRP;

import team.serenity.logic.commands.exceptions.CommandException;
import team.serenity.model.Model;
import team.serenity.model.group.Group;
import team.serenity.model.group.GroupContainsKeywordPredicate;

public class DelGrpCommand extends Command {

    public static final String COMMAND_WORD = "delgrp";
    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Deletes an existing tutorial group.\n"
        + "Parameter: "
        + PREFIX_GRP + "GROUP_NAME\n"
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_GRP + "G01\n";

    public static final String MESSAGE_DELETE_GROUP_SUCCESS = "Tutorial group deleted: %1$s";

    private final GroupContainsKeywordPredicate grpPredicate;

    /**
     * Creates a DelGrpCommand to delete the specified {@code Group} in the predicate.
     */
    public DelGrpCommand(GroupContainsKeywordPredicate grpPredicate) {
        this.grpPredicate = grpPredicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredGroupList(grpPredicate);
        checkIfGroupIsEmpty(model);
        Group toDel = model.getFilteredGroupList().get(0);
        model.deleteGroup(toDel);
        return new CommandResult(String.format(MESSAGE_DELETE_GROUP_SUCCESS, toDel), CommandResult.UiAction.DEL_GRP);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof DelGrpCommand // instanceof handles nulls
            && grpPredicate.equals(((DelGrpCommand) other).grpPredicate));
    }
}
