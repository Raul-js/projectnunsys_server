package es.projectnunsys.repository;

import es.projectnunsys.domain.TipoProducto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoProducto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoProductoRepository extends JpaRepository<TipoProducto, Long>, JpaSpecificationExecutor<TipoProducto> {}
