package es.projectnunsys.service.mapper;

import es.projectnunsys.domain.*;
import es.projectnunsys.service.dto.FacturaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Factura} and its DTO {@link FacturaDTO}.
 */
@Mapper(componentModel = "spring", uses = { UsuarioMapper.class })
public interface FacturaMapper extends EntityMapper<FacturaDTO, Factura> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "id")
    FacturaDTO toDto(Factura s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FacturaDTO toDtoId(Factura factura);
}
