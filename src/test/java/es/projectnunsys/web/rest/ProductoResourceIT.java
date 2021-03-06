package es.projectnunsys.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.projectnunsys.IntegrationTest;
import es.projectnunsys.domain.Carrito;
import es.projectnunsys.domain.Producto;
import es.projectnunsys.domain.TipoProducto;
import es.projectnunsys.repository.ProductoRepository;
import es.projectnunsys.service.ProductoService;
import es.projectnunsys.service.criteria.ProductoCriteria;
import es.projectnunsys.service.dto.ProductoDTO;
import es.projectnunsys.service.mapper.ProductoMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ProductoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductoResourceIT {

    private static final String DEFAULT_NOMBRE_PRODUCTO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_PRODUCTO = "BBBBBBBBBB";

    private static final String DEFAULT_INGREDIENTES = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENTES = "BBBBBBBBBB";

    private static final Integer DEFAULT_CALORIAS = 1;
    private static final Integer UPDATED_CALORIAS = 2;
    private static final Integer SMALLER_CALORIAS = 1 - 1;

    private static final byte[] DEFAULT_IMAGEN = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEN = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGEN_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEN_CONTENT_TYPE = "image/png";

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;
    private static final Float SMALLER_PRECIO = 1F - 1F;

    private static final Integer DEFAULT_EXISTENCIAS = 1;
    private static final Integer UPDATED_EXISTENCIAS = 2;
    private static final Integer SMALLER_EXISTENCIAS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductoRepository productoRepository;

    @Mock
    private ProductoRepository productoRepositoryMock;

    @Autowired
    private ProductoMapper productoMapper;

    @Mock
    private ProductoService productoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductoMockMvc;

    private Producto producto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createEntity(EntityManager em) {
        Producto producto = new Producto()
            .nombreProducto(DEFAULT_NOMBRE_PRODUCTO)
            .ingredientes(DEFAULT_INGREDIENTES)
            .calorias(DEFAULT_CALORIAS)
            .imagen(DEFAULT_IMAGEN)
            .imagenContentType(DEFAULT_IMAGEN_CONTENT_TYPE)
            .precio(DEFAULT_PRECIO)
            .existencias(DEFAULT_EXISTENCIAS);
        return producto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createUpdatedEntity(EntityManager em) {
        Producto producto = new Producto()
            .nombreProducto(UPDATED_NOMBRE_PRODUCTO)
            .ingredientes(UPDATED_INGREDIENTES)
            .calorias(UPDATED_CALORIAS)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .precio(UPDATED_PRECIO)
            .existencias(UPDATED_EXISTENCIAS);
        return producto;
    }

    @BeforeEach
    public void initTest() {
        producto = createEntity(em);
    }

    @Test
    @Transactional
    void createProducto() throws Exception {
        int databaseSizeBeforeCreate = productoRepository.findAll().size();
        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);
        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isCreated());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeCreate + 1);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getNombreProducto()).isEqualTo(DEFAULT_NOMBRE_PRODUCTO);
        assertThat(testProducto.getIngredientes()).isEqualTo(DEFAULT_INGREDIENTES);
        assertThat(testProducto.getCalorias()).isEqualTo(DEFAULT_CALORIAS);
        assertThat(testProducto.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testProducto.getImagenContentType()).isEqualTo(DEFAULT_IMAGEN_CONTENT_TYPE);
        assertThat(testProducto.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testProducto.getExistencias()).isEqualTo(DEFAULT_EXISTENCIAS);
    }

    @Test
    @Transactional
    void createProductoWithExistingId() throws Exception {
        // Create the Producto with an existing ID
        producto.setId(1L);
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        int databaseSizeBeforeCreate = productoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductos() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreProducto").value(hasItem(DEFAULT_NOMBRE_PRODUCTO)))
            .andExpect(jsonPath("$.[*].ingredientes").value(hasItem(DEFAULT_INGREDIENTES)))
            .andExpect(jsonPath("$.[*].calorias").value(hasItem(DEFAULT_CALORIAS)))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].existencias").value(hasItem(DEFAULT_EXISTENCIAS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductosWithEagerRelationshipsIsEnabled() throws Exception {
        when(productoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProducto() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get the producto
        restProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, producto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(producto.getId().intValue()))
            .andExpect(jsonPath("$.nombreProducto").value(DEFAULT_NOMBRE_PRODUCTO))
            .andExpect(jsonPath("$.ingredientes").value(DEFAULT_INGREDIENTES))
            .andExpect(jsonPath("$.calorias").value(DEFAULT_CALORIAS))
            .andExpect(jsonPath("$.imagenContentType").value(DEFAULT_IMAGEN_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagen").value(Base64Utils.encodeToString(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()))
            .andExpect(jsonPath("$.existencias").value(DEFAULT_EXISTENCIAS));
    }

    @Test
    @Transactional
    void getProductosByIdFiltering() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        Long id = producto.getId();

        defaultProductoShouldBeFound("id.equals=" + id);
        defaultProductoShouldNotBeFound("id.notEquals=" + id);

        defaultProductoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductoShouldNotBeFound("id.greaterThan=" + id);

        defaultProductoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductosByNombreProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombreProducto equals to DEFAULT_NOMBRE_PRODUCTO
        defaultProductoShouldBeFound("nombreProducto.equals=" + DEFAULT_NOMBRE_PRODUCTO);

        // Get all the productoList where nombreProducto equals to UPDATED_NOMBRE_PRODUCTO
        defaultProductoShouldNotBeFound("nombreProducto.equals=" + UPDATED_NOMBRE_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllProductosByNombreProductoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombreProducto not equals to DEFAULT_NOMBRE_PRODUCTO
        defaultProductoShouldNotBeFound("nombreProducto.notEquals=" + DEFAULT_NOMBRE_PRODUCTO);

        // Get all the productoList where nombreProducto not equals to UPDATED_NOMBRE_PRODUCTO
        defaultProductoShouldBeFound("nombreProducto.notEquals=" + UPDATED_NOMBRE_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllProductosByNombreProductoIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombreProducto in DEFAULT_NOMBRE_PRODUCTO or UPDATED_NOMBRE_PRODUCTO
        defaultProductoShouldBeFound("nombreProducto.in=" + DEFAULT_NOMBRE_PRODUCTO + "," + UPDATED_NOMBRE_PRODUCTO);

        // Get all the productoList where nombreProducto equals to UPDATED_NOMBRE_PRODUCTO
        defaultProductoShouldNotBeFound("nombreProducto.in=" + UPDATED_NOMBRE_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllProductosByNombreProductoIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombreProducto is not null
        defaultProductoShouldBeFound("nombreProducto.specified=true");

        // Get all the productoList where nombreProducto is null
        defaultProductoShouldNotBeFound("nombreProducto.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByNombreProductoContainsSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombreProducto contains DEFAULT_NOMBRE_PRODUCTO
        defaultProductoShouldBeFound("nombreProducto.contains=" + DEFAULT_NOMBRE_PRODUCTO);

        // Get all the productoList where nombreProducto contains UPDATED_NOMBRE_PRODUCTO
        defaultProductoShouldNotBeFound("nombreProducto.contains=" + UPDATED_NOMBRE_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllProductosByNombreProductoNotContainsSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombreProducto does not contain DEFAULT_NOMBRE_PRODUCTO
        defaultProductoShouldNotBeFound("nombreProducto.doesNotContain=" + DEFAULT_NOMBRE_PRODUCTO);

        // Get all the productoList where nombreProducto does not contain UPDATED_NOMBRE_PRODUCTO
        defaultProductoShouldBeFound("nombreProducto.doesNotContain=" + UPDATED_NOMBRE_PRODUCTO);
    }

    @Test
    @Transactional
    void getAllProductosByIngredientesIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where ingredientes equals to DEFAULT_INGREDIENTES
        defaultProductoShouldBeFound("ingredientes.equals=" + DEFAULT_INGREDIENTES);

        // Get all the productoList where ingredientes equals to UPDATED_INGREDIENTES
        defaultProductoShouldNotBeFound("ingredientes.equals=" + UPDATED_INGREDIENTES);
    }

    @Test
    @Transactional
    void getAllProductosByIngredientesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where ingredientes not equals to DEFAULT_INGREDIENTES
        defaultProductoShouldNotBeFound("ingredientes.notEquals=" + DEFAULT_INGREDIENTES);

        // Get all the productoList where ingredientes not equals to UPDATED_INGREDIENTES
        defaultProductoShouldBeFound("ingredientes.notEquals=" + UPDATED_INGREDIENTES);
    }

    @Test
    @Transactional
    void getAllProductosByIngredientesIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where ingredientes in DEFAULT_INGREDIENTES or UPDATED_INGREDIENTES
        defaultProductoShouldBeFound("ingredientes.in=" + DEFAULT_INGREDIENTES + "," + UPDATED_INGREDIENTES);

        // Get all the productoList where ingredientes equals to UPDATED_INGREDIENTES
        defaultProductoShouldNotBeFound("ingredientes.in=" + UPDATED_INGREDIENTES);
    }

    @Test
    @Transactional
    void getAllProductosByIngredientesIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where ingredientes is not null
        defaultProductoShouldBeFound("ingredientes.specified=true");

        // Get all the productoList where ingredientes is null
        defaultProductoShouldNotBeFound("ingredientes.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByIngredientesContainsSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where ingredientes contains DEFAULT_INGREDIENTES
        defaultProductoShouldBeFound("ingredientes.contains=" + DEFAULT_INGREDIENTES);

        // Get all the productoList where ingredientes contains UPDATED_INGREDIENTES
        defaultProductoShouldNotBeFound("ingredientes.contains=" + UPDATED_INGREDIENTES);
    }

    @Test
    @Transactional
    void getAllProductosByIngredientesNotContainsSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where ingredientes does not contain DEFAULT_INGREDIENTES
        defaultProductoShouldNotBeFound("ingredientes.doesNotContain=" + DEFAULT_INGREDIENTES);

        // Get all the productoList where ingredientes does not contain UPDATED_INGREDIENTES
        defaultProductoShouldBeFound("ingredientes.doesNotContain=" + UPDATED_INGREDIENTES);
    }

    @Test
    @Transactional
    void getAllProductosByCaloriasIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where calorias equals to DEFAULT_CALORIAS
        defaultProductoShouldBeFound("calorias.equals=" + DEFAULT_CALORIAS);

        // Get all the productoList where calorias equals to UPDATED_CALORIAS
        defaultProductoShouldNotBeFound("calorias.equals=" + UPDATED_CALORIAS);
    }

    @Test
    @Transactional
    void getAllProductosByCaloriasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where calorias not equals to DEFAULT_CALORIAS
        defaultProductoShouldNotBeFound("calorias.notEquals=" + DEFAULT_CALORIAS);

        // Get all the productoList where calorias not equals to UPDATED_CALORIAS
        defaultProductoShouldBeFound("calorias.notEquals=" + UPDATED_CALORIAS);
    }

    @Test
    @Transactional
    void getAllProductosByCaloriasIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where calorias in DEFAULT_CALORIAS or UPDATED_CALORIAS
        defaultProductoShouldBeFound("calorias.in=" + DEFAULT_CALORIAS + "," + UPDATED_CALORIAS);

        // Get all the productoList where calorias equals to UPDATED_CALORIAS
        defaultProductoShouldNotBeFound("calorias.in=" + UPDATED_CALORIAS);
    }

    @Test
    @Transactional
    void getAllProductosByCaloriasIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where calorias is not null
        defaultProductoShouldBeFound("calorias.specified=true");

        // Get all the productoList where calorias is null
        defaultProductoShouldNotBeFound("calorias.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByCaloriasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where calorias is greater than or equal to DEFAULT_CALORIAS
        defaultProductoShouldBeFound("calorias.greaterThanOrEqual=" + DEFAULT_CALORIAS);

        // Get all the productoList where calorias is greater than or equal to UPDATED_CALORIAS
        defaultProductoShouldNotBeFound("calorias.greaterThanOrEqual=" + UPDATED_CALORIAS);
    }

    @Test
    @Transactional
    void getAllProductosByCaloriasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where calorias is less than or equal to DEFAULT_CALORIAS
        defaultProductoShouldBeFound("calorias.lessThanOrEqual=" + DEFAULT_CALORIAS);

        // Get all the productoList where calorias is less than or equal to SMALLER_CALORIAS
        defaultProductoShouldNotBeFound("calorias.lessThanOrEqual=" + SMALLER_CALORIAS);
    }

    @Test
    @Transactional
    void getAllProductosByCaloriasIsLessThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where calorias is less than DEFAULT_CALORIAS
        defaultProductoShouldNotBeFound("calorias.lessThan=" + DEFAULT_CALORIAS);

        // Get all the productoList where calorias is less than UPDATED_CALORIAS
        defaultProductoShouldBeFound("calorias.lessThan=" + UPDATED_CALORIAS);
    }

    @Test
    @Transactional
    void getAllProductosByCaloriasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where calorias is greater than DEFAULT_CALORIAS
        defaultProductoShouldNotBeFound("calorias.greaterThan=" + DEFAULT_CALORIAS);

        // Get all the productoList where calorias is greater than SMALLER_CALORIAS
        defaultProductoShouldBeFound("calorias.greaterThan=" + SMALLER_CALORIAS);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio equals to DEFAULT_PRECIO
        defaultProductoShouldBeFound("precio.equals=" + DEFAULT_PRECIO);

        // Get all the productoList where precio equals to UPDATED_PRECIO
        defaultProductoShouldNotBeFound("precio.equals=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio not equals to DEFAULT_PRECIO
        defaultProductoShouldNotBeFound("precio.notEquals=" + DEFAULT_PRECIO);

        // Get all the productoList where precio not equals to UPDATED_PRECIO
        defaultProductoShouldBeFound("precio.notEquals=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio in DEFAULT_PRECIO or UPDATED_PRECIO
        defaultProductoShouldBeFound("precio.in=" + DEFAULT_PRECIO + "," + UPDATED_PRECIO);

        // Get all the productoList where precio equals to UPDATED_PRECIO
        defaultProductoShouldNotBeFound("precio.in=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio is not null
        defaultProductoShouldBeFound("precio.specified=true");

        // Get all the productoList where precio is null
        defaultProductoShouldNotBeFound("precio.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio is greater than or equal to DEFAULT_PRECIO
        defaultProductoShouldBeFound("precio.greaterThanOrEqual=" + DEFAULT_PRECIO);

        // Get all the productoList where precio is greater than or equal to UPDATED_PRECIO
        defaultProductoShouldNotBeFound("precio.greaterThanOrEqual=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio is less than or equal to DEFAULT_PRECIO
        defaultProductoShouldBeFound("precio.lessThanOrEqual=" + DEFAULT_PRECIO);

        // Get all the productoList where precio is less than or equal to SMALLER_PRECIO
        defaultProductoShouldNotBeFound("precio.lessThanOrEqual=" + SMALLER_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsLessThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio is less than DEFAULT_PRECIO
        defaultProductoShouldNotBeFound("precio.lessThan=" + DEFAULT_PRECIO);

        // Get all the productoList where precio is less than UPDATED_PRECIO
        defaultProductoShouldBeFound("precio.lessThan=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precio is greater than DEFAULT_PRECIO
        defaultProductoShouldNotBeFound("precio.greaterThan=" + DEFAULT_PRECIO);

        // Get all the productoList where precio is greater than SMALLER_PRECIO
        defaultProductoShouldBeFound("precio.greaterThan=" + SMALLER_PRECIO);
    }

    @Test
    @Transactional
    void getAllProductosByExistenciasIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where existencias equals to DEFAULT_EXISTENCIAS
        defaultProductoShouldBeFound("existencias.equals=" + DEFAULT_EXISTENCIAS);

        // Get all the productoList where existencias equals to UPDATED_EXISTENCIAS
        defaultProductoShouldNotBeFound("existencias.equals=" + UPDATED_EXISTENCIAS);
    }

    @Test
    @Transactional
    void getAllProductosByExistenciasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where existencias not equals to DEFAULT_EXISTENCIAS
        defaultProductoShouldNotBeFound("existencias.notEquals=" + DEFAULT_EXISTENCIAS);

        // Get all the productoList where existencias not equals to UPDATED_EXISTENCIAS
        defaultProductoShouldBeFound("existencias.notEquals=" + UPDATED_EXISTENCIAS);
    }

    @Test
    @Transactional
    void getAllProductosByExistenciasIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where existencias in DEFAULT_EXISTENCIAS or UPDATED_EXISTENCIAS
        defaultProductoShouldBeFound("existencias.in=" + DEFAULT_EXISTENCIAS + "," + UPDATED_EXISTENCIAS);

        // Get all the productoList where existencias equals to UPDATED_EXISTENCIAS
        defaultProductoShouldNotBeFound("existencias.in=" + UPDATED_EXISTENCIAS);
    }

    @Test
    @Transactional
    void getAllProductosByExistenciasIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where existencias is not null
        defaultProductoShouldBeFound("existencias.specified=true");

        // Get all the productoList where existencias is null
        defaultProductoShouldNotBeFound("existencias.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByExistenciasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where existencias is greater than or equal to DEFAULT_EXISTENCIAS
        defaultProductoShouldBeFound("existencias.greaterThanOrEqual=" + DEFAULT_EXISTENCIAS);

        // Get all the productoList where existencias is greater than or equal to UPDATED_EXISTENCIAS
        defaultProductoShouldNotBeFound("existencias.greaterThanOrEqual=" + UPDATED_EXISTENCIAS);
    }

    @Test
    @Transactional
    void getAllProductosByExistenciasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where existencias is less than or equal to DEFAULT_EXISTENCIAS
        defaultProductoShouldBeFound("existencias.lessThanOrEqual=" + DEFAULT_EXISTENCIAS);

        // Get all the productoList where existencias is less than or equal to SMALLER_EXISTENCIAS
        defaultProductoShouldNotBeFound("existencias.lessThanOrEqual=" + SMALLER_EXISTENCIAS);
    }

    @Test
    @Transactional
    void getAllProductosByExistenciasIsLessThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where existencias is less than DEFAULT_EXISTENCIAS
        defaultProductoShouldNotBeFound("existencias.lessThan=" + DEFAULT_EXISTENCIAS);

        // Get all the productoList where existencias is less than UPDATED_EXISTENCIAS
        defaultProductoShouldBeFound("existencias.lessThan=" + UPDATED_EXISTENCIAS);
    }

    @Test
    @Transactional
    void getAllProductosByExistenciasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where existencias is greater than DEFAULT_EXISTENCIAS
        defaultProductoShouldNotBeFound("existencias.greaterThan=" + DEFAULT_EXISTENCIAS);

        // Get all the productoList where existencias is greater than SMALLER_EXISTENCIAS
        defaultProductoShouldBeFound("existencias.greaterThan=" + SMALLER_EXISTENCIAS);
    }

    @Test
    @Transactional
    void getAllProductosByCarritoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);
        Carrito carrito = CarritoResourceIT.createEntity(em);
        em.persist(carrito);
        em.flush();
        producto.addCarrito(carrito);
        productoRepository.saveAndFlush(producto);
        Long carritoId = carrito.getId();

        // Get all the productoList where carrito equals to carritoId
        defaultProductoShouldBeFound("carritoId.equals=" + carritoId);

        // Get all the productoList where carrito equals to (carritoId + 1)
        defaultProductoShouldNotBeFound("carritoId.equals=" + (carritoId + 1));
    }

    @Test
    @Transactional
    void getAllProductosByTipoproductoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);
        TipoProducto tipoproducto = TipoProductoResourceIT.createEntity(em);
        em.persist(tipoproducto);
        em.flush();
        producto.setTipoproducto(tipoproducto);
        productoRepository.saveAndFlush(producto);
        Long tipoproductoId = tipoproducto.getId();

        // Get all the productoList where tipoproducto equals to tipoproductoId
        defaultProductoShouldBeFound("tipoproductoId.equals=" + tipoproductoId);

        // Get all the productoList where tipoproducto equals to (tipoproductoId + 1)
        defaultProductoShouldNotBeFound("tipoproductoId.equals=" + (tipoproductoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductoShouldBeFound(String filter) throws Exception {
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreProducto").value(hasItem(DEFAULT_NOMBRE_PRODUCTO)))
            .andExpect(jsonPath("$.[*].ingredientes").value(hasItem(DEFAULT_INGREDIENTES)))
            .andExpect(jsonPath("$.[*].calorias").value(hasItem(DEFAULT_CALORIAS)))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].existencias").value(hasItem(DEFAULT_EXISTENCIAS)));

        // Check, that the count call also returns 1
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductoShouldNotBeFound(String filter) throws Exception {
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProducto() throws Exception {
        // Get the producto
        restProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProducto() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeUpdate = productoRepository.findAll().size();

        // Update the producto
        Producto updatedProducto = productoRepository.findById(producto.getId()).get();
        // Disconnect from session so that the updates on updatedProducto are not directly saved in db
        em.detach(updatedProducto);
        updatedProducto
            .nombreProducto(UPDATED_NOMBRE_PRODUCTO)
            .ingredientes(UPDATED_INGREDIENTES)
            .calorias(UPDATED_CALORIAS)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .precio(UPDATED_PRECIO)
            .existencias(UPDATED_EXISTENCIAS);
        ProductoDTO productoDTO = productoMapper.toDto(updatedProducto);

        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getNombreProducto()).isEqualTo(UPDATED_NOMBRE_PRODUCTO);
        assertThat(testProducto.getIngredientes()).isEqualTo(UPDATED_INGREDIENTES);
        assertThat(testProducto.getCalorias()).isEqualTo(UPDATED_CALORIAS);
        assertThat(testProducto.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testProducto.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);
        assertThat(testProducto.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testProducto.getExistencias()).isEqualTo(UPDATED_EXISTENCIAS);
    }

    @Test
    @Transactional
    void putNonExistingProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeUpdate = productoRepository.findAll().size();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto.existencias(UPDATED_EXISTENCIAS);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getNombreProducto()).isEqualTo(DEFAULT_NOMBRE_PRODUCTO);
        assertThat(testProducto.getIngredientes()).isEqualTo(DEFAULT_INGREDIENTES);
        assertThat(testProducto.getCalorias()).isEqualTo(DEFAULT_CALORIAS);
        assertThat(testProducto.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testProducto.getImagenContentType()).isEqualTo(DEFAULT_IMAGEN_CONTENT_TYPE);
        assertThat(testProducto.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testProducto.getExistencias()).isEqualTo(UPDATED_EXISTENCIAS);
    }

    @Test
    @Transactional
    void fullUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeUpdate = productoRepository.findAll().size();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto
            .nombreProducto(UPDATED_NOMBRE_PRODUCTO)
            .ingredientes(UPDATED_INGREDIENTES)
            .calorias(UPDATED_CALORIAS)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .precio(UPDATED_PRECIO)
            .existencias(UPDATED_EXISTENCIAS);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getNombreProducto()).isEqualTo(UPDATED_NOMBRE_PRODUCTO);
        assertThat(testProducto.getIngredientes()).isEqualTo(UPDATED_INGREDIENTES);
        assertThat(testProducto.getCalorias()).isEqualTo(UPDATED_CALORIAS);
        assertThat(testProducto.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testProducto.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);
        assertThat(testProducto.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testProducto.getExistencias()).isEqualTo(UPDATED_EXISTENCIAS);
    }

    @Test
    @Transactional
    void patchNonExistingProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();
        producto.setId(count.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducto() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeDelete = productoRepository.findAll().size();

        // Delete the producto
        restProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, producto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
