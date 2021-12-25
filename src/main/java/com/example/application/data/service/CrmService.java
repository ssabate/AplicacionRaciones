package com.example.application.data.service;

import com.example.application.data.entity.Alimento;
import com.example.application.data.entity.Ingesta;
import com.example.application.data.entity.TipoComida;
import com.example.application.data.repository.AlimentoRepository;
import com.example.application.data.repository.ContactRepository;
import com.example.application.data.repository.IngestaRepository;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Transactional          //añadida para poder borrar todos los alimentos de una ingesta
public class CrmService {

    private ContactRepository cR;
    private AlimentoRepository aR;
    private IngestaRepository iR;

    @Autowired
    public CrmService(ContactRepository cR, AlimentoRepository aR, IngestaRepository iR) {
        this.aR=aR;
        this.cR = cR;
        this.iR = iR;
    }

    public List<Alimento> findAllAlimentos() {
        return aR.findAll();
    }

    public List<Alimento> findAllAlimentos(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return aR.findAll();
        } else {
            return aR.search(stringFilter);
        }
    }

    public Collection<Ingesta> findAllIngestas() {
        return iR.findAll();
    }

    public Alimento findAlimentoByNombre(String nombre) {
        return aR.findByNombre(nombre);
    }

    public List<Ingesta> findIngestasByDateTipoComida(LocalDate fecha, TipoComida tipoComida) {
        return iR.findIngestasByDateTipoComida(fecha, tipoComida);
    }

    public void insertarIngesta(Ingesta ing) {
        Optional<Ingesta> repe=iR.findIngestasByDateTipoComidaAlimento(ing.getDate(), ing.getComida(), ing.getAlimento());
        if(repe.isPresent()){
            repe.get().setRaciones(repe.get().getRaciones()+ing.getRaciones());
            iR.save(repe.get());
        }
        else iR.save(ing);
    }

    /**
     * The contents of this method are just for demo purposes and irrelevant for the features that want to be presented.
     * @param offset
     * @param limit
     * @param fecha
     * @param tipoComida
     * @return
     */
    public Stream<Ingesta> fetchIngestas(int offset, int limit, LocalDate fecha, TipoComida tipoComida) {
        return iR.fetchIngestas( fecha, tipoComida, PageRequest.of(offset, limit)).stream();
    }

    //devuelve la cantidad de alimentos existente en una ingesta, señalada con la fecha y tipo de alimento
    public int getIngestaCount(LocalDate fecha, TipoComida tipoComida) {
        return iR.findIngestasByDateTipoComida(fecha, tipoComida).size();
    }

    //elimina la ingesta que recibe
    public void eliminarIngesta(Optional<Ingesta> first) {
        if(first.isPresent()) iR.delete(first.get());
        else throw new IllegalStateException();
    }

    //Modifica el alimento y cantidad de raciones de la ingesta que recibe
    public void modificarIngesta(Optional<Ingesta> modificado, Alimento alimento, Double raciones) {
        if(modificado.isPresent()){
            modificado.get().setAlimento(alimento);
            modificado.get().setRaciones(raciones);
            iR.save(modificado.get());
        }
    }

    //Devuelve la suma de las raciones de HCs consumidas en una ingesta
    public double totalRaciones(LocalDate fecha, TipoComida tipoComida) {
        if(iR.totalRaciones(fecha, tipoComida).isPresent()) return iR.totalRaciones(fecha, tipoComida).get();
        return 0.0;
    }

    public void eliminarIngestas(LocalDate fecha, TipoComida tipoComida) {
        iR.deleteByDateAndComida(fecha, tipoComida);

    }

    public Alimento findAllFoods(String value) {
        return null;
    }

    public void eliminarAlimento(Alimento food) {
        try{
        aR.delete(food);
        }catch(Exception e){
            // When creating a notification using the `show` static method,
// the duration is 5-sec by default.
            Notification notification = Notification.show("Alimento usado en ingesta no puede ser borrado");
        }
    }

    public void insertarAlimento(Alimento alimento) {
        aR.save(alimento);
//        aR.saveAll(Arrays.asList(alimento));
    }


//    public interface IngestaService {
//        List<Ingesta> fetchIngestas(int offset, int limit);
//        int getIngestaCount();
//    }
//
//    //--------------------------------------------------------------------------
//    public int count() {
//        long cc = iR.count();
//        int count = (int)cc;
//        return(count);
//    }
//
//    //--------------------------------------------------------------------------
//    public Stream<Ingesta> getPage(int offset, int limit) {
//        Pageable pageable = PageRequest.of(offset, limit);
//        Page<Ingesta> page = iR.findAll(pageable);
//        Stream<Ingesta> stm = page.stream();
//        return(stm);
//    }
//
//    public class IngestaDataProvider<F> extends AbstractDataProvider<Ingesta, F> {
//        private static final long serialVersionUID = 0L;
//
//
//        //--------------------------------------------------------------------------
//        public IngestaDataProvider() {
//        }
//
//        //--------------------------------------------------------------------------
//        @Override
//        public Stream<Ingesta> fetch(Query<Ingesta, F> query) {
//            int limit = query.getLimit();
//            int offset = query.getOffset();
//            Stream<Ingesta> stm = getPage(offset, limit);
//            return (stm);
//        }
//
//        //--------------------------------------------------------------------------
//        @Override
//        public boolean isInMemory() {
//            return false;
//        }
//
//        //--------------------------------------------------------------------------
//        @Override
//        public int size(Query<Ingesta, F> query) {
//            int count = count();   // NPE here
//            return (count);
//        }
//
//        DataProvider<Ingesta, String> getDataProvider(
//                CrmService service) {
//            DataProvider<Ingesta, Predicate<Ingesta>>
//                    predicateDataProvider =
//                    DataProvider.fromFilteringCallbacks(
//                            query -> service.fetch(query.getOffset(),
//                                    query.getLimit(),
//                                    query.getFilter()).stream(),
//                            query -> service.getCount(query.getFilter()));
//
//            DataProvider<Ingesta, String> dataProvider =
//                    predicateDataProvider.withConvertedFilter(
//                            text -> (person -> person.getComida().toString()
//                                    .startsWith(text)));
//
//            return dataProvider;
//        }
//    }
}
