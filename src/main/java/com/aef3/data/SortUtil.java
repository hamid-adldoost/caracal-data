package com.aef3.data;

import com.aef3.data.api.qbe.SortObject;
import com.aef3.data.api.qbe.SortOrder;

public class SortUtil {

    public static SortObject generateSortObject(String sortField, String sortOrder) {
        if (sortField != null && !sortField.isEmpty() && sortOrder != null && !sortOrder.isEmpty()) {
            if (sortOrder.equalsIgnoreCase(SortOrder.ASC.name())) {
                return new SortObject(sortField, SortOrder.ASC);
            } else {
                return sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? new SortObject(sortField, SortOrder.DESC) : null;
            }
        } else {
            return null;
        }
    }

}
