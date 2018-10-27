package com.zsoft.service.extension;

import com.zsoft.domain.extension.PersistentConfiguration;
import com.zsoft.repository.extension.PersistentConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Transactional
public class PersistentConfigurationService {

    private final Logger log = LoggerFactory.getLogger(PersistentConfiguration.class);

    private PersistentConfigurationRepository repository;

    public PersistentConfigurationService(PersistentConfigurationRepository repository) {
        this.repository = repository;
    }

    /*
     * Add NEW CONFIGURATION
     */
    public PersistentConfiguration add(PersistentConfiguration configuration) {
        log.debug("Add a new persistent configuration : {}", configuration);
        return repository.saveAndFlush(configuration);
    }

    /*
     * UPDATE A CONFIGURATION
     */
    public PersistentConfiguration update(PersistentConfiguration configuration) {
        return repository
            .findById(configuration.getId())
            .map(currentConfiguration -> {
                log.debug("Update an persistent configuration : {}", configuration);
                currentConfiguration.setValue(configuration.getValue());
                return currentConfiguration;
            })
            .orElseThrow(() -> new IllegalArgumentException("Illegal Argument, Persistent Configuration not found !"));
    }

    /*
    * DELETE Services
    */
    public void delete(Long id) {
        repository
            .findById(id)
            .ifPresent(configuration ->  {
                log.debug("Delete an persistent configuration : {}", configuration);
                repository.delete(configuration);
            });
    }

    public void deleteByEntity(String entity_name) {
        repository
            .findAllByEntity(entity_name)
            .forEach(configuration ->  {
                log.debug("Delete an persistent configuration : {}", configuration);
                repository.delete(configuration);
            });
    }

    public void deleteByKey(String key) {
        repository
            .findAllByKey(key)
            .forEach(configuration ->  {
                log.debug("Delete an persistent configuration : {}", configuration);
                repository.delete(configuration);
            });
    }

    public void deleteByEntityAndEntityId(String entity_name, Long entity_id) {
        repository
            .findAllByEntityAndEntityId(entity_name, entity_id)
            .forEach(configuration ->  {
                log.debug("Delete an persistent configuration : {}", configuration);
                if( configuration.getKey().startsWith("@") ){
                    deleteByEntityAndEntityId(configuration.getKey(), Long.parseLong(configuration.getValue()));
                }
                repository.delete(configuration);
            });
    }

    public void deleteByEntityAndEntityIdAndKey(String entity_name, Long entity_id, String key) {
        repository
            .findAllByEntityAndEntityIdAndKey(entity_name, entity_id, key)
            .forEach(configuration ->  {
                log.debug("Delete an persistent configuration : {}", configuration);
                repository.delete(configuration);
            });
    }

    public void deleteByEntityAndKey(String entity_name, String key) {
        repository
            .findAllByEntityAndKey(entity_name,key)
            .forEach(configuration ->  {
                log.debug("Delete an persistent configuration : {}", configuration);
                repository.delete(configuration);
            });
    }

    /*
     * GET CONFIGURATION SERVICES
     */

    public Optional<PersistentConfiguration> getById(Long id) {
        return repository.findById(id);
    }

    public List<PersistentConfiguration> getAll() {
        return repository.findAll();
    }

    public Stream<PersistentConfiguration> getByEntityName(String entity_name) {
        return repository.findAllByEntity(entity_name);
    }

    public Stream<PersistentConfiguration> getByEntityAndEntityId(String entity_name, Long entity_id) {
        return repository.findAllByEntityAndEntityId(entity_name, entity_id);
    }

    public Stream<PersistentConfiguration> getByEntityAndEntityIdAndKey(String entity_name, Long entity_id, String key) {
        return repository.findAllByEntityAndEntityIdAndKey(entity_name, entity_id, key);
    }

    public Stream<PersistentConfiguration> getByEntityAndKey(String entity_name, String key) {
        return repository.findAllByEntityAndKey(entity_name, key);
    }

    public Stream<PersistentConfiguration> getByKey(String key) {
        return repository.findAllByKey(key);
    }
}
