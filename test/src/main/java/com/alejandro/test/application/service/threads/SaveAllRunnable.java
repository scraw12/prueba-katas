package com.alejandro.test.application.service.threads;

import com.alejandro.test.application.domain.OrderDTO;
import com.alejandro.test.application.model.order.Order;
import com.alejandro.test.application.repository.OrderRepository;
import org.modelmapper.*;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SaveAllRunnable<T, R> implements Runnable {

    private List<T> dtoList;

    private R dbRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public SaveAllRunnable(List<T> dtoList, OrderRepository dbRepository) {
        this.dtoList = dtoList;
        this.dbRepository = (R) dbRepository;
    }

    @Override
    public void run() {
        if(dbRepository instanceof OrderRepository)
            this.convertAndSave();
    }

    @Transactional
    public void convertAndSave() {
        this.generateModelMapperProviderLocalDate();
        List<Order> orderList = ((List<OrderDTO>) dtoList).stream().map(dto -> this.modelMapper.map(dto, Order.class)).collect(Collectors.toList());
        ((OrderRepository) dbRepository).saveAllAndFlush(orderList);
    }

    /*
        ModelMapper no reconoce el tipo LocalDate
        *Provider: Inicializa el tipo LocalDate para que el ModelMapper la pueda tratar
        *Converter: Se genera el conversor para tratar la LocalDate
     */
    private void generateModelMapperProviderLocalDate() {
        Provider<LocalDate> localDateProvider = new AbstractProvider<LocalDate>() {
            @Override
            public LocalDate get() {
                return LocalDate.now();
            }
        };

        Converter<String, LocalDate> toStringDate = new AbstractConverter<String, LocalDate>() {
            @Override
            protected LocalDate convert(String source) {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern("M/d/yyyy"));
            }
        };

        modelMapper.createTypeMap(String.class, LocalDate.class);
        modelMapper.addConverter(toStringDate);
        modelMapper.getTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);
    }
}




