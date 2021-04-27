package es.projectnunsys.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoProductoMapperTest {

    private TipoProductoMapper tipoProductoMapper;

    @BeforeEach
    public void setUp() {
        tipoProductoMapper = new TipoProductoMapperImpl();
    }
}
