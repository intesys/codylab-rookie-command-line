package it.intesys.codylab.rookie.api;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Pagination {
    public static Pageable buildPageable(@NotNull Integer page, @NotNull Integer size, @Nullable String sort){
        if(sort == null || sort.isEmpty()){
            return PageRequest.of(page, size);
        }

        String[] sortSplit = sort.split(",");
        Sort.Order order;
        if(sortSplit.length == 2){
            order = new Sort.Order(Sort.Direction.fromString(sortSplit[1]), sortSplit[0]);
        } else {
            order = Sort.Order.by(sortSplit[0]);
        }

        return PageRequest.of(page, size, Sort.by(order));
    }

}
