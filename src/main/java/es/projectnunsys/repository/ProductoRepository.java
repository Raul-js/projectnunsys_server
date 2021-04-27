package es.projectnunsys.repository;

import es.projectnunsys.domain.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Producto entity.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
    @Query(
        value = "select distinct producto from Producto producto left join fetch producto.carritos",
        countQuery = "select count(distinct producto) from Producto producto"
    )
    Page<Producto> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct producto from Producto producto left join fetch producto.carritos")
    List<Producto> findAllWithEagerRelationships();

    @Query("select producto from Producto producto left join fetch producto.carritos where producto.id =:id")
    Optional<Producto> findOneWithEagerRelationships(@Param("id") Long id);
}
