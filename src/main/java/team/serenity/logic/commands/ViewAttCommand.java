package team.serenity.logic.commands;

import static java.util.Objects.requireNonNull;
import static team.serenity.commons.core.Messages.MESSAGE_ATTENDANCE_LISTED_OVERVIEW;
import static team.serenity.commons.core.Messages.MESSAGE_GROUP_EMPTY;
import static team.serenity.logic.parser.CliSyntax.PREFIX_GRP;

import team.serenity.logic.commands.exceptions.CommandException;
import team.serenity.model.Model;
import team.serenity.model.group.GroupContainsKeywordPredicate;

/**
 * Display the attendance data of all students across all lessons in the group specified.
 * Keyword matching is case insensitive.
 */
public class ViewAttCommand extends Command {

    public static final String COMMAND_WORD = "viewatt";
    public static final Object MESSAGE_USAGE = COMMAND_WORD
        + ": Displays the attendance sheet of all students in the specified tutorial group (case-insensitive).\n"
        + "Parameters: "
        + PREFIX_GRP + "GROUP_NAME\n"
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_GRP + "G01\n";

    private final GroupContainsKeywordPredicate predicate;

    public ViewAttCommand(GroupContainsKeywordPredicate predicate) {
        this.predicate = predicate;
    }

    private String getMessage(Model model) {
        return model.getFilteredGroupList().isEmpty()
            ? MESSAGE_GROUP_EMPTY
            : String.format(MESSAGE_ATTENDANCE_LISTED_OVERVIEW, model.getFilteredGroupList().get(0).getGroupName());
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredGroupList(predicate);
        checkIfGroupIsEmpty(model);
        model.updateFilteredLessonList(Model.PREDICATE_SHOW_ALL_LESSONS);
        return new CommandResult(getMessage(model), CommandResult.UiAction.VIEW_ATT);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof ViewAttCommand // instanceof handles nulls
            && predicate.equals(((ViewAttCommand) other).predicate)); // state check
    }

}
