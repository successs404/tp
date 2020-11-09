package team.serenity.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonRootName;

import team.serenity.commons.exceptions.IllegalValueException;
import team.serenity.model.group.Group;
import team.serenity.model.group.UniqueGroupList;
import team.serenity.model.group.exceptions.DuplicateException;
import team.serenity.model.managers.ReadOnlyGroupManager;
import team.serenity.model.managers.Serenity;
import team.serenity.model.util.UniqueList;

/**
 * An Immutable Serenity that is serializable to JSON format.
 */
@JsonRootName(value = "serenity")
class JsonSerializableSerenity {

    private final List<JsonAdaptedGroup> groups = new ArrayList<>();

    /**
     * Creates a new JsonSerializableSerenity object.
     *
     * @param manager The ReadOnlyGroupManager involved.
     */
    public JsonSerializableSerenity(ReadOnlyGroupManager manager) {
        this.groups.addAll(manager.getListOfGroups().stream().map(JsonAdaptedGroup::new).collect(Collectors.toList()));
    }

    /**
     * Converts this serenity object into the model's {@code Serenity} object.
     *
     * @return A new Serenity object containing the groups from the JSON file.
     * @throws IllegalValueException Thrown if there are any data constraints violated.
     * @throws DuplicateException Thrown if there are any duplicates.
     */
    public Serenity toModelType() throws IllegalValueException, DuplicateException {
        UniqueList<Group> groups = new UniqueGroupList();
        for (JsonAdaptedGroup jsonAdaptedGroup : this.groups) {
            Group group = jsonAdaptedGroup.toModelType();
            groups.add(group);
        }
        return new Serenity(groups.asUnmodifiableObservableList());
    }
}
