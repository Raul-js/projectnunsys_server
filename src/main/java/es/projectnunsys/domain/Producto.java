package es.projectnunsys.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Producto.
 */
@Entity
@Table(name = "producto")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nombre_producto")
    private String nombreProducto;

    @Column(name = "ingredientes")
    private String ingredientes;

    @Column(name = "calorias")
    private Integer calorias;

    @Lob
    @Column(name = "imagen")
    private byte[] imagen;

    @Column(name = "imagen_content_type")
    private String imagenContentType;

    @Column(name = "precio")
    private Float precio;

    @Column(name = "existencias")
    private Integer existencias;

    @ManyToMany
    @JoinTable(
        name = "rel_producto__carrito",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "carrito_id")
    )
    @JsonIgnoreProperties(value = { "usuario", "productos" }, allowSetters = true)
    private Set<Carrito> carritos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "productos" }, allowSetters = true)
    private TipoProducto tipoproducto;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombreProducto() {
        return this.nombreProducto;
    }

    public Producto nombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
        return this;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getIngredientes() {
        return this.ingredientes;
    }

    public Producto ingredientes(String ingredientes) {
        this.ingredientes = ingredientes;
        return this;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Integer getCalorias() {
        return this.calorias;
    }

    public Producto calorias(Integer calorias) {
        this.calorias = calorias;
        return this;
    }

    public void setCalorias(Integer calorias) {
        this.calorias = calorias;
    }

    public byte[] getImagen() {
        return this.imagen;
    }

    public Producto imagen(byte[] imagen) {
        this.imagen = imagen;
        return this;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenContentType() {
        return this.imagenContentType;
    }

    public Producto imagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
        return this;
    }

    public void setImagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
    }

    public Float getPrecio() {
        return this.precio;
    }

    public Producto precio(Float precio) {
        this.precio = precio;
        return this;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getExistencias() {
        return this.existencias;
    }

    public Producto existencias(Integer existencias) {
        this.existencias = existencias;
        return this;
    }

    public void setExistencias(Integer existencias) {
        this.existencias = existencias;
    }

    public Set<Carrito> getCarritos() {
        return this.carritos;
    }

    public Producto carritos(Set<Carrito> carritos) {
        this.setCarritos(carritos);
        return this;
    }

    public Producto addCarrito(Carrito carrito) {
        this.carritos.add(carrito);
        carrito.getProductos().add(this);
        return this;
    }

    public Producto removeCarrito(Carrito carrito) {
        this.carritos.remove(carrito);
        carrito.getProductos().remove(this);
        return this;
    }

    public void setCarritos(Set<Carrito> carritos) {
        this.carritos = carritos;
    }

    public TipoProducto getTipoproducto() {
        return this.tipoproducto;
    }

    public Producto tipoproducto(TipoProducto tipoProducto) {
        this.setTipoproducto(tipoProducto);
        return this;
    }

    public void setTipoproducto(TipoProducto tipoProducto) {
        this.tipoproducto = tipoProducto;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        return id != null && id.equals(((Producto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Producto{" +
            "id=" + getId() +
            ", nombreProducto='" + getNombreProducto() + "'" +
            ", ingredientes='" + getIngredientes() + "'" +
            ", calorias=" + getCalorias() +
            ", imagen='" + getImagen() + "'" +
            ", imagenContentType='" + getImagenContentType() + "'" +
            ", precio=" + getPrecio() +
            ", existencias=" + getExistencias() +
            "}";
    }
}
