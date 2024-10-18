package com.decafmango.lab_1;

import jakarta.validation.ValidationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public class Pagination {

    public static Pageable createPageTemplate(Integer from, Integer size) {
        Pageable page = null;
        if (from != null && size != null) {
            if (from < 0 || size <= 0)
                throw new ValidationException("The pagination parameters should be as follows: from >= 0 and size > 0.");
            page = PageRequest.of(from / size, size);
        }
        return page;
    }

}
