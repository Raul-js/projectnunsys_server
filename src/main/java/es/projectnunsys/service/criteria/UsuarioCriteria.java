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
 * Criteria class for the {@link es.projectnunsys.domain.Usuario} entity. This class is used
 * in {@link es.projectnunsys.web.rest.UsuarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /usuarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UsuarioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter apellido1;

    private StringFilter apellido2;

    private StringFilter email;

    private StringFilter password;

    private LongFilter carritoId;

    private LongFilter facturaId;

    public UsuarioCriteria() {}

    public UsuarioCriteria(UsuarioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.apellido1 = other.apellido1 == null ? null : other.apellido1.copy();
        this.apellido2 = other.apellido2 == null ? null : other.apellido2.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.carritoId = other.carritoId == null ? null : other.carritoId.copy();
        this.facturaId = other.facturaId == null ? null : other.facturaId.copy();
    }

    @Override
    public UsuarioCriteria copy() {
        return new UsuarioCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getApellido1() {
        return apellido1;
    }

    public StringFilter apellido1() {
        if (apellido1 == null) {
            apellido1 = new StringFilter();
        }
        return apellido1;
    }

    public void setApellido1(StringFilter apellido1) {
        this.apellido1 = apellido1;
    }

    public StringFilter getApellido2() {
        return apellido2;
    }

    public StringFilter apellido2() {
        if (apellido2 == null) {
            apellido2 = new StringFilter();
        }
        return apellido2;
    }

    public void setApellido2(StringFilter apellido2) {
        this.apellido2 = apellido2;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPassword() {
        return password;
    }

    public StringFilter password() {
        if (password == null) {
            password = new StringFilter();
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
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

    public LongFilter getFacturaId() {
        return facturaId;
    }

    public LongFilter facturaId() {
        if (facturaId == null) {
            facturaId = new LongFilter();
        }
        return facturaId;
    }

    public void setFacturaId(LongFilter facturaId) {
        this.facturaId = facturaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UsuarioCriteria that = (UsuarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(apellido1, that.apellido1) &&
            Objects.equals(apellido2, that.apellido2) &&
            Objects.equals(email, that.email) &&
            Objects.equals(password, that.password) &&
            Objects.equals(carritoId, that.carritoId) &&
            Objects.equals(facturaId, that.facturaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido1, apellido2, email, password, carritoId, facturaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (apellido1 != null ? "apellido1=" + apellido1 + ", " : "") +
            (apellido2 != null ? "apellido2=" + apellido2 + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (password != null ? "password=" + password + ", " : "") +
            (carritoId != null ? "carritoId=" + carritoId + ", " : "") +
            (facturaId != null ? "facturaId=" + facturaId + ", " : "") +
            "}";
    }
}
