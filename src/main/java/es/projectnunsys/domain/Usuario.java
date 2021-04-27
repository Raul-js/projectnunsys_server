package es.projectnunsys.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido_1")
    private String apellido1;

    @Column(name = "apellido_2")
    private String apellido2;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnoreProperties(value = { "usuario", "productos" }, allowSetters = true)
    private Set<Carrito> carritos = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @JsonIgnoreProperties(value = { "compras", "usuario" }, allowSetters = true)
    private Set<Factura> facturas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Usuario nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return this.apellido1;
    }

    public Usuario apellido1(String apellido1) {
        this.apellido1 = apellido1;
        return this;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return this.apellido2;
    }

    public Usuario apellido2(String apellido2) {
        this.apellido2 = apellido2;
        return this;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getEmail() {
        return this.email;
    }

    public Usuario email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public Usuario password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Carrito> getCarritos() {
        return this.carritos;
    }

    public Usuario carritos(Set<Carrito> carritos) {
        this.setCarritos(carritos);
        return this;
    }

    public Usuario addCarrito(Carrito carrito) {
        this.carritos.add(carrito);
        carrito.setUsuario(this);
        return this;
    }

    public Usuario removeCarrito(Carrito carrito) {
        this.carritos.remove(carrito);
        carrito.setUsuario(null);
        return this;
    }

    public void setCarritos(Set<Carrito> carritos) {
        if (this.carritos != null) {
            this.carritos.forEach(i -> i.setUsuario(null));
        }
        if (carritos != null) {
            carritos.forEach(i -> i.setUsuario(this));
        }
        this.carritos = carritos;
    }

    public Set<Factura> getFacturas() {
        return this.facturas;
    }

    public Usuario facturas(Set<Factura> facturas) {
        this.setFacturas(facturas);
        return this;
    }

    public Usuario addFactura(Factura factura) {
        this.facturas.add(factura);
        factura.setUsuario(this);
        return this;
    }

    public Usuario removeFactura(Factura factura) {
        this.facturas.remove(factura);
        factura.setUsuario(null);
        return this;
    }

    public void setFacturas(Set<Factura> facturas) {
        if (this.facturas != null) {
            this.facturas.forEach(i -> i.setUsuario(null));
        }
        if (facturas != null) {
            facturas.forEach(i -> i.setUsuario(this));
        }
        this.facturas = facturas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido1='" + getApellido1() + "'" +
            ", apellido2='" + getApellido2() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
