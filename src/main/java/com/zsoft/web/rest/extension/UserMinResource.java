package com.zsoft.web.rest.extension;

import com.codahale.metrics.annotation.Timed;
import com.zsoft.security.AuthoritiesConstants;
import com.zsoft.service.dto.extension.UserMinDTO;
import com.zsoft.service.extension.UserMinService;
import com.zsoft.service.mapper.extension.UserMinMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing appointments.
 */
@RestController
@RequestMapping("/api")
public class UserMinResource {
    private final Logger log = LoggerFactory.getLogger(UserMinResource.class);

    private final UserMinService userMinService;

    private UserMinMapper mapper = Mappers.getMapper(UserMinMapper.class);

    public UserMinResource(UserMinService userMinService) {
        this.userMinService = userMinService;
    }

    /**
     * GET /usersmin/:user_id : get an User Minimal by ID.
     *
     * @param user_id the ID of the User Minimal to find
     * @return the ResponseEntity with status 200 (OK) and with body the User Minimal, or with status 404 (Not Found)
     */
    @GetMapping("/usersmin/{user_id}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<UserMinDTO> findAppointment(@PathVariable Long user_id) {
        log.debug("REST request to get Appointment : {}", user_id);
        return ResponseUtil.wrapOrNotFound(
            userMinService.find(user_id)
        );
    }
}
