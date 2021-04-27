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
 * Criteria class for the {@link es.projectnunsys.domain.Producto} entity. This class is used
 * in {@link es.projectnunsys.web.rest.ProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombreProducto;

    private StringFilter ingredientes;

    private IntegerFilter calorias;

    private FloatFilter precio;

    private IntegerFilter existencias;

    private LongFilter carritoId;

    private LongFilter tipoproductoId;

    public ProductoCriteria() {}

    public ProductoCriteria(ProductoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombreProducto = other.nombreProducto == null ? null : other.nombreProducto.copy();
        this.ingredientes = other.ingredientes == null ? null : other.ingredientes.copy();
        this.calorias = other.calorias == null ? null : other.calorias.copy();
        this.precio = other.precio == null ? null : other.precio.copy();
        this.existencias = other.existencias == null ? null : other.existencias.copy();
        this.carritoId = other.carritoId == null ? null : other.carritoId.copy();
        this.tipoproductoId = other.tipoproductoId == null ? null : other.tipoproductoId.copy();
    }

    @Override
    public ProductoCriteria copy() {
        return new ProductoCriteria(this);
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

    public StringFilter getNombreProducto() {
        return nombreProducto;
    }

    public StringFilter nombreProducto() {
        if (nombreProducto == null) {
            nombreProducto = new StringFilter();
        }
        return nombreProducto;
    }

    public void setNombreProducto(StringFilter nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public StringFilter getIngredientes() {
        return ingredientes;
    }

    public StringFilter ingredientes() {
        if (ingredientes == null) {
            ingredientes = new StringFilter();
        }
        return ingredientes;
    }

    public void setIngredientes(StringFilter ingredientes) {
        this.ingredientes = ingredientes;
    }

    public IntegerFilter getCalorias() {
        return calorias;
    }

    public IntegerFilter calorias() {
        if (calorias == null) {
            calorias = new IntegerFilter();
        }
        return calorias;
    }

    public void setCalorias(IntegerFilter calorias) {
        this.calorias = calorias;
    }

    public FloatFilter getPrecio() {
        return precio;
    }

    public FloatFilter precio() {
        if (precio == null) {
            precio = new FloatFilter();
        }
        return precio;
    }

    public void setPrecio(FloatFilter precio) {
        this.precio = precio;
    }

    public IntegerFilter getExistencias() {
        return existencias;
    }

    public IntegerFilter existencias() {
        if (existencias == null) {
            existencias = new IntegerFilter();
        }
        return existencias;
    }

    public void setExistencias(IntegerFilter existencias) {
        this.existencias = existencias;
    }

    public LongFilter getCarritoId() {
        return carritoId;
    }

    public LongFilter carritoId() {
        if (carritoId == null) {
            carritoId = new LongFilter();
        }
        return carritoId;
    }

    public void setCarritoId(LongFilter carritoId) {
        this.carritoId = carritoId;
    }

    public LongFilter getTipoproductoId() {
        return tipoproductoId;
    }

    public LongFilter tipoproductoId() {
        if (tipoproductoId == null) {
            tipoproductoId = new LongFilter();
        }
        return tipoproductoId;
    }

    public void setTipoproductoId(LongFilter tipoproductoId) {
        this.tipoproductoId = tipoproductoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductoCriteria that = (ProductoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombreProducto, that.nombreProducto) &&
            Objects.equals(ingredientes, that.ingredientes) &&
            Objects.equals(calorias, that.calorias) &&
            Objects.equals(precio, that.precio) &&
            Objects.equals(existencias, that.existencias) &&
            Objects.equals(carritoId, that.carritoId) &&
            Objects.equals(tipoproductoId, that.tipoproductoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombreProducto, ingredientes, calorias, precio, existencias, carritoId, tipoproductoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombreProducto != null ? "nombreProducto=" + nombreProducto + ", " : "") +
            (ingredientes != null ? "ingredientes=" + ingredientes + ", " : "") +
            (calorias != null ? "calorias=" + calorias + ", " : "") +
            (precio != null ? "precio=" + precio + ", " : "") +
            (existencias != null ? "existencias=" + existencias + ", " : "") +
            (carritoId != null ? "carritoId=" + carritoId + ", " : "") +
            (tipoproductoId != null ? "tipoproductoId=" + tipoproductoId + ", " : "") +
            "}";
    }
}
