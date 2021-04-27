package es.projectnunsys.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link es.projectnunsys.domain.Factura} entity.
 */
public class FacturaDTO implements Serializable {

    private Long id;

    private Instant fechaFactura;

    private Integer cantidadPagada;

    private UsuarioDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Instant fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public Integer getCantidadPagada() {
        return cantidadPagada;
    }

    public void setCantidadPagada(Integer cantidadPagada) {
        this.cantidadPagada = cantidadPagada;
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
        if (!(o instanceof FacturaDTO)) {
            return false;
        }

        FacturaDTO facturaDTO = (FacturaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facturaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaDTO{" +
            "id=" + getId() +
            ", fechaFactura='" + getFechaFactura() + "'" +
            ", cantidadPagada=" + getCantidadPagada() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
