package es.projectnunsys.domain;

import static org.assertj.core.api.Assertions.assertThat;

import es.projectnunsys.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoProducto.class);
        TipoProducto tipoProducto1 = new TipoProducto();
        tipoProducto1.setId(1L);
        TipoProducto tipoProducto2 = new TipoProducto();
        tipoProducto2.setId(tipoProducto1.getId());
        assertThat(tipoProducto1).isEqualTo(tipoProducto2);
        tipoProducto2.setId(2L);
        assertThat(tipoProducto1).isNotEqualTo(tipoProducto2);
        tipoProducto1.setId(null);
        assertThat(tipoProducto1).isNotEqualTo(tipoProducto2);
    }
}
