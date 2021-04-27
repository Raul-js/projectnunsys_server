package es.projectnunsys.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;

/**
 * A DTO for the {@link es.projectnunsys.domain.Producto} entity.
 */
public class ProductoDTO implements Serializable {

    private Long id;

    private String nombreProducto;

    private String ingredientes;

    private Integer calorias;

    @Lob
    private byte[] imagen;

    private String imagenContentType;
    private Float precio;

    private Integer existencias;

    private Set<CarritoDTO> carritos = new HashSet<>();

    private TipoProductoDTO tipoproducto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Integer getCalorias() {
        return calorias;
    }

    public void setCalorias(Integer calorias) {
        this.calorias = calorias;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenContentType() {
        return imagenContentType;
    }

    public void setImagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getExistencias() {
        return existencias;
    }

    public void setExistencias(Integer existencias) {
        this.existencias = existencias;
    }

    public Set<CarritoDTO> getCarritos() {
        return carritos;
    }

    public void setCarritos(Set<CarritoDTO> carritos) {
        this.carritos = carritos;
    }

    public TipoProductoDTO getTipoproducto() {
        return tipoproducto;
    }

    public void setTipoproducto(TipoProductoDTO tipoproducto) {
        this.tipoproducto = tipoproducto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductoDTO)) {
            return false;
        }

        ProductoDTO productoDTO = (ProductoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoDTO{" +
            "id=" + getId() +
            ", nombreProducto='" + getNombreProducto() + "'" +
            ", ingredientes='" + getIngredientes() + "'" +
            ", calorias=" + getCalorias() +
            ", imagen='" + getImagen() + "'" +
            ", precio=" + getPrecio() +
            ", existencias=" + getExistencias() +
            ", carritos=" + getCarritos() +
            ", tipoproducto=" + getTipoproducto() +
            "}";
    }
}
