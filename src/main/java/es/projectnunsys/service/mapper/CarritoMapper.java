package es.projectnunsys.service.mapper;

import es.projectnunsys.domain.*;
import es.projectnunsys.service.dto.CarritoDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Carrito} and its DTO {@link CarritoDTO}.
 */
@Mapper(componentModel = "spring", uses = { UsuarioMapper.class })
public interface CarritoMapper extends EntityMapper<CarritoDTO, Carrito> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "id")
    CarritoDTO toDto(Carrito s);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<CarritoDTO> toDtoIdSet(Set<Carrito> carrito);
}
