package org.itqProj.util;

import org.itqProj.dto.pageable.PageableDto;
import org.itqProj.dto.pageable.PageableSortDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PageableUtil {
    public static PageableDto fromPage(Page<?> page) {
        return new PageableDto(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumberOfElements(),
                page.getNumber(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty(),
                new PageableSortDto(
                        page.getSort().isSorted(),
                        page.getSort().isEmpty())
                );
    }

    public static Pageable buildPageable(Integer page, Integer size, List<String> sort, long totalCount) {
        List<Sort.Order> orders = decodeSortOrders(sort);
        int p = page == null ? 0 : page;
        int s = size == null ? (totalCount <= 0 ? 1 : (int) totalCount) : size;
        return orders.isEmpty() ? PageRequest.of(p, s) : PageRequest.of(p, s, Sort.by(orders));
    }

    private static List<Sort.Order> decodeSortOrders(List<String> sort) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sort != null && !sort.isEmpty()) {
            for (String sortParam : sort) {
                String[] sortParams = sortParam.split(":");
                String property = sortParams[0];
                String direction = sortParams.length > 1 ? sortParams[1] : "ASC";
                orders.add(new Sort.Order(Sort.Direction.fromString(direction), property));
            }
        }
        return orders;
    }
}
