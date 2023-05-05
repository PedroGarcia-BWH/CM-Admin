package es.uca.cm.admin.hilo;

import es.uca.cm.admin.username.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HiloService {

     @Autowired
     private HiloRepository hiloRepository;

     public Hilo save(Hilo hilo){
         return hiloRepository.save(hilo);
     }

     public void delete(Hilo hilo){
         hilo.setDateElimination(new Date());
         hiloRepository.save(hilo);
     }

     public List<Hilo> findByAutorAndHiloPadreIsNullOrderByDateCreation(Username userApp){
         return hiloRepository.findByAutorAndHiloPadreIsNullOrderByDateCreation(userApp);
     }

     public List<Hilo> findByAutorOrderByDateCreation(Username userApp){
         return hiloRepository.findByAutorOrderByDateCreation(userApp);
     }

     public List<Hilo> findByHiloPadreIsNullOrderByDateCreation(){
         return hiloRepository.findByHiloPadreIsNullOrderByDateCreation();
     }

     public List<Hilo> findByMensajeContainingIgnoreCase(String mensaje){
         return hiloRepository.findByMensajeContainingIgnoreCase(mensaje);
     }

     public Optional<Hilo> findById(UUID id){
         return hiloRepository.findById(id);
     }

     public List<Hilo> findByHiloPadreOrderByDateCreation(Hilo hiloPadre){
         return hiloRepository.findByHiloPadreOrderByDateCreation(hiloPadre);
     }

}
