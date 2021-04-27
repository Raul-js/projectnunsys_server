package es.projectnunsys.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link es.projectnunsys.domain.TipoProducto} entity.
 */
public class TipoProductoDTO implements Serializable {

    private Long id;

    private String nombreTipoProducto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreTipoProducto() {
        return nombreTipoProducto;
    }

    public void setNombreTipoProducto(String nombreTipoProducto) {
        this.nombreTipoProducto = nombreTipoProducto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoProductoDTO)) {
            return false;
        }

        TipoProductoDTO tipoProductoDTO = (TipoProductoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoProductoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoProductoDTO{" +
            "id=" + getId() +
            ", nombreTipoProducto='" + getNombreTipoProducto() + "'" +
            "}";
    }
}
