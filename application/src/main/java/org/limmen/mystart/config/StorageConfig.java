package org.limmen.mystart.config;

import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.Storage;
import org.limmen.mystart.StorageProvider;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.VisitStorage;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;

@Factory
public class StorageConfig {

    private final Storage storage;

    private final UserStorage userStorage;

    private final LinkStorage linkStorage;

    private final VisitStorage visitStorage;

    @Inject
    public StorageConfig(SettingsProvider settingsConfig) {
        String storageName = settingsConfig.getProperties().getProperty("server.storage");
        this.storage = StorageProvider.getStorageByName(settingsConfig.getProperties(), storageName);

        this.userStorage = this.storage.getUserStorage();
        this.linkStorage = this.storage.getLinkStorage();
        this.visitStorage = this.storage.getVisitStorage();
    }

    @Bean(typed = UserStorage.class)
    public UserStorage getUserStorage() {
        return this.userStorage;
    }

    @Bean(typed = LinkStorage.class)
    public LinkStorage getLinkStorage() {
        return linkStorage;
    }

    @Bean(typed = VisitStorage.class)
    public VisitStorage getVisitStorage() {
        return visitStorage;
    }
}
