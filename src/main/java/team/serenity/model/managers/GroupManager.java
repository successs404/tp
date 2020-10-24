package team.serenity.model.managers;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Stream;

import javafx.collections.ObservableList;
import team.serenity.model.group.Group;
import team.serenity.model.group.UniqueGroupList;
import team.serenity.model.util.UniqueList;

/**
 * Manages tutorial groups.
 */
public class GroupManager implements ReadOnlyGroupManager {

    private final UniqueList<Group> listOfGroups;

    /**
     * Instantiates a new QuestionManager.
     */
    public GroupManager() {
        this.listOfGroups = new UniqueGroupList();
    }

    /**
     * Creates a GroupManager using the Questions in the {@code toBeCopied}
     * @param toBeCopied
     */
    public GroupManager(ReadOnlyGroupManager toBeCopied) {
        this.listOfGroups = new UniqueGroupList();
        resetData(toBeCopied);
    }

    // Methods that overrides the whole group list

    /**
     * Replaces the contents of the question list with {@code newListOfGroups}.
     * {@code newListOfGroups} must not contain duplicate groups.
     */
    public void setGroups(List<Group> newListOfGroups) {
        this.listOfGroups.setElementsWithList(newListOfGroups);
    }

    /**
     * Resets the existing data of this {@code GroupManager} with {@code newData}
     */
    public void resetData(ReadOnlyGroupManager newData) {
        requireNonNull(newData);
        setGroups(newData.getListOfGroups());
    }

    /**
     * Returns the list of groups as an unmodifiable list
     */
    public ObservableList<Group> getListOfGroups() {
        return this.listOfGroups.asUnmodifiableObservableList();
    }

    // Group-level operations

    /**
     * Checks whether group exists.
     *
     * @param group Group to check for
     * @return Whether given group exists
     */
    public boolean hasGroup(Group group) {
        requireNonNull(group);
        return this.listOfGroups.contains(group);
    }

    public boolean hasGroup() {
        return this.listOfGroups.size() > 0;
    }

    public Stream<Group> getStream() {
        return this.listOfGroups.stream();
    }

    /**
     * Adds given group to the list, if it doesn't exist yet.
     *
     * @param group Group to be added
     */
    public void addGroup(Group group) {
        requireNonNull(group);
        if (!hasGroup(group)) {
            this.listOfGroups.add(group);
        }
    }

    /**
     * Deletes a specified {@code Group} from the list.
     *
     * @param group
     */
    public void deleteGroup(Group group) {
        requireNonNull(group);
        if (hasGroup(group)) {
            this.listOfGroups.remove(group);
        }
    }

}
