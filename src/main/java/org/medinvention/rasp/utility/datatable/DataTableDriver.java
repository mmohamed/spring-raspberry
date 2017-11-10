/*******************************************************************************
 * Copyright 2017 Marouan MOHAMED
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.medinvention.rasp.utility.datatable;

import java.lang.reflect.Method;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.medinvention.rasp.utility.http.exception.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class DataTableDriver {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<?> targetClass;
    private List<String> fields;
    private PageRequest pageRequest;
    private String searchTerm;
    private HashMap<String, String> filters;
    private String echo;

    private boolean binded;

    public void bind(HttpServletRequest request, Class<?> targetClass, List<String> fields) throws Exception {

        if (this.binded) {
            throw new Exception("DataTableDriver must be cleared first !");
        }

        this.targetClass = targetClass;
        this.fields = fields;

        if (this.fields.size() == 0) {
            throw new Exception("DataTable needs field !");
        }

        this.extract(request);

        this.binded = true;
    }

    public DataTableResponse getData() throws Exception {
        if (!this.binded) {
            throw new Exception("DataTableDataSource must be binded first !");
        }

        List<?> data = this.selectData();

        DataTableResponse response = new DataTableResponse();

        response.setiTotalRecords(this.countAll());
        response.setiTotalDisplayRecords(this.countDisplay());
        response.setsEcho(this.echo);
        response.setAaData(this.format(data));

        return response;
    }

    public DataTableResponse getData(DataTableCallback callback) throws Exception {
        DataTableResponse response = this.getData();
        callback.callback(response);
        return response;
    }

    private long countAll() {
        Query query = this.entityManager.createQuery("SELECT count(*) FROM " + this.targetClass.getName());

        return (long) query.getSingleResult();
    }

    private long countDisplay() throws Exception {
        String where = this.buildWhere();

        String HQL = "SELECT count(a) FROM " + this.targetClass.getName() + " a " + where;

        Query query = this.entityManager.createQuery(HQL);

        this.bindQueryParameters(query);

        return (long) query.getSingleResult();
    }

    private List<?> selectData() throws Exception {
        if (this.filters.size() > 0) {
            throw new Exception("Filters not implemented yet !");
        }

        String orderBy = this.buildOrder();

        String where = this.buildWhere();

        String HQL = "SELECT a FROM " + this.targetClass.getName() + " a " + where + orderBy;

        Query query = this.entityManager.createQuery(HQL);

        query.setFirstResult(this.pageRequest.getOffset());
        query.setMaxResults(this.pageRequest.getPageSize());

        this.bindQueryParameters(query);

        return query.getResultList();
    }

    private String buildOrder() {
        Order order;
        String orderBy = null;

        if (this.pageRequest.getSort() != null) {
            Iterator<Order> cursor = this.pageRequest.getSort().iterator();

            while (cursor.hasNext()) {
                order = cursor.next();
                if (orderBy == null) {
                    orderBy = " ORDER BY ";
                }
                else {
                    orderBy += ",";
                }
                orderBy += ("a." + order.getProperty() + " " + order.getDirection().toString());
            }
        }
        return (orderBy == null) ? "" : orderBy;
    }

    private String buildWhere() throws Exception {
        if (this.searchTerm == null) {
            return "";
        }

        String where = " WHERE (1 = 0";

        for (int i = 0; i < this.fields.size(); i++) {
            String methodName = "get" + this.fields.get(i).substring(0, 1).toUpperCase()
                    + this.fields.get(i).substring(1);

            if (this.targetClass.getMethod(methodName).getReturnType() == String.class) {
                where += " OR a." + this.fields.get(i) + " LIKE :search" + i;
            }
        }

        where += ")";

        return where;
    }

    private void bindQueryParameters(Query query) throws Exception {
        if (this.searchTerm != null) {

            for (int i = 0; i < this.fields.size(); i++) {
                String methodName = "get" + this.fields.get(i).substring(0, 1).toUpperCase()
                        + this.fields.get(i).substring(1);

                if (this.targetClass.getMethod(methodName).getReturnType() == String.class) {
                    query.setParameter("search" + i, "%" + this.searchTerm + "%");
                }
            }
        }
    }

    private ArrayList<ArrayList<String>> format(List<?> data) throws Exception {

        ArrayList<ArrayList<String>> formatedData = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < data.size(); i++) {

            ArrayList<String> row = new ArrayList<String>();

            for (int j = 0; j < this.fields.size(); j++) {
                String methodName = "get" + this.fields.get(j).substring(0, 1).toUpperCase()
                        + this.fields.get(j).substring(1);
                Method method = data.get(i).getClass().getMethod(methodName);

                Object fieldValue = method.invoke(data.get(i));

                if (fieldValue instanceof Date) {
                    Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    row.add(formatter.format(method.invoke(data.get(i))));
                }
                else {
                    row.add(fieldValue == null ? "" : String.valueOf(fieldValue));
                }
            }
            formatedData.add(row);
        }
        return formatedData;
    }

    public void detach() {
        this.binded = false;
        this.targetClass = null;
        this.fields = null;
        this.pageRequest = null;
        this.echo = null;
        this.filters = null;
        this.searchTerm = null;
    }

    private void extract(HttpServletRequest request) throws BadRequestException {
        if (!"XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            throw new BadRequestException("Request must be ajax type !");
        }

        int page = 0;
        int size = 20;

        if (request.getParameter("iDisplayStart") != null && request.getParameter("iDisplayLength") != "-1") {
            page = Integer.valueOf(request.getParameter("iDisplayStart"));
            size = Integer.valueOf(request.getParameter("iDisplayLength"));
        }

        ArrayList<Order> orders = new ArrayList<Order>();

        if (request.getParameter("iSortCol_0") != null) {
            for (int i = 0; i < Integer.valueOf(request.getParameter("iSortingCols")); i++) {

                if (request.getParameter("bSortable_" + request.getParameter("iSortCol_" + i)) == null) {
                    continue;
                }

                if (request.getParameter("bSortable_" + request.getParameter("iSortCol_" + i)).toLowerCase()
                        .compareTo("true") == 0) {
                    if (request.getParameter("sSortDir_" + i).toLowerCase().compareTo("asc") == 0) {
                        orders.add(new Order(Direction.ASC, this.fields.get(i)));
                    }
                    else if (request.getParameter("sSortDir_" + i).toLowerCase().compareTo("desc") == 0) {
                        orders.add(new Order(Direction.DESC, this.fields.get(i)));
                    }
                    else {
                        throw new BadRequestException(String.format("Invalid sord direction '%s' for field '%s'",
                                request.getParameter("sSortDir_" + i), this.fields.get(i)));
                    }
                }
            }
        }

        if (orders.isEmpty()) {
            this.pageRequest = new PageRequest(page, size);
        }
        else {
            this.pageRequest = new PageRequest(page, size, new Sort(orders));
        }

        if (request.getParameter("sSearch") != null && request.getParameter("sSearch") != "") {
            this.searchTerm = request.getParameter("sSearch");
        }

        this.filters = new HashMap<String, String>();

        for (int i = 0; i < this.fields.size(); i++) {
            if (request.getParameter("bSearchable_" + i) != null
                    && request.getParameter("bSearchable_" + i).toLowerCase().compareTo("true") == 0
                    && request.getParameter("sSearch_" + i) != "") {
                this.filters.put(this.fields.get(i), request.getParameter("sSearch_" + i));
            }
        }

        this.echo = request.getParameter("sEcho");
    }

    public List<String> getFields() {
        return fields;
    }

    public PageRequest getPageRequest() {
        return pageRequest;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public HashMap<String, String> getFilters() {
        return filters;
    }

    public String getEcho() {
        return echo;
    }

}
