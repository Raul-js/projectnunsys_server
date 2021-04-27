package es.projectnunsys.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Carrito.
 */
@Entity
@Table(name = "carrito")
public class Carrito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_carrito")
    private Instant fechaCarrito;

    @ManyToOne
    @JsonIgnoreProperties(value = { "carritos", "facturas" }, allowSetters = true)
    private Usuario usuario;

    @ManyToMany(mappedBy = "carritos")
    @JsonIgnoreProperties(value = { "carritos", "tipoproducto" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrito id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Carrito cantidad(Integer cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Instant getFechaCarrito() {
        return this.fechaCarrito;
    }

    public Carrito fechaCarrito(Instant fechaCarrito) {
        this.fechaCarrito = fechaCarrito;
        return this;
    }

    public void setFechaCarrito(Instant fechaCarrito) {
        this.fechaCarrito = fechaCarrito;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public Carrito usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public Carrito productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public Carrito addProducto(Producto producto) {
        this.productos.add(producto);
        producto.getCarritos().add(this);
        return this;
    }

    public Carrito removeProducto(Producto producto) {
        this.productos.remove(producto);
        producto.getCarritos().remove(this);
        return this;
    }

    public void setProductos(Set<Producto> productos) {
        if (this.productos != null) {
            this.productos.forEach(i -> i.removeCarrito(this));
        }
        if (productos != null) {
            productos.forEach(i -> i.addCarrito(this));
        }
        this.productos = productos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Carrito)) {
            return false;
        }
        return id != null && id.equals(((Carrito) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Carrito{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", fechaCarrito='" + getFechaCarrito() + "'" +
            "}";
    }
}
