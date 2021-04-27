package es.projectnunsys.service.mapper;

import es.projectnunsys.domain.*;
import es.projectnunsys.service.dto.CompraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Compra} and its DTO {@link CompraDTO}.
 */
@Mapper(componentModel = "spring", uses = { FacturaMapper.class })
public interface CompraMapper extends EntityMapper<CompraDTO, Compra> {
    @Mapping(target = "factura", source = "factura", qualifiedByName = "id")
    CompraDTO toDto(Compra s);
}
