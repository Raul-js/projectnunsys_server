package es.projectnunsys.service.impl;

import es.projectnunsys.domain.TipoProducto;
import es.projectnunsys.repository.TipoProductoRepository;
import es.projectnunsys.service.TipoProductoService;
import es.projectnunsys.service.dto.TipoProductoDTO;
import es.projectnunsys.service.mapper.TipoProductoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TipoProducto}.
 */
@Service
@Transactional
public class TipoProductoServiceImpl implements TipoProductoService {

    private final Logger log = LoggerFactory.getLogger(TipoProductoServiceImpl.class);

    private final TipoProductoRepository tipoProductoRepository;

    private final TipoProductoMapper tipoProductoMapper;

    public TipoProductoServiceImpl(TipoProductoRepository tipoProductoRepository, TipoProductoMapper tipoProductoMapper) {
        this.tipoProductoRepository = tipoProductoRepository;
        this.tipoProductoMapper = tipoProductoMapper;
    }

    @Override
    public TipoProductoDTO save(TipoProductoDTO tipoProductoDTO) {
        log.debug("Request to save TipoProducto : {}", tipoProductoDTO);
        TipoProducto tipoProducto = tipoProductoMapper.toEntity(tipoProductoDTO);
        tipoProducto = tipoProductoRepository.save(tipoProducto);
        return tipoProductoMapper.toDto(tipoProducto);
    }

    @Override
    public Optional<TipoProductoDTO> partialUpdate(TipoProductoDTO tipoProductoDTO) {
        log.debug("Request to partially update TipoProducto : {}", tipoProductoDTO);

        return tipoProductoRepository
            .findById(tipoProductoDTO.getId())
            .map(
                existingTipoProducto -> {
                    tipoProductoMapper.partialUpdate(existingTipoProducto, tipoProductoDTO);
                    return existingTipoProducto;
                }
            )
            .map(tipoProductoRepository::save)
            .map(tipoProductoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoProductoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoProductos");
        return tipoProductoRepository.findAll(pageable).map(tipoProductoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoProductoDTO> findOne(Long id) {
        log.debug("Request to get TipoProducto : {}", id);
        return tipoProductoRepository.findById(id).map(tipoProductoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoProducto : {}", id);
        tipoProductoRepository.deleteById(id);
    }
}
