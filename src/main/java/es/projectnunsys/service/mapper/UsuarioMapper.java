package es.projectnunsys.service.mapper;

import es.projectnunsys.domain.*;
import es.projectnunsys.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Usuario} and its DTO {@link UsuarioDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UsuarioMapper extends EntityMapper<UsuarioDTO, Usuario> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoId(Usuario usuario);
}
