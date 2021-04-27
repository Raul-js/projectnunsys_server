package es.projectnunsys.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.projectnunsys.IntegrationTest;
import es.projectnunsys.domain.Producto;
import es.projectnunsys.domain.TipoProducto;
import es.projectnunsys.repository.TipoProductoRepository;
import es.projectnunsys.service.criteria.TipoProductoCriteria;
import es.projectnunsys.service.dto.TipoProductoDTO;
import es.projectnunsys.service.mapper.TipoProductoMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TipoProductoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoProductoResourceIT {

    private static final String DEFAULT_NOMBRE_TIPO_PRODUCTO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_TIPO_PRODUCTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private TipoProductoMapper tipoProductoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoProductoMockMvc;

    private TipoProducto tipoProducto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoProducto createEntity(EntityManager em) {
        TipoProducto tipoProducto = new TipoProducto().nombreTipoProducto(DEFAULT_NOMBRE_TIPO_PRODUCTO);
        return tipoProducto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoProducto createUpdatedEntity(EntityManager em) {
        TipoProducto tipoProducto = new TipoProducto().nombreTipoProducto(UPDATED_NOMBRE_TIPO_PRODUCTO);
        return tipoProducto;
    }

    @BeforeEach
    public void initTest() {
        tipoProducto = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoProducto() throws Exception {
        int databaseSizeBeforeCreate = tipoProductoRepository.findAll().size();
        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);
        restTipoProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeCreate + 1);
        TipoProducto testTipoProducto = tipoProductoList.get(tipoProductoList.size() - 1);
        assertThat(testTipoProducto.getNombreTipoProducto()).isEqualTo(DEFAULT_NOMBRE_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void createTipoProductoWithExistingId() throws Exception {
        // Create the TipoProducto with an existing ID
        tipoProducto.setId(1L);
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        int databaseSizeBeforeCreate = tipoProductoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTipoProductos() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoProducto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreTipoProducto").value(hasItem(DEFAULT_NOMBRE_TIPO_PRODUCTO)));
    }

    @Test
    @Transactional
    void getTipoProducto() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get the tipoProducto
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoProducto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoProducto.getId().intValue()))
            .andExpect(jsonPath("$.nombreTipoProducto").value(DEFAULT_NOMBRE_TIPO_PRODUCTO));
    }

    @Test
    @Transactional
    void getTipoProductosByIdFiltering() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        Long id = tipoProducto.getId();

        defaultTipoProductoShouldBeFound("id.equals=" + id);
        defaultTipoProductoShouldNotBeFound("id.notEquals=" + id);

        defaultTipoProductoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTipoProductoShouldNotBeFound("id.greaterThan=" + id);

        defaultTipoProductoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTipoProductoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNombreTipoProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where nombreTipoProducto equals to DEFAULT_NOMBRE_TIPO_PRODUCTO
        defaultTipoProductoShouldBeFound("nombreTipoProducto.equals=" + DEFAULT_NOMBRE_TIPO_PRODUCTO);

        // Get all the tipoProductoList where nombreTipoProducto equals to UPDATED_NOMBRE_TIPO_PRODUCTO
        defaultTipoProductoShouldNotBeFound("nombreTipoProducto.equals=" + UPDATED_NOMBRE_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNombreTipoProductoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where nombreTipoProducto not equals to DEFAULT_NOMBRE_TIPO_PRODUCTO
        defaultTipoProductoShouldNotBeFound("nombreTipoProducto.notEquals=" + DEFAULT_NOMBRE_TIPO_PRODUCTO);

        // Get all the tipoProductoList where nombreTipoProducto not equals to UPDATED_NOMBRE_TIPO_PRODUCTO
        defaultTipoProductoShouldBeFound("nombreTipoProducto.notEquals=" + UPDATED_NOMBRE_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNombreTipoProductoIsInShouldWork() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where nombreTipoProducto in DEFAULT_NOMBRE_TIPO_PRODUCTO or UPDATED_NOMBRE_TIPO_PRODUCTO
        defaultTipoProductoShouldBeFound("nombreTipoProducto.in=" + DEFAULT_NOMBRE_TIPO_PRODUCTO + "," + UPDATED_NOMBRE_TIPO_PRODUCTO);

        // Get all the tipoProductoList where nombreTipoProducto equals to UPDATED_NOMBRE_TIPO_PRODUCTO
        defaultTipoProductoShouldNotBeFound("nombreTipoProducto.in=" + UPDATED_NOMBRE_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNombreTipoProductoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where nombreTipoProducto is not null
        defaultTipoProductoShouldBeFound("nombreTipoProducto.specified=true");

        // Get all the tipoProductoList where nombreTipoProducto is null
        defaultTipoProductoShouldNotBeFound("nombreTipoProducto.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoProductosByNombreTipoProductoContainsSomething() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where nombreTipoProducto contains DEFAULT_NOMBRE_TIPO_PRODUCTO
        defaultTipoProductoShouldBeFound("nombreTipoProducto.contains=" + DEFAULT_NOMBRE_TIPO_PRODUCTO);

        // Get all the tipoProductoList where nombreTipoProducto contains UPDATED_NOMBRE_TIPO_PRODUCTO
        defaultTipoProductoShouldNotBeFound("nombreTipoProducto.contains=" + UPDATED_NOMBRE_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllTipoProductosByNombreTipoProductoNotContainsSomething() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList where nombreTipoProducto does not contain DEFAULT_NOMBRE_TIPO_PRODUCTO
        defaultTipoProductoShouldNotBeFound("nombreTipoProducto.doesNotContain=" + DEFAULT_NOMBRE_TIPO_PRODUCTO);

        // Get all the tipoProductoList where nombreTipoProducto does not contain UPDATED_NOMBRE_TIPO_PRODUCTO
        defaultTipoProductoShouldBeFound("nombreTipoProducto.doesNotContain=" + UPDATED_NOMBRE_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllTipoProductosByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);
        Producto producto = ProductoResourceIT.createEntity(em);
        em.persist(producto);
        em.flush();
        tipoProducto.addProducto(producto);
        tipoProductoRepository.saveAndFlush(tipoProducto);
        Long productoId = producto.getId();

        // Get all the tipoProductoList where producto equals to productoId
        defaultTipoProductoShouldBeFound("productoId.equals=" + productoId);

        // Get all the tipoProductoList where producto equals to (productoId + 1)
        defaultTipoProductoShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoProductoShouldBeFound(String filter) throws Exception {
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoProducto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreTipoProducto").value(hasItem(DEFAULT_NOMBRE_TIPO_PRODUCTO)));

        // Check, that the count call also returns 1
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoProductoShouldNotBeFound(String filter) throws Exception {
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoProducto() throws Exception {
        // Get the tipoProducto
        restTipoProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoProducto() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();

        // Update the tipoProducto
        TipoProducto updatedTipoProducto = tipoProductoRepository.findById(tipoProducto.getId()).get();
        // Disconnect from session so that the updates on updatedTipoProducto are not directly saved in db
        em.detach(updatedTipoProducto);
        updatedTipoProducto.nombreTipoProducto(UPDATED_NOMBRE_TIPO_PRODUCTO);
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(updatedTipoProducto);

        restTipoProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoProductoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
        TipoProducto testTipoProducto = tipoProductoList.get(tipoProductoList.size() - 1);
        assertThat(testTipoProducto.getNombreTipoProducto()).isEqualTo(UPDATED_NOMBRE_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void putNonExistingTipoProducto() throws Exception {
        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();
        tipoProducto.setId(count.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoProductoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoProducto() throws Exception {
        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();
        tipoProducto.setId(count.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoProducto() throws Exception {
        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();
        tipoProducto.setId(count.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoProductoWithPatch() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();

        // Update the tipoProducto using partial update
        TipoProducto partialUpdatedTipoProducto = new TipoProducto();
        partialUpdatedTipoProducto.setId(tipoProducto.getId());

        partialUpdatedTipoProducto.nombreTipoProducto(UPDATED_NOMBRE_TIPO_PRODUCTO);

        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoProducto))
            )
            .andExpect(status().isOk());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
        TipoProducto testTipoProducto = tipoProductoList.get(tipoProductoList.size() - 1);
        assertThat(testTipoProducto.getNombreTipoProducto()).isEqualTo(UPDATED_NOMBRE_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void fullUpdateTipoProductoWithPatch() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();

        // Update the tipoProducto using partial update
        TipoProducto partialUpdatedTipoProducto = new TipoProducto();
        partialUpdatedTipoProducto.setId(tipoProducto.getId());

        partialUpdatedTipoProducto.nombreTipoProducto(UPDATED_NOMBRE_TIPO_PRODUCTO);

        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoProducto))
            )
            .andExpect(status().isOk());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
        TipoProducto testTipoProducto = tipoProductoList.get(tipoProductoList.size() - 1);
        assertThat(testTipoProducto.getNombreTipoProducto()).isEqualTo(UPDATED_NOMBRE_TIPO_PRODUCTO);
    }

    @Test
    @Transactional
    void patchNonExistingTipoProducto() throws Exception {
        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();
        tipoProducto.setId(count.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoProductoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoProducto() throws Exception {
        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();
        tipoProducto.setId(count.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoProducto() throws Exception {
        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();
        tipoProducto.setId(count.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoProducto() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        int databaseSizeBeforeDelete = tipoProductoRepository.findAll().size();

        // Delete the tipoProducto
        restTipoProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoProducto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
