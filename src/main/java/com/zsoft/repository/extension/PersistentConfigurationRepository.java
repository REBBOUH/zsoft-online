package com.zsoft.repository.extension;

import com.zsoft.domain.extension.PersistentConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface PersistentConfigurationRepository extends JpaRepository<PersistentConfiguration, Long> {

    Stream<PersistentConfiguration> findAllByEntity(String entityname);

    Stream<PersistentConfiguration> findAllByEntityAndEntityId(String entity_name, Long entity_id);

    Stream<PersistentConfiguration> findAllByEntityAndEntityIdAndKey(String entity_name, Long entity_id, String key);

    Stream<PersistentConfiguration> findAllByEntityAndKey(String entity_name, String key);

    Stream<PersistentConfiguration> findAllByKey(String key);
}
