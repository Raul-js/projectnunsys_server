package es.projectnunsys.service.mapper;

import es.projectnunsys.domain.*;
import es.projectnunsys.service.dto.ProductoDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring", uses = { CarritoMapper.class, TipoProductoMapper.class })
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {
    @Mapping(target = "carritos", source = "carritos", qualifiedByName = "idSet")
    @Mapping(target = "tipoproducto", source = "tipoproducto", qualifiedByName = "id")
    ProductoDTO toDto(Producto s);

    @Mapping(target = "removeCarrito", ignore = true)
    Producto toEntity(ProductoDTO productoDTO);
}
