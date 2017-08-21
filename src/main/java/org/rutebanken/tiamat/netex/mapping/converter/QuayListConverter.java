package org.rutebanken.tiamat.netex.mapping.converter;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.rutebanken.netex.model.Quays_RelStructure;
import org.rutebanken.tiamat.model.Quay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class QuayListConverter extends BidirectionalConverter<Set<Quay>, Quays_RelStructure> {

    private static final Logger logger = LoggerFactory.getLogger(QuayListConverter.class);

    @Override
    public Quays_RelStructure convertTo(Set<Quay> quays, Type<Quays_RelStructure> type, MappingContext mappingContext) {
        if(quays == null || quays.isEmpty()) {
            return null;
        }

        Quays_RelStructure quays_relStructure = new Quays_RelStructure();

        logger.debug("Mapping {} quays to netex", quays != null ? quays.size() : 0);

        quays.forEach(quay -> {
            org.rutebanken.netex.model.Quay netexQuay = mapperFacade.map(quay, org.rutebanken.netex.model.Quay.class);
            quays_relStructure.getQuayRefOrQuay().add(netexQuay);
        });
        return quays_relStructure;
    }

    @Override
    public Set<Quay> convertFrom(Quays_RelStructure quays_relStructure, Type<Set<Quay>> type, MappingContext mappingContext) {
        logger.debug("Mapping {} quays to internal model", quays_relStructure != null ? quays_relStructure.getQuayRefOrQuay().size() : 0);
        Set<Quay> quays = new HashSet<>();
        if(quays_relStructure.getQuayRefOrQuay() != null) {
            quays_relStructure.getQuayRefOrQuay().stream()
                    .filter(object -> object instanceof org.rutebanken.netex.model.Quay)
                    .map(object -> ((org.rutebanken.netex.model.Quay) object))
                    .map(netexQuay -> {
                        Quay tiamatQuay = mapperFacade.map(netexQuay, Quay.class);
                        return tiamatQuay;
                    })
                    .forEach(quay -> quays.add(quay));
        }
        
        return quays;
    }
}
