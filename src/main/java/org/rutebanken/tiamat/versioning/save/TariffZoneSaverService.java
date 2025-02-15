/*
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *   https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */

package org.rutebanken.tiamat.versioning.save;


import org.rutebanken.tiamat.auth.UsernameFetcher;
import org.rutebanken.tiamat.model.TariffZone;
import org.rutebanken.tiamat.repository.TariffZoneRepository;
import org.rutebanken.tiamat.service.TariffZonesLookupService;
import org.rutebanken.tiamat.service.metrics.PrometheusMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * No history for tariff zones. Overwrites existing version for tariff zone
 */
@Service
public class TariffZoneSaverService {

    private static final Logger logger = LoggerFactory.getLogger(TariffZoneSaverService.class);

    private final TariffZoneRepository tariffZoneRepository;
    private final TariffZonesLookupService tariffZonesLookupService;
    private final UsernameFetcher usernameFetcher;
    private final PrometheusMetricsService prometheusMetricsService;

    @Autowired
    public TariffZoneSaverService(TariffZoneRepository tariffZoneRepository, TariffZonesLookupService tariffZonesLookupService, UsernameFetcher usernameFetcher, PrometheusMetricsService prometheusMetricsService) {
        this.tariffZoneRepository = tariffZoneRepository;
        this.tariffZonesLookupService = tariffZonesLookupService;
        this.usernameFetcher = usernameFetcher;
        this.prometheusMetricsService = prometheusMetricsService;
    }

    public TariffZone saveNewVersion(TariffZone existingVersion, TariffZone newVersion) {
        return saveNewVersion(newVersion);
    }

    public TariffZone saveNewVersion(TariffZone newVersion) {

        TariffZone existing = tariffZoneRepository.findFirstByNetexIdOrderByVersionDesc(newVersion.getNetexId());

        TariffZone result;
        if(existing != null) {
            BeanUtils.copyProperties(newVersion, existing, "id", "created", "version");
            existing.setValidBetween(null);
            existing.setChanged(Instant.now());
            result = tariffZoneRepository.save(existing);

        } else {
            newVersion.setCreated(Instant.now());
            newVersion.setVersion(1L);
            result = tariffZoneRepository.save(newVersion);
        }

        result.setChangedBy(usernameFetcher.getUserNameForAuthenticatedUser());

        logger.info("Saved tariff zone {}, version {}, name {}", result.getNetexId(), result.getVersion(), result.getName());

        tariffZonesLookupService.reset();
        prometheusMetricsService.registerEntitySaved(newVersion.getClass(),1L);
        return result;
    }

}
