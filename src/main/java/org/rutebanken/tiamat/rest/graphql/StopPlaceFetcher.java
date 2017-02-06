package org.rutebanken.tiamat.rest.graphql;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.rutebanken.tiamat.dtoassembling.dto.BoundingBoxDto;
import org.rutebanken.tiamat.model.StopPlace;
import org.rutebanken.tiamat.netex.mapping.mapper.NetexIdMapper;
import org.rutebanken.tiamat.repository.StopPlaceRepository;
import org.rutebanken.tiamat.repository.StopPlaceSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import static org.rutebanken.tiamat.rest.graphql.GraphQLNames.*;

@Service("stopPlaceFetcher")
@Transactional
class StopPlaceFetcher implements DataFetcher {


    @Autowired
    private StopPlaceRepository stopPlaceRepository;

    @Override
    @Transactional
    public Object get(DataFetchingEnvironment environment) {
        StopPlaceSearch.Builder stopPlaceSearchBuilder = new StopPlaceSearch.Builder();

        Page<StopPlace> stopPlaces = null;
        if (environment.getArgument(ID) != null) {
            List<String> idList = environment.getArgument(ID);

            stopPlaceSearchBuilder.setIdList(idList
                    .stream()
                    .map(nsrId -> {
                        if(!nsrId.startsWith(NetexIdMapper.NSR)) {
                            throw new IllegalArgumentException("Id does not start with "+NetexIdMapper.NSR +": "+nsrId);
                        }
                        return nsrId;
                    })
                    .map(nsrId -> NetexIdMapper.getTiamatId(nsrId))
                    .collect(Collectors.<Long>toList()));

            stopPlaces = stopPlaceRepository.findStopPlace(stopPlaceSearchBuilder.build());
        } else if (environment.getArgument(IMPORTED_ID_QUERY) != null) {

            List<Long> stopPlaceId = stopPlaceRepository.searchByKeyValue(NetexIdMapper.ORIGINAL_ID_KEY, environment.getArgument(IMPORTED_ID_QUERY));

            if (stopPlaceId != null && !stopPlaceId.isEmpty()) {
                stopPlaceSearchBuilder.setIdList(stopPlaceId);
                stopPlaces = stopPlaceRepository.findStopPlace(stopPlaceSearchBuilder.build());
            }
        } else {
            stopPlaceSearchBuilder.setStopTypeEnumerations(environment.getArgument(STOP_TYPE));

            if (environment.getArgument(COUNTY_REF) != null) {
                stopPlaceSearchBuilder.setCountyIds(
                        Lists.transform(environment.getArgument(COUNTY_REF), Functions.toStringFunction())
                );
            }

            if (environment.getArgument(MUNICIPALITY_REF) != null) {
                stopPlaceSearchBuilder.setMunicipalityIds(
                        Lists.transform(environment.getArgument(MUNICIPALITY_REF), Functions.toStringFunction())
                );
            }

            stopPlaceSearchBuilder.setQuery(environment.getArgument(QUERY));

            PageRequest pageable = new PageRequest(environment.getArgument(PAGE), environment.getArgument(SIZE));
            stopPlaceSearchBuilder.setPageable(pageable);

            if (environment.getArgument(LONGITUDE_MIN) != null) {
                BoundingBoxDto boundingBox = new BoundingBoxDto();

                try {
                    boundingBox.xMin = ((BigDecimal) environment.getArgument(LONGITUDE_MIN)).doubleValue();
                    boundingBox.yMin = ((BigDecimal) environment.getArgument(LATITUDE_MIN)).doubleValue();
                    boundingBox.xMax = ((BigDecimal) environment.getArgument(LONGITUDE_MAX)).doubleValue();
                    boundingBox.yMax = ((BigDecimal) environment.getArgument(LATITUDE_MAX)).doubleValue();
                } catch (NullPointerException npe) {
                    RuntimeException rte = new RuntimeException(MessageFormat.format("{}, {}, {} and {} must all be set when searching within bounding box", LONGITUDE_MIN, LATITUDE_MIN, LONGITUDE_MAX, LATITUDE_MAX));
                    rte.setStackTrace(new StackTraceElement[0]);
                    throw rte;
                }

                Long ignoreStopPlaceId = null;
                if (environment.getArgument(IGNORE_STOPPLACE_ID) != null) {
                    ignoreStopPlaceId = NetexIdMapper.getTiamatId(environment.getArgument(IGNORE_STOPPLACE_ID));
                }

                stopPlaces = stopPlaceRepository.findStopPlacesWithin(boundingBox.xMin, boundingBox.yMin, boundingBox.xMax,
                        boundingBox.yMax, ignoreStopPlaceId, pageable);
            } else {
                stopPlaces = stopPlaceRepository.findStopPlace(stopPlaceSearchBuilder.build());
            }
        }
        return stopPlaces;
    }
}
