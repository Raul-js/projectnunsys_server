package es.projectnunsys.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.projectnunsys.IntegrationTest;
import es.projectnunsys.domain.Carrito;
import es.projectnunsys.domain.Factura;
import es.projectnunsys.domain.Usuario;
import es.projectnunsys.repository.UsuarioRepository;
import es.projectnunsys.service.criteria.UsuarioCriteria;
import es.projectnunsys.service.dto.UsuarioDTO;
import es.projectnunsys.service.mapper.UsuarioMapper;
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
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsuarioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO_1 = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO_1 = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO_2 = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO_2 = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .nombre(DEFAULT_NOMBRE)
            .apellido1(DEFAULT_APELLIDO_1)
            .apellido2(DEFAULT_APELLIDO_2)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD);
        return usuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createUpdatedEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .nombre(UPDATED_NOMBRE)
            .apellido1(UPDATED_APELLIDO_1)
            .apellido2(UPDATED_APELLIDO_2)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD);
        return usuario;
    }

    @BeforeEach
    public void initTest() {
        usuario = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();
        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isCreated());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testUsuario.getApellido1()).isEqualTo(DEFAULT_APELLIDO_1);
        assertThat(testUsuario.getApellido2()).isEqualTo(DEFAULT_APELLIDO_2);
        assertThat(testUsuario.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUsuario.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createUsuarioWithExistingId() throws Exception {
        // Create the Usuario with an existing ID
        usuario.setId(1L);
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido1").value(hasItem(DEFAULT_APELLIDO_1)))
            .andExpect(jsonPath("$.[*].apellido2").value(hasItem(DEFAULT_APELLIDO_2)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido1").value(DEFAULT_APELLIDO_1))
            .andExpect(jsonPath("$.apellido2").value(DEFAULT_APELLIDO_2))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getUsuariosByIdFiltering() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        Long id = usuario.getId();

        defaultUsuarioShouldBeFound("id.equals=" + id);
        defaultUsuarioShouldNotBeFound("id.notEquals=" + id);

        defaultUsuarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.greaterThan=" + id);

        defaultUsuarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre equals to DEFAULT_NOMBRE
        defaultUsuarioShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the usuarioList where nombre equals to UPDATED_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre not equals to DEFAULT_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the usuarioList where nombre not equals to UPDATED_NOMBRE
        defaultUsuarioShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultUsuarioShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the usuarioList where nombre equals to UPDATED_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre is not null
        defaultUsuarioShouldBeFound("nombre.specified=true");

        // Get all the usuarioList where nombre is null
        defaultUsuarioShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre contains DEFAULT_NOMBRE
        defaultUsuarioShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the usuarioList where nombre contains UPDATED_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre does not contain DEFAULT_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the usuarioList where nombre does not contain UPDATED_NOMBRE
        defaultUsuarioShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellido1IsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellido1 equals to DEFAULT_APELLIDO_1
        defaultUsuarioShouldBeFound("apellido1.equals=" + DEFAULT_APELLIDO_1);

        // Get all the usuarioList where apellido1 equals to UPDATED_APELLIDO_1
        defaultUsuarioShouldNotBeFound("apellido1.equals=" + UPDATED_APELLIDO_1);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellido1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellido1 not equals to DEFAULT_APELLIDO_1
        defaultUsuarioShouldNotBeFound("apellido1.notEquals=" + DEFAULT_APELLIDO_1);

        // Get all the usuarioList where apellido1 not equals to UPDATED_APELLIDO_1
        defaultUsuarioShouldBeFound("apellido1.notEquals=" + UPDATED_APELLIDO_1);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellido1IsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellido1 in DEFAULT_APELLIDO_1 or UPDATED_APELLIDO_1
        defaultUsuarioShouldBeFound("apellido1.in=" + DEFAULT_APELLIDO_1 + "," + UPDATED_APELLIDO_1);

        // Get all the usuarioList where apellido1 equals to UPDATED_APELLIDO_1
        defaultUsuarioShouldNotBeFound("apellido1.in=" + UPDATED_APELLIDO_1);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellido1IsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellido1 is not null
        defaultUsuarioShouldBeFound("apellido1.specified=true");

        // Get all the usuarioList where apellido1 is null
        defaultUsuarioShouldNotBeFound("apellido1.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByApellido1ContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellido1 contains DEFAULT_APELLIDO_1
        defaultUsuarioShouldBeFound("apellido1.contains=" + DEFAULT_APELLIDO_1);

        // Get all the usuarioList where apellido1 contains UPDATED_APELLIDO_1
        defaultUsuarioShouldNotBeFound("apellido1.contains=" + UPDATED_APELLIDO_1);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellido1NotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellido1 does not contain DEFAULT_APELLIDO_1
        defaultUsuarioShouldNotBeFound("apellido1.doesNotContain=" + DEFAULT_APELLIDO_1);

        // Get all the usuarioList where apellido1 does not contain UPDATED_APELLIDO_1
        defaultUsuarioShouldBeFound("apellido1.doesNotContain=" + UPDATED_APELLIDO_1);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellido2IsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellido2 equals to DEFAULT_APELLIDO_2
        defaultUsuarioShouldBeFound("apellido2.equals=" + DEFAULT_APELLIDO_2);

        // Get all the usuarioList where apellido2 equals to UPDATED_APELLIDO_2
        defaultUsuarioShouldNotBeFound("apellido2.equals=" + UPDATED_APELLIDO_2);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellido2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellido2 not equals to DEFAULT_APELLIDO_2
        defaultUsuarioShouldNotBeFound("apellido2.notEquals=" + DEFAULT_APELLIDO_2);

        // Get all the usuarioList where apellido2 not equals to UPDATED_APELLIDO_2
        defaultUsuarioShouldBeFound("apellido2.notEquals=" + UPDATED_APELLIDO_2);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellido2IsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellido2 in DEFAULT_APELLIDO_2 or UPDATED_APELLIDO_2
        defaultUsuarioShouldBeFound("apellido2.in=" + DEFAULT_APELLIDO_2 + "," + UPDATED_APELLIDO_2);

        // Get all the usuarioList where apellido2 equals to UPDATED_APELLIDO_2
        defaultUsuarioShouldNotBeFound("apellido2.in=" + UPDATED_APELLIDO_2);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellido2IsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellido2 is not null
        defaultUsuarioShouldBeFound("apellido2.specified=true");

        // Get all the usuarioList where apellido2 is null
        defaultUsuarioShouldNotBeFound("apellido2.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByApellido2ContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellido2 contains DEFAULT_APELLIDO_2
        defaultUsuarioShouldBeFound("apellido2.contains=" + DEFAULT_APELLIDO_2);

        // Get all the usuarioList where apellido2 contains UPDATED_APELLIDO_2
        defaultUsuarioShouldNotBeFound("apellido2.contains=" + UPDATED_APELLIDO_2);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellido2NotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellido2 does not contain DEFAULT_APELLIDO_2
        defaultUsuarioShouldNotBeFound("apellido2.doesNotContain=" + DEFAULT_APELLIDO_2);

        // Get all the usuarioList where apellido2 does not contain UPDATED_APELLIDO_2
        defaultUsuarioShouldBeFound("apellido2.doesNotContain=" + UPDATED_APELLIDO_2);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email equals to DEFAULT_EMAIL
        defaultUsuarioShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the usuarioList where email equals to UPDATED_EMAIL
        defaultUsuarioShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email not equals to DEFAULT_EMAIL
        defaultUsuarioShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the usuarioList where email not equals to UPDATED_EMAIL
        defaultUsuarioShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultUsuarioShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the usuarioList where email equals to UPDATED_EMAIL
        defaultUsuarioShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email is not null
        defaultUsuarioShouldBeFound("email.specified=true");

        // Get all the usuarioList where email is null
        defaultUsuarioShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email contains DEFAULT_EMAIL
        defaultUsuarioShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the usuarioList where email contains UPDATED_EMAIL
        defaultUsuarioShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email does not contain DEFAULT_EMAIL
        defaultUsuarioShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the usuarioList where email does not contain UPDATED_EMAIL
        defaultUsuarioShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where password equals to DEFAULT_PASSWORD
        defaultUsuarioShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the usuarioList where password equals to UPDATED_PASSWORD
        defaultUsuarioShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsuariosByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where password not equals to DEFAULT_PASSWORD
        defaultUsuarioShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the usuarioList where password not equals to UPDATED_PASSWORD
        defaultUsuarioShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsuariosByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultUsuarioShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the usuarioList where password equals to UPDATED_PASSWORD
        defaultUsuarioShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsuariosByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where password is not null
        defaultUsuarioShouldBeFound("password.specified=true");

        // Get all the usuarioList where password is null
        defaultUsuarioShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByPasswordContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where password contains DEFAULT_PASSWORD
        defaultUsuarioShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the usuarioList where password contains UPDATED_PASSWORD
        defaultUsuarioShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsuariosByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where password does not contain DEFAULT_PASSWORD
        defaultUsuarioShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the usuarioList where password does not contain UPDATED_PASSWORD
        defaultUsuarioShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsuariosByCarritoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        Carrito carrito = CarritoResourceIT.createEntity(em);
        em.persist(carrito);
        em.flush();
        usuario.addCarrito(carrito);
        usuarioRepository.saveAndFlush(usuario);
        Long carritoId = carrito.getId();

        // Get all the usuarioList where carrito equals to carritoId
        defaultUsuarioShouldBeFound("carritoId.equals=" + carritoId);

        // Get all the usuarioList where carrito equals to (carritoId + 1)
        defaultUsuarioShouldNotBeFound("carritoId.equals=" + (carritoId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByFacturaIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        Factura factura = FacturaResourceIT.createEntity(em);
        em.persist(factura);
        em.flush();
        usuario.addFactura(factura);
        usuarioRepository.saveAndFlush(usuario);
        Long facturaId = factura.getId();

        // Get all the usuarioList where factura equals to facturaId
        defaultUsuarioShouldBeFound("facturaId.equals=" + facturaId);

        // Get all the usuarioList where factura equals to (facturaId + 1)
        defaultUsuarioShouldNotBeFound("facturaId.equals=" + (facturaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioShouldBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido1").value(hasItem(DEFAULT_APELLIDO_1)))
            .andExpect(jsonPath("$.[*].apellido2").value(hasItem(DEFAULT_APELLIDO_2)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));

        // Check, that the count call also returns 1
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioShouldNotBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).get();
        // Disconnect from session so that the updates on updatedUsuario are not directly saved in db
        em.detach(updatedUsuario);
        updatedUsuario
            .nombre(UPDATED_NOMBRE)
            .apellido1(UPDATED_APELLIDO_1)
            .apellido2(UPDATED_APELLIDO_2)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD);
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(updatedUsuario);

        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuario.getApellido1()).isEqualTo(UPDATED_APELLIDO_1);
        assertThat(testUsuario.getApellido2()).isEqualTo(UPDATED_APELLIDO_2);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsuario.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario.nombre(UPDATED_NOMBRE).apellido2(UPDATED_APELLIDO_2).email(UPDATED_EMAIL).password(UPDATED_PASSWORD);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuario.getApellido1()).isEqualTo(DEFAULT_APELLIDO_1);
        assertThat(testUsuario.getApellido2()).isEqualTo(UPDATED_APELLIDO_2);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsuario.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .nombre(UPDATED_NOMBRE)
            .apellido1(UPDATED_APELLIDO_1)
            .apellido2(UPDATED_APELLIDO_2)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuario.getApellido1()).isEqualTo(UPDATED_APELLIDO_1);
        assertThat(testUsuario.getApellido2()).isEqualTo(UPDATED_APELLIDO_2);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsuario.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeDelete = usuarioRepository.findAll().size();

        // Delete the usuario
        restUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
