package es.projectnunsys.web.rest;

import es.projectnunsys.repository.TipoProductoRepository;
import es.projectnunsys.service.TipoProductoQueryService;
import es.projectnunsys.service.TipoProductoService;
import es.projectnunsys.service.criteria.TipoProductoCriteria;
import es.projectnunsys.service.dto.TipoProductoDTO;
import es.projectnunsys.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link es.projectnunsys.domain.TipoProducto}.
 */
@RestController
@RequestMapping("/api")
public class TipoProductoResource {

    private final Logger log = LoggerFactory.getLogger(TipoProductoResource.class);

    private static final String ENTITY_NAME = "tipoProducto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoProductoService tipoProductoService;

    private final TipoProductoRepository tipoProductoRepository;

    private final TipoProductoQueryService tipoProductoQueryService;

    public TipoProductoResource(
        TipoProductoService tipoProductoService,
        TipoProductoRepository tipoProductoRepository,
        TipoProductoQueryService tipoProductoQueryService
    ) {
        this.tipoProductoService = tipoProductoService;
        this.tipoProductoRepository = tipoProductoRepository;
        this.tipoProductoQueryService = tipoProductoQueryService;
    }

    /**
     * {@code POST  /tipo-productos} : Create a new tipoProducto.
     *
     * @param tipoProductoDTO the tipoProductoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoProductoDTO, or with status {@code 400 (Bad Request)} if the tipoProducto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-productos")
    public ResponseEntity<TipoProductoDTO> createTipoProducto(@RequestBody TipoProductoDTO tipoProductoDTO) throws URISyntaxException {
        log.debug("REST request to save TipoProducto : {}", tipoProductoDTO);
        if (tipoProductoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoProducto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoProductoDTO result = tipoProductoService.save(tipoProductoDTO);
        return ResponseEntity
            .created(new URI("/api/tipo-productos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-productos/:id} : Updates an existing tipoProducto.
     *
     * @param id the id of the tipoProductoDTO to save.
     * @param tipoProductoDTO the tipoProductoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoProductoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoProductoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoProductoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-productos/{id}")
    public ResponseEntity<TipoProductoDTO> updateTipoProducto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoProductoDTO tipoProductoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TipoProducto : {}, {}", id, tipoProductoDTO);
        if (tipoProductoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoProductoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoProductoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoProductoDTO result = tipoProductoService.save(tipoProductoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoProductoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-productos/:id} : Partial updates given fields of an existing tipoProducto, field will ignore if it is null
     *
     * @param id the id of the tipoProductoDTO to save.
     * @param tipoProductoDTO the tipoProductoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoProductoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoProductoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoProductoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoProductoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-productos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TipoProductoDTO> partialUpdateTipoProducto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoProductoDTO tipoProductoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoProducto partially : {}, {}", id, tipoProductoDTO);
        if (tipoProductoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoProductoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoProductoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoProductoDTO> result = tipoProductoService.partialUpdate(tipoProductoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoProductoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-productos} : get all the tipoProductos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoProductos in body.
     */
    @GetMapping("/tipo-productos")
    public ResponseEntity<List<TipoProductoDTO>> getAllTipoProductos(TipoProductoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TipoProductos by criteria: {}", criteria);
        Page<TipoProductoDTO> page = tipoProductoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-productos/count} : count all the tipoProductos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tipo-productos/count")
    public ResponseEntity<Long> countTipoProductos(TipoProductoCriteria criteria) {
        log.debug("REST request to count TipoProductos by criteria: {}", criteria);
        return ResponseEntity.ok().body(tipoProductoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipo-productos/:id} : get the "id" tipoProducto.
     *
     * @param id the id of the tipoProductoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoProductoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-productos/{id}")
    public ResponseEntity<TipoProductoDTO> getTipoProducto(@PathVariable Long id) {
        log.debug("REST request to get TipoProducto : {}", id);
        Optional<TipoProductoDTO> tipoProductoDTO = tipoProductoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoProductoDTO);
    }

    /**
     * {@code DELETE  /tipo-productos/:id} : delete the "id" tipoProducto.
     *
     * @param id the id of the tipoProductoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-productos/{id}")
    public ResponseEntity<Void> deleteTipoProducto(@PathVariable Long id) {
        log.debug("REST request to delete TipoProducto : {}", id);
        tipoProductoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
