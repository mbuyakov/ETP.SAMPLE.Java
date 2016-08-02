package ru.codeunited.jms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 09.10.15.
 */

public class BusinessNumberServiceImpl implements BusinessService {

    private final Logger log = LoggerFactory.getLogger(BusinessNumberServiceImpl.class);

    @Override
    public BusinessResponse processRequest(BusinessRequest request) {
        log.info(BusinessService.class.getSimpleName() + " got message [" + request.getPayload() + "]");
        Long longValue = Long.valueOf(request.getPayload());
        return new BusinessResponse("All right! RQ [" + longValue + "]");
    }
}
