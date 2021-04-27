package es.projectnunsys.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A TipoProducto.
 */
@Entity
@Table(name = "tipo_producto")
public class TipoProducto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nombre_tipo_producto")
    private String nombreTipoProducto;

    @OneToMany(mappedBy = "tipoproducto")
    @JsonIgnoreProperties(value = { "carritos", "tipoproducto" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoProducto id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombreTipoProducto() {
        return this.nombreTipoProducto;
    }

    public TipoProducto nombreTipoProducto(String nombreTipoProducto) {
        this.nombreTipoProducto = nombreTipoProducto;
        return this;
    }

    public void setNombreTipoProducto(String nombreTipoProducto) {
        this.nombreTipoProducto = nombreTipoProducto;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public TipoProducto productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public TipoProducto addProducto(Producto producto) {
        this.productos.add(producto);
        producto.setTipoproducto(this);
        return this;
    }

    public TipoProducto removeProducto(Producto producto) {
        this.productos.remove(producto);
        producto.setTipoproducto(null);
        return this;
    }

    public void setProductos(Set<Producto> productos) {
        if (this.productos != null) {
            this.productos.forEach(i -> i.setTipoproducto(null));
        }
        if (productos != null) {
            productos.forEach(i -> i.setTipoproducto(this));
        }
        this.productos = productos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoProducto)) {
            return false;
        }
        return id != null && id.equals(((TipoProducto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoProducto{" +
            "id=" + getId() +
            ", nombreTipoProducto='" + getNombreTipoProducto() + "'" +
            "}";
    }
}
