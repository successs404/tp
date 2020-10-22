package team.serenity;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import team.serenity.commons.core.Config;
import team.serenity.commons.core.LogsCenter;
import team.serenity.commons.core.Version;
import team.serenity.commons.exceptions.DataConversionException;
import team.serenity.commons.util.ConfigUtil;
import team.serenity.commons.util.StringUtil;
import team.serenity.logic.Logic;
import team.serenity.logic.LogicManager;
import team.serenity.model.Model;
import team.serenity.model.ModelManager;
import team.serenity.model.ReadOnlySerenity;
import team.serenity.model.ReadOnlyUserPrefs;
import team.serenity.model.Serenity;
import team.serenity.model.UserPrefs;
import team.serenity.storage.JsonSerenityStorage;
import team.serenity.storage.JsonUserPrefsStorage;
import team.serenity.storage.SerenityStorage;
import team.serenity.storage.Storage;
import team.serenity.storage.StorageManager;
import team.serenity.storage.UserPrefsStorage;
import team.serenity.ui.Ui;
import team.serenity.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 6, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info(
            "=============================[ Initializing Serenity ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        SerenityStorage serenityStorage = new JsonSerenityStorage(userPrefs.getSerenityFilePath());
        storage = new StorageManager(serenityStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s address book and {@code userPrefs}. <br> The
     * data from the sample address book will be used instead if {@code storage}'s address book is not found, or an
     * empty address book will be used instead if errors occur when reading {@code storage}'s address book.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        ReadOnlySerenity serenity = initSerenity(storage);
        return new ModelManager(serenity, userPrefs);
    }

    private ReadOnlySerenity initSerenity(Storage storage) {
        ReadOnlySerenity serenity;
        serenity = new Serenity();
        /*
        try {
            Optional<ReadOnlySerenity> serenityOptional = storage.readSerenity();
            if (serenityOptional.isEmpty()) {
                logger.info("Data file not found. Will be starting with a sample Serenity.");
            }
            serenity =
                serenityOptional.orElseGet(SampleDataUtil::getSampleSerenity);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty"
                + "Serenity.");
            serenity = new Serenity();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty "
                + "Serenity.");
            serenity = new Serenity();
        }

        try {
            storage.saveSerenity(serenity);
            logger.info("Saving initial data of Serenity.");
        } catch (IOException e) {
            logger.warning("Problem while saving to the file.");
        }

         */

        return serenity;
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br> The default file path {@code
     * Config#DEFAULT_CONFIG_FILE} will be used instead if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path, or a new {@code UserPrefs}
     * with default configuration if errors occur when reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning(
                "Problem while reading from the file. Will be starting with an empty Serenity");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting Serenity " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info(
            "============================ [ Stopping Serenity ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
