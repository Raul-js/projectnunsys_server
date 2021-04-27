package es.projectnunsys.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link es.projectnunsys.domain.TipoProducto} entity. This class is used
 * in {@link es.projectnunsys.web.rest.TipoProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tipo-productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TipoProductoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombreTipoProducto;

    private LongFilter productoId;

    public TipoProductoCriteria() {}

    public TipoProductoCriteria(TipoProductoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombreTipoProducto = other.nombreTipoProducto == null ? null : other.nombreTipoProducto.copy();
        this.productoId = other.productoId == null ? null : other.productoId.copy();
    }

    @Override
    public TipoProductoCriteria copy() {
        return new TipoProductoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombreTipoProducto() {
        return nombreTipoProducto;
    }

    public StringFilter nombreTipoProducto() {
        if (nombreTipoProducto == null) {
            nombreTipoProducto = new StringFilter();
        }
        return nombreTipoProducto;
    }

    public void setNombreTipoProducto(StringFilter nombreTipoProducto) {
        this.nombreTipoProducto = nombreTipoProducto;
    }

    public LongFilter getProductoId() {
        return productoId;
    }

    public LongFilter productoId() {
        if (productoId == null) {
            productoId = new LongFilter();
        }
        return productoId;
    }

    public void setProductoId(LongFilter productoId) {
        this.productoId = productoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TipoProductoCriteria that = (TipoProductoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombreTipoProducto, that.nombreTipoProducto) &&
            Objects.equals(productoId, that.productoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombreTipoProducto, productoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoProductoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombreTipoProducto != null ? "nombreTipoProducto=" + nombreTipoProducto + ", " : "") +
            (productoId != null ? "productoId=" + productoId + ", " : "") +
            "}";
    }
}
