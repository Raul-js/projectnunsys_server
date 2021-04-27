package es.projectnunsys.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link es.projectnunsys.domain.Carrito} entity.
 */
public class CarritoDTO implements Serializable {

    private Long id;

    private Integer cantidad;

    private Instant fechaCarrito;

    private UsuarioDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Instant getFechaCarrito() {
        return fechaCarrito;
    }

    public void setFechaCarrito(Instant fechaCarrito) {
        this.fechaCarrito = fechaCarrito;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarritoDTO)) {
            return false;
        }

        CarritoDTO carritoDTO = (CarritoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carritoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarritoDTO{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", fechaCarrito='" + getFechaCarrito() + "'" +
            ", usuario=" + getUsuario() +
            "}";
    }
}
