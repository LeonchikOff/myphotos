package org.example.common.converter;

import java.util.List;

public interface ModelConverter {

    <SourceType, DestinationType> DestinationType convert(SourceType source,
                                                          Class<DestinationType> destinationTypeClass);

    <SourceType, DestinationType> List<DestinationType> convertList(List<SourceType> sourceList,
                                                                    Class<DestinationType> destinationTypeClass);

}
