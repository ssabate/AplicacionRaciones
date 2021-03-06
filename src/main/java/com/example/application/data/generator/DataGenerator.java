package com.example.application.data.generator;

import com.example.application.data.entity.Alimento;
import com.example.application.data.entity.Ingesta;
import com.example.application.data.entity.TipoComida;
import com.example.application.data.repository.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(ContactRepository contactRepository, CompanyRepository companyRepository,
                                      StatusRepository statusRepository, AlimentoRepository alimentoRepository,
                                      IngestaRepository ingestaRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (alimentoRepository.count() != 0L) {
//            if (true || contactRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            //int seed = 123;

            logger.info("Generating demo data");
//            ExampleDataGenerator<Company> companyGenerator = new ExampleDataGenerator<>(Company.class,
//                    LocalDateTime.now());
//            companyGenerator.setData(Company::setName, DataType.COMPANY_NAME);
//            List<Company> companies = companyRepository.saveAll(companyGenerator.create(5, seed));
//
//            List<Status> statuses = statusRepository
//                    .saveAll(Stream.of("Imported lead", "Not contacted", "Contacted", "Customer", "Closed (lost)")
//                            .map(Status::new).collect(Collectors.toList()));
//
//            logger.info("... generating 50 Contact entities...");
//            ExampleDataGenerator<Contact> contactGenerator = new ExampleDataGenerator<>(Contact.class,
//                    LocalDateTime.now());
//            contactGenerator.setData(Contact::setFirstName, DataType.FIRST_NAME);
//            contactGenerator.setData(Contact::setLastName, DataType.LAST_NAME);
//            contactGenerator.setData(Contact::setEmail, DataType.EMAIL);
//
//            Random r = new Random(seed);
//            List<Contact> contacts = contactGenerator.create(50, seed).stream().map(contact -> {
//                contact.setCompany(companies.get(r.nextInt(companies.size())));
//                contact.setStatus(statuses.get(r.nextInt(statuses.size())));
//                return contact;
//            }).collect(Collectors.toList());
//
//            contactRepository.saveAll(contacts);
            ingestaRepository.deleteAll();
            alimentoRepository.deleteAll();
            List<Alimento> alimentos = alimentoRepository
                    .saveAll(Arrays.asList(new Alimento("Pan", 20),
                            new Alimento("Leche", 200),
                            new Alimento("Verdura", 100),
                            new Alimento("Cerveza", 200),
                            new Alimento("Acompa??amiento", 100),
                            new Alimento("Otros", 1))
                    );
//            List<Alimento> alimentos = Arrays.asList(new Alimento("Pan", 20),
//                            new Alimento("Leche", 200),
//                            new Alimento("Verdura", 100),
//                            new Alimento("Cerveza", 200),
//                            new Alimento("Acompa??amiento", 100),
//                            new Alimento("Otros", 1))
//                    ;

//            List<Ingesta> ingestas = ingestaRepository
//                    .saveAll(Stream.of("Pan", "Leche", "Verdura", "Cerveza", "Acompa??amiento")
//                            .map(Alimento::new).map(Ingesta::new).collect(Collectors.toList()));
            List<Ingesta> ingestas = ingestaRepository
                    .saveAll(Stream.
                            generate(() -> alimentos.get(new Random().nextInt(alimentos.size()))).
//                            generate(Ingesta::new).
                            limit(10).
                            map(a -> new Ingesta(LocalDate.now(),TipoComida.COMIDA, a, 1.0)).
                            collect(Collectors.toList()));


            logger.info("Generated demo data");
        };
    }

}
