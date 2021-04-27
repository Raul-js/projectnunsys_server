package es.projectnunsys.service;

import es.projectnunsys.service.dto.TipoProductoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link es.projectnunsys.domain.TipoProducto}.
 */
public interface TipoProductoService {
    /**
     * Save a tipoProducto.
     *
     * @param tipoProductoDTO the entity to save.
     * @return the persisted entity.
     */
    TipoProductoDTO save(TipoProductoDTO tipoProductoDTO);

    /**
     * Partially updates a tipoProducto.
     *
     * @param tipoProductoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoProductoDTO> partialUpdate(TipoProductoDTO tipoProductoDTO);

    /**
     * Get all the tipoProductos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoProductoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tipoProducto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoProductoDTO> findOne(Long id);

    /**
     * Delete the "id" tipoProducto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
