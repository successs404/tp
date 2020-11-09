package team.serenity.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import team.serenity.commons.exceptions.DataConversionException;
import team.serenity.commons.exceptions.IllegalValueException;
import team.serenity.model.managers.ReadOnlyGroupManager;
import team.serenity.model.managers.ReadOnlySerenity;
import team.serenity.model.managers.Serenity;


/**
 * Represents a storage for {@link Serenity}.
 */
public interface SerenityStorage {

    /**
     * Returns the file path of the data file.
     *
     * @return The file path of the data file.
     */
    Path getSerenityFilePath();

    /**
     * Reads Serenity data.
     *
     * @return Returns Serenity data as ReadOnlySerenity, and returns Optional.empty if storage file is not found.
     * @throws IllegalValueException Thrown if the value is invalid.
     * @throws DataConversionException Thrown if the data in storage is not in the expected format.
     */
    Optional<ReadOnlySerenity> readSerenity() throws IllegalValueException, DataConversionException;

    /**
     * @see #getSerenityFilePath()
     *
     * @param filePath The file path of the data file.
     * @return An optional ReadOnlySerenity object.
     * @throws IllegalValueException Thrown if the value is invalid.
     * @throws DataConversionException Thrown if the data in storage is not in the expected format.
     */
    Optional<ReadOnlySerenity> readSerenity(Path filePath) throws IllegalValueException, DataConversionException;

    /**
     * Saves Serenity with the given {@code ReadOnlyGroupManager}
     *
     * @param manager The ReadOnlyGroupManager involved.
     * @throws IOException Thrown if there is an input/output error.
     */
    void saveSerenity(ReadOnlyGroupManager manager) throws IOException;

}
