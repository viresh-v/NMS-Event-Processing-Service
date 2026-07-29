package com.hcl.faultalertengine.specification;

import com.hcl.faultalertengine.dto.AlertSearchRequest;
import com.hcl.faultalertengine.entity.Alert;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AlertSpecification {
    public static Specification<Alert> search(AlertSearchRequest request){
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getDeviceId() != null && !request.getDeviceId().isBlank()){
                predicates.add(cb.equal(root.get("deviceId"), request.getDeviceId()));
            }
            if(request.getAlertType()!=null && !request.getAlertType().isBlank()){
                predicates.add(cb.equal(root.get("alertType"), request.getAlertType()));
            }
            if(request.getSeverity()!=null){
                predicates.add(cb.equal(root.get("severity"), request.getSeverity()));
            }
            if(request.getStatus()!=null){
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
