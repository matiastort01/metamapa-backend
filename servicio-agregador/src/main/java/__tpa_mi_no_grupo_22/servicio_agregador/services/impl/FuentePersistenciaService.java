package __tpa_mi_no_grupo_22.servicio_agregador.services.impl;

import __tpa_mi_no_grupo_22.servicio_agregador.models.entities.hechos.Fuente;
import __tpa_mi_no_grupo_22.servicio_agregador.models.repositories.IFuentesRepository;
import __tpa_mi_no_grupo_22.servicio_agregador.services.IFuentePersistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class FuentePersistenciaService implements IFuentePersistenciaService {
  @Autowired
  private IFuentesRepository fuentesRepository;

  // Cache local: Mapea Nombre -> Entidad Fuente Completa
  private final ConcurrentMap<String, Fuente> fuentesCache = new ConcurrentHashMap<>();

  // Método para obtener o crear la Fuente
  @Override
  @Transactional
  public Fuente findOrCreate(String nombreFuente) {
    // 1. Verificar primero en la caché (¡Rendimiento!)
    if (fuentesCache.containsKey(nombreFuente)) {
      return fuentesCache.get(nombreFuente); // Ya lo procesamos, devolvemos el nombre
    }

    // 2. Si no está en caché, lógica de DB (Sincronizada para evitar duplicados en race condition)
    // Usamos computeIfAbsent del mapa concurrente como un bloqueo ligero por clave
    return fuentesCache.computeIfAbsent(nombreFuente, nombre ->
        fuentesRepository.findByNombre(nombre)
            .orElseGet(() -> {
              Fuente nueva = new Fuente();
              nueva.setNombre(nombre);
              return fuentesRepository.save(nueva);
            }));
  }
}
