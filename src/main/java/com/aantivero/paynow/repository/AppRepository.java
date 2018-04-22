package com.aantivero.paynow.repository;

import com.aantivero.paynow.domain.App;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the App entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppRepository extends JpaRepository<App, Long>, JpaSpecificationExecutor<App> {

}
