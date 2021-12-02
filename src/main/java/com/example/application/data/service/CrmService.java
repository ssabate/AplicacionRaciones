package com.example.application.data.service;

import com.example.application.data.entity.Alimento;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Ingesta;
import com.example.application.data.entity.TipoComida;
import com.example.application.data.repository.AlimentoRepository;
import com.example.application.data.repository.ContactRepository;
import com.example.application.data.repository.IngestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
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

    public List<Contact> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return cR.findAll();
        } else {
            return cR.search(stringFilter);
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

    public int getIngestaCount(LocalDate fecha, TipoComida tipoComida) {

        return iR.findIngestasByDateTipoComida(fecha, tipoComida).size();
    }

    public void eliminarIngesta(Optional<Ingesta> first) {
        if(first.isPresent()) iR.delete(first.get());
        else throw new IllegalStateException();
    }

    public void modificarIngesta(Optional<Ingesta> modificado, Alimento alimento, Double raciones) {
        if(modificado.isPresent()){
            modificado.get().setAlimento(alimento);
            modificado.get().setRaciones(raciones);
            iR.save(modificado.get());
        }
    }

    public double totalRaciones(LocalDate fecha, TipoComida tipoComida) {
        return iR.totalRaciones(fecha, tipoComida);
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
